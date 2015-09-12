package de.mih.core.game;


import java.util.HashMap;
import java.util.Map;

import de.mih.core.engine.ai.Pathfinder;
import de.mih.core.engine.ecs.BlueprintManager;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.ecs.EventManager;
import de.mih.core.engine.ecs.SystemManager;
import de.mih.core.engine.io.AdvancedAssetManager;
import de.mih.core.engine.io.TilemapReader;
import de.mih.core.engine.tilemap.Tilemap;
import de.mih.core.engine.tilemap.Tile;
import de.mih.core.game.ai.orders.MoveOrder;
import de.mih.core.game.components.ColliderC;
import de.mih.core.game.components.Control;
import de.mih.core.game.components.NodeC;
import de.mih.core.game.components.OrderableC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.SelectableC;
import de.mih.core.game.components.VelocityC;
import de.mih.core.game.components.VisualC;
import de.mih.core.game.input.CircularContextMenu;
import de.mih.core.game.input.InGameInput;
import de.mih.core.game.player.Player;
import de.mih.core.game.render.RenderManager;
import de.mih.core.game.render.TilemapRenderer;
import de.mih.core.game.systems.ControllerSystem;
import de.mih.core.game.systems.MoveSystem;
import de.mih.core.game.systems.OrderSystem;
import de.mih.core.game.systems.PlayerSystem;
import de.mih.core.game.systems.RenderSystem;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class MiH extends ApplicationAdapter {

	EntityManager entityM = EntityManager.getInstance();
	SystemManager systemM = SystemManager.getInstance();
	EventManager eventM = EventManager.getInstance();
	RenderSystem rs;
	ControllerSystem cs;
	OrderSystem os;
	MoveSystem ms;
	Pathfinder pf;
	TilemapReader tr;
	InputMultiplexer input;
	Tilemap tilemap;
	InGameInput inGameInput;
	CircularContextMenu contextMenu;
	AdvancedAssetManager assetManager;

	//public static AssetManager assetManager;

	public static Player activePlayer;
	int cam_target = -1;

	Map<Tile, Tile> path;
	private TilemapReader tilemapReader;
	private TilemapRenderer tilemapRenderer;
	private SpriteBatch spriteBatch;

	public void create() {
		activePlayer = new Player("localplayer", 0, entityM);

		assetManager = AdvancedAssetManager.getInstance();
		// Gdx.files.internal("assets/textures/contextmenu_bg.png");
		assetManager.assetManager.load("assets/textures/contextmenu_bg.png", Texture.class);
		PerspectiveCamera camera = new PerspectiveCamera(75, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(2f, 5f, 3f);
		camera.lookAt(0f, 0f, 0f);
		camera.near = 0.1f;
		camera.far = 300f;
		assetManager.assetManager.finishLoading();
		rs = new RenderSystem(systemM, entityM, eventM, camera
				);
		input = new InputMultiplexer();

		cs = new ControllerSystem(rs, Gdx.input);

		new PlayerSystem(rs);
		BlueprintManager.getInstance().registerComponentType(ColliderC.name, ColliderC.class);
		BlueprintManager.getInstance().registerComponentType(Control.name, Control.class);
		BlueprintManager.getInstance().registerComponentType(PositionC.name, PositionC.class);
		BlueprintManager.getInstance().registerComponentType(SelectableC.name, SelectableC.class);
		BlueprintManager.getInstance().registerComponentType(VelocityC.name, VelocityC.class);
		BlueprintManager.getInstance().registerComponentType(VisualC.name, VisualC.class);
		BlueprintManager.getInstance().registerComponentType(OrderableC.name,OrderableC.class);

		BlueprintManager.getInstance().readBlueprintFromXML("assets/unittypes/robocop.xml");
		BlueprintManager.getInstance().readBlueprintFromXML("assets/unittypes/wall.xml");
		BlueprintManager.getInstance().readBlueprintFromXML("assets/unittypes/door.xml");


//		Wall.wallVisual = new Visual(assetManager.get("assets/models/wall.obj",Model.class));
//		Door.doorVisual = new Visual(assetManager.get("assets/models/door.obj",Model.class));
		
		RenderManager.getInstance().setCamera(camera);
		contextMenu = new CircularContextMenu(50, assetManager.assetManager.get("assets/textures/contextmenu_bg.png",Texture.class));
		

		contextMenu.getButton(0).addClickListener(() -> {
			PositionC pos = entityM.getComponent(activePlayer.selectedunits.get(0), PositionC.class);
			//TilemapC tilemap = entityM.getComponent(map, TilemapC.class);
			Vector3 mouseTarget = RenderManager.getInstance().getMouseTarget(0, Gdx.input);

			Tile start = tilemap.getTileAt(tilemap.coordToIndex_x((int)pos.position.x), tilemap.coordToIndex_z((int)pos.position.z));
			Tile end = tilemap.getTileAt(tilemap.coordToIndex_x(mouseTarget.x),tilemap.coordToIndex_z(mouseTarget.z));
			MoveOrder order = new MoveOrder(RenderManager.getInstance().getMouseTarget(0f, Gdx.input),
					start,
					end,
					pf.findShortesPath(start, end),
					tilemap);
			
			entityM.getComponent(activePlayer.selectedunits.get(0), OrderableC.class).newOrder(order);
		});
		contextMenu.getButton(1).addClickListener(() -> System.out.println("Button 2 pressed!"));
		contextMenu.getButton(2).addClickListener(() -> System.out.println("Button 3 pressed!"));
		contextMenu.getButton(3).addClickListener(() -> System.out.println("Button 4 pressed!"));
		contextMenu.getButton(4).addClickListener(() -> System.out.println("Button 5 pressed!"));
		contextMenu.getButton(5).addClickListener(() -> System.out.println("Button 6 pressed!"));

		inGameInput = new InGameInput(activePlayer, contextMenu, entityM, rs.camera);
		input.addProcessor(contextMenu);
		input.addProcessor(inGameInput);
		input.addProcessor(cs);

		Gdx.input.setInputProcessor(input);
		tilemapReader = new TilemapReader();

		pf = new Pathfinder();

		tilemap = tilemapReader.readMap("assets/maps/map1.xml");
		os = new OrderSystem(pf, tilemap);

		tilemapRenderer = new TilemapRenderer(tilemap);
		


//		new OrderSystem(pf, EntityManager.getInstance().getComponent(map, TilemapC.class));
//
		ms = new MoveSystem(tilemap);


		//"assets/unittypes/" + unittype + ".xml"
		
		//BlueprintManager.getInstance().readBlueprintFromXML(node);
		//utp.newUnit("robocop");
		
	
		PositionC tmp = EntityManager.getInstance().getComponent(BlueprintManager.getInstance().createEntityFromBlueprint("robocop"), PositionC.class);
		tmp.position.x = 1;
		tmp.position.z = 1;
		
		tmp = EntityManager.getInstance().getComponent(BlueprintManager.getInstance().createEntityFromBlueprint("robocop"), PositionC.class);
		tmp.position.x = 2;
		tmp.position.z = 1;
		
		tmp = EntityManager.getInstance().getComponent(BlueprintManager.getInstance().createEntityFromBlueprint("robocop"), PositionC.class);
		tmp.position.x = 1;
		tmp.position.z = 2;


		this.spriteBatch = new SpriteBatch();
	}

	Map<Tile,Integer> pathToEntity = new HashMap<>();
	private Tile start;
	private Tile end = null;

	public void render() {
		
		// TODO: Delete! (Pathfinder-Test)
		for (int i = 0; i < entityM.entityCount; i++) {
			if (entityM.hasComponent(i, NodeC.class)) {
				if (!entityM.getComponent(i, NodeC.class).blocked && entityM.hasComponent(i, VisualC.class)) {
					entityM.removeComponent(i, entityM.getComponent(i, VisualC.class));
				}
			}
		}
//		Vector3 mouseTarget = RenderManager.getInstance().getMouseTarget(0, Gdx.input);
//		//tilemap = entityM.getComponent(map, TilemapC.class);
////		System.out.println(mouseTarget);
//		int x = tilemap.coordToIndex_x(mouseTarget.x);
//		int z = tilemap.coordToIndex_z(mouseTarget.z);
//		start = tilemap.getTileAt(0, 0);
//		if (x >= 0 && x < tilemap.getLength() && z >= 0 && z < tilemap.getWidth()) {
//			end = tilemap.getTileAt(x, z);
//		}
////		System.out.println(x + ", " + z);
//		//pathToEntity.values().forEach(entity -> entityM.removeEntity(entity));
//		if(end != null && start != null)
//		{
//			path = pf.findShortesPath(start, end);
//			System.out.println("path found");
//			Tile tmp = start;
//			while (tmp != null) {
//				int current;
//				if(tmp != null && path.containsKey(tmp))
//					System.out.println(tmp + " -> " + path.get(tmp));
//				if(!pathToEntity.containsKey(tmp))
//				{
//					pathToEntity.put(tmp, entityM.createEntity());
//				}
//				current = pathToEntity.get(tmp);
//				entityM.addComponent(current,new PositionC());
//				entityM.addComponent(current, new VisualC("redbox"));
//				entityM.getComponent(current, VisualC.class).visual.pos.y = tilemap.getTILESIZE() / 2f;
//				entityM.getComponent(current, PositionC.class).position = tmp.getCenter().cpy();
//				//entityM.getComponent(current, VisualC.class).visual.setScale(tilemap.getTILESIZE(),tilemap.getTILESIZE(), tilemap.getTILESIZE());
//				if(tmp == end)
//					break;
//				if(path.containsKey(tmp))
//					tmp = path.get(tmp);
//				else
//					tmp = null;
//			}
//			
//		}


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
