package de.mih.core.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.mih.core.engine.ai.Pathfinder;
import de.mih.core.engine.ecs.BlueprintManager;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.ecs.EventManager;
import de.mih.core.engine.ecs.SystemManager;
import de.mih.core.engine.io.TilemapReader;
import de.mih.core.engine.render.AdvancedCamera;
import de.mih.core.engine.render.Visual;
import de.mih.core.engine.tilemap.Tile;
import de.mih.core.engine.tilemap.Tilemap;
import de.mih.core.game.components.ColliderC;
import de.mih.core.game.components.Control;
import de.mih.core.game.components.NodeC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.SelectableC;
import de.mih.core.game.components.TilemapC;
import de.mih.core.game.components.VelocityC;
import de.mih.core.game.components.VisualC;
import de.mih.core.game.input.CircularContextMenu;
import de.mih.core.game.input.ClickListener;
import de.mih.core.game.input.InGameInput;
import de.mih.core.game.player.Player;
import de.mih.core.game.render.RenderManager;
import de.mih.core.game.render.TilemapRenderer;
import de.mih.core.game.systems.ControllerSystem;
import de.mih.core.game.systems.MoveSystem;
import de.mih.core.game.systems.RenderSystem;
import de.mih.core.game.tilemap.borders.Door;
import de.mih.core.game.tilemap.borders.RoomBorderColliderFactory;
import de.mih.core.game.tilemap.borders.Wall;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;

public class MiH extends ApplicationAdapter {

	static EntityManager entityM;
	static SystemManager systemM;
	static EventManager eventM;
	static RenderSystem rs;
	static ControllerSystem cs;
	static MoveSystem ms;
	//static UnitTypeParser utp;
	static Pathfinder pf;
	static TilemapReader tilemapReader;
	static InputMultiplexer input;
	static Tilemap map;
	static InGameInput inGameInput;
	static CircularContextMenu contextMenu;
	static TilemapRenderer tilemapRenderer;

	static AssetManager assetManager;
	
	Player activePlayer;
	int cam_target = -1;

	Map<Tile, Tile> path;

