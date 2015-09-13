package de.mih.core.game;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import de.mih.core.engine.ai.Pathfinder;
import de.mih.core.engine.ecs.BlueprintManager;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.ecs.EventManager;
import de.mih.core.engine.ecs.RenderManager;
import de.mih.core.engine.ecs.SystemManager;
import de.mih.core.engine.ecs.events.orderevents.OrderToPoint_Event;
import de.mih.core.engine.io.TilemapReader;
import de.mih.core.engine.render.Visual;
import de.mih.core.engine.tilemap.Tilemap;
import de.mih.core.engine.tilemap.Tile;
import de.mih.core.game.ai.orders.MoveOrder;
import de.mih.core.game.components.ColliderC;
import de.mih.core.game.components.Control;
import de.mih.core.game.components.InteractableC;
import de.mih.core.game.components.NodeC;
import de.mih.core.game.components.OrderableC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.SelectableC;
import de.mih.core.game.components.TilemapC;
import de.mih.core.game.components.VelocityC;
import de.mih.core.game.components.VisualC;
import de.mih.core.game.input.InGameInput;
import de.mih.core.game.player.Interaction;
import de.mih.core.game.player.Player;
import de.mih.core.game.player.input.contextmenu.CircularContextMenu;
import de.mih.core.game.render.CircularContextMenuRenderer;
import de.mih.core.game.render.TilemapRenderer;
import de.mih.core.game.systems.ControllerSystem;
import de.mih.core.game.systems.MoveSystem;
import de.mih.core.game.systems.OrderSystem;
import de.mih.core.game.systems.PlayerSystem;
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

	EntityManager entityM;
	SystemManager systemM;
	EventManager eventM;
	RenderSystem rs;
	ControllerSystem cs;
	OrderSystem os;
	MoveSystem ms;
	static public Pathfinder pf;
	TilemapReader tr;
	InGameInput inGameInput;
	public static InputMultiplexer input;
	CircularContextMenu contextMenu;
	public static Tilemap tilemap;

	public static AssetManager assetManager;

	public static Player activePlayer;
	int cam_target = -1;

	Map<Tile, Tile> path;
	private TilemapReader tilemapReader;
	private TilemapRenderer tilemapRenderer;

	public void create() {

		entityM = EntityManager.getInstance();
		systemM = SystemManager.getInstance();
		eventM = EventManager.getInstance();

		activePlayer = new Player("localplayer", 0, entityM);

		assetManager = new AssetManager();
		assetManager.load("assets/textures/contextmenu_bg.png", Texture.class);
		assetManager.load("assets/icons/sit.png", Texture.class);
		assetManager.load("assets/models/wall.obj", Model.class);
		assetManager.load("assets/models/door.obj", Model.class);
		assetManager.load("assets/models/selectioncircle.obj", Model.class);
		assetManager.load("assets/models/chair.obj", Model.class);
		PerspectiveCamera camera = new PerspectiveCamera(75, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(2f, 5f, 3f);
		camera.lookAt(0f, 0f, 0f);
		camera.near = 0.1f;
		camera.far = 300f;
		
		assetManager.finishLoading();
		rs = new RenderSystem(systemM, entityM, eventM, camera);
		input = new InputMultiplexer();

		cs = new ControllerSystem(rs, Gdx.input);

		new PlayerSystem(rs);

		while (!assetManager.isLoaded("assets/textures/contextmenu_bg.png")) {
			System.out.println("Loading textures!");
		}

		Wall.wallVisual = new Visual(assetManager.get("assets/models/wall.obj", Model.class));
		Door.doorVisual = new Visual(assetManager.get("assets/models/door.obj", Model.class));

		RenderManager.getInstance().setCamera(camera);
		contextMenu = new CircularContextMenu();
		inGameInput = new InGameInput(activePlayer, contextMenu, entityM, camera);
		input.addProcessor(contextMenu);
		input.addProcessor(inGameInput);
		input.addProcessor(cs);

		Gdx.input.setInputProcessor(input);
		tilemapReader = new TilemapReader(rs, entityM, RoomBorderColliderFactory.getInstance());

		pf = new Pathfinder();

		tilemap = tilemapReader.readMap("assets/maps/map1.xml");
		os = new OrderSystem(pf, tilemap);

		tilemapRenderer = new TilemapRenderer(tilemap);
		new CircularContextMenuRenderer(this.contextMenu);

		ms = new MoveSystem(tilemap);


		BlueprintManager.getInstance().registerComponentType(ColliderC.name, ColliderC.class);
		BlueprintManager.getInstance().registerComponentType(Control.name, Control.class);
		BlueprintManager.getInstance().registerComponentType(PositionC.name, PositionC.class);
		BlueprintManager.getInstance().registerComponentType(SelectableC.name, SelectableC.class);
		BlueprintManager.getInstance().registerComponentType(VelocityC.name, VelocityC.class);
		BlueprintManager.getInstance().registerComponentType(VisualC.name, VisualC.class);
		BlueprintManager.getInstance().registerComponentType(OrderableC.name,OrderableC.class);
		BlueprintManager.getInstance().registerComponentType(InteractableC.name, InteractableC.class);

		BlueprintManager.getInstance().readBlueprintFromXML("assets/unittypes/robocop.xml");
		BlueprintManager.getInstance().readBlueprintFromXML("assets/objects/chair.xml");
		

		int chair = BlueprintManager.getInstance().createEntityFromBlueprint("chair");
		EntityManager.getInstance().getComponent(chair, PositionC.class).position.x = 2f;
		EntityManager.getInstance().getComponent(chair, PositionC.class).position.z = 2f;

		EntityManager.getInstance().getComponent(chair, VisualC.class).setScale(0.5f, 0.5f, 0.5f);

		PositionC tmp = EntityManager.getInstance().getComponent(BlueprintManager.getInstance().createEntityFromBlueprint("robocop"), PositionC.class);
		tmp.position.x = 1;
		tmp.position.z = 1;
		
		tmp = EntityManager.getInstance().getComponent(BlueprintManager.getInstance().createEntityFromBlueprint("robocop"), PositionC.class);
		tmp.position.x = 2;
		tmp.position.z = 1;
		
		tmp = EntityManager.getInstance().getComponent(BlueprintManager.getInstance().createEntityFromBlueprint("robocop"), PositionC.class);
		tmp.position.x = 1;
		tmp.position.z = 2;

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

		RenderManager.getInstance().render();
	}
}