	public void create() {
		entityM = EntityManager.getInstance();
		systemM = new SystemManager(entityM, 5);
		eventM = new EventManager();
		
		activePlayer = new Player("localplayer",0,entityM);

		
		assetManager = new AssetManager();
		//Gdx.files.internal("assets/textures/contextmenu_bg.png");
		assetManager.load("assets/textures/contextmenu_bg.png", Texture.class);
		assetManager.load("assets/models/wall.obj",Model.class);
		assetManager.load("assets/models/door.obj",Model.class);
		PerspectiveCamera camera = new PerspectiveCamera(75, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(2f, 5f, 3f);
		camera.lookAt(0f, 0f, 0f);
		camera.near = 0.1f;
		camera.far = 300f;
		
		assetManager.finishLoading();
		rs = new RenderSystem(systemM, entityM, eventM, camera
				);
		input = new InputMultiplexer();
		
		cs = new ControllerSystem(systemM, entityM, eventM, rs, Gdx.input);
		
		while(!assetManager.isLoaded("assets/textures/contextmenu_bg.png"))
		{
			System.out.println("Loading textures!");
		}
		Wall.wallVisual = new Visual(assetManager.get("assets/models/wall.obj",Model.class));
		Door.doorVisual = new Visual(assetManager.get("assets/models/door.obj",Model.class));
		
		RenderManager.getInstance().setCamera(camera);
		contextMenu = new CircularContextMenu(50, assetManager.get("assets/textures/contextmenu_bg.png",Texture.class));
		

		contextMenu.getButton(0).addClickListener(() -> System.out.println("Button 1 pressed!"));
		contextMenu.getButton(1).addClickListener(() -> System.out.println("Button 2 pressed!"));
		contextMenu.getButton(2).addClickListener(() -> System.out.println("Button 3 pressed!"));
		contextMenu.getButton(3).addClickListener(() -> System.out.println("Button 4 pressed!"));
		contextMenu.getButton(4).addClickListener(() -> System.out.println("Button 5 pressed!"));
		contextMenu.getButton(5).addClickListener(() -> System.out.println("Button 6 pressed!"));

		inGameInput = new InGameInput(activePlayer,contextMenu,entityM,rs.camera);
		input.addProcessor(contextMenu);
		input.addProcessor(inGameInput);
		input.addProcessor(cs);

		Gdx.input.setInputProcessor(input);
		tilemapReader = new TilemapReader(rs, entityM, RoomBorderColliderFactory.getInstance());
		//utp = new UnitTypeParser(rs, entityM);
		pf = new Pathfinder();

		map = tilemapReader.readMap("assets/maps/map1.xml");
		
		tilemapRenderer = new TilemapRenderer(map);
		
		System.out.println(map);
		ms = new MoveSystem(systemM, entityM, eventM, map);
	
		// Robocop!!!111elf
		//"assets/unittypes/" + unittype + ".xml"
		BlueprintManager.getInstance().registerComponentType(ColliderC.name, ColliderC.class);
		BlueprintManager.getInstance().registerComponentType(Control.name, Control.class);
		BlueprintManager.getInstance().registerComponentType(PositionC.name, PositionC.class);
		BlueprintManager.getInstance().registerComponentType(SelectableC.name, SelectableC.class);
		BlueprintManager.getInstance().registerComponentType(VelocityC.name, VelocityC.class);
		BlueprintManager.getInstance().registerComponentType(VisualC.name, VisualC.class);

		BlueprintManager.getInstance().readBlueprintFromXML("assets/unittypes/robocop.xml");
		//BlueprintManager.getInstance().readBlueprintFromXML(node);
		//utp.newUnit("robocop");
		
	
		EntityManager.getInstance().getComponent(BlueprintManager.getInstance().createEntityFromBlueprint("robocop"), PositionC.class).position.x = 1f;
		
		EntityManager.getInstance().getComponent(BlueprintManager.getInstance().createEntityFromBlueprint("robocop"), PositionC.class).position.z = -1f;
		
		EntityManager.getInstance().getComponent(BlueprintManager.getInstance().createEntityFromBlueprint("robocop"), PositionC.class).position.x = -1f;
		//

		// TODO: Delete! (Pathfinder-Test)
		//end = entityM.getComponent(map, TilemapC.class).getTileAt(1, 1);
		//
		
		this.spriteBatch = new SpriteBatch();
	}

	// TODO: Delete! (Pathfinder-Test)
	Tile start = null;
	Tile end = null;
	//
	SpriteBatch spriteBatch;
	Map<Tile,Integer> pathToEntity = new HashMap<>();

	public void render() {
		
		// TODO: Delete! (Pathfinder-Test)
		for (int i = 0; i < entityM.entityCount; i++) {
			if (entityM.hasComponent(i, NodeC.class)) {
				if (!entityM.getComponent(i, NodeC.class).blocked && entityM.hasComponent(i, VisualC.class)) {
					entityM.removeComponent(i, entityM.getComponent(i, VisualC.class));
				}
			}
		}
		//tilemap = entityM.getComponent(map, TilemapC.class);
		int x = map.coordToIndex_x(RenderManager.getInstance().getMouseTarget(0, Gdx.input).x);
		int z = map.coordToIndex_z(RenderManager.getInstance().getMouseTarget(0, Gdx.input).z);
		start = map.getTileAt(0, 0);
//		if (x >= 0 && x < tilemap.length && z >= 0 && z < tilemap.width) {
//			if (!entityM.getComponent(tilemap.getTileAt(x, z), NodeC.class).blocked)
//				end = map.getTileAt(x, z);
//		}
//		path = pf.findShortesPath(start, end);
//		Tile tmp = end;
//		while (tmp != null) {
//			int current;
//			if(!pathToEntity.containsKey(tmp))
//			{
//				pathToEntity.put(tmp, entityM.createEntity());
//			}
//			current = pathToEntity.get(tmp);
//			entityM.addComponent(current, new VisualC("redbox"));
//			entityM.getComponent(current, VisualC.class).visual.pos.y = tilemap.TILE_SIZE / 2f;
//			entityM.getComponent(current, VisualC.class).visual.setScale(tilemap.TILE_SIZE,tilemap.TILE_SIZE, tilemap.TILE_SIZE);
//			if(path.containsKey(tmp))
//				tmp = path.get(tmp);
//			else
//				tmp = null;
//		}
		//

		systemM.update(Gdx.graphics.getDeltaTime());
		
		RenderManager.getInstance().startRender();
		tilemapRenderer.render();
		systemM.render(Gdx.graphics.getDeltaTime());
		RenderManager.getInstance().endRender();
		
	//	this.contextMenu.update();
		this.spriteBatch.begin();
		this.contextMenu.render(spriteBatch);
		this.spriteBatch.end();
		
		//test
	}
}
