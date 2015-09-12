package de.mih.core.game;

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
import de.mih.core.game.player.Player;
import de.mih.core.game.player.input.contextmenu.CircularContextMenu;
import de.mih.core.game.player.input.InGameInput;
import de.mih.core.game.render.CircularContextMenuRenderer;
import de.mih.core.game.render.TilemapRenderer;
import de.mih.core.game.systems.ControllerSystem;
import de.mih.core.game.systems.MoveSystem;
import de.mih.core.game.systems.OrderSystem;
import de.mih.core.game.systems.PlayerSystem;
import de.mih.core.game.systems.RenderSystem;
import de.mih.core.game.tilemap.borders.DoorBorder;
import de.mih.core.game.tilemap.borders.RoomBorderColliderFactory;
import de.mih.core.game.tilemap.borders.WallBorder;

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
	MoveSystem ms;
	Pathfinder pf;
	TilemapReader tr;
	InGameInput inGameInput;
	public static InputMultiplexer input;
	CircularContextMenu contextMenu;
	Tilemap map;

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
	
		WallBorder.wallVisual = new Visual(assetManager.get("assets/models/wall.obj", Model.class));
		DoorBorder.doorVisual = new Visual(assetManager.get("assets/models/door.obj", Model.class));

		RenderManager.getInstance().setCamera(camera);
		
		contextMenu = CircularContextMenu.getInstance();
		inGameInput = new InGameInput(activePlayer, entityM, camera);	
		input.addProcessor(contextMenu);
		input.addProcessor(inGameInput);
		input.addProcessor(cs);

		Gdx.input.setInputProcessor(input);
		tilemapReader = new TilemapReader(rs, entityM, RoomBorderColliderFactory.getInstance());

		pf = new Pathfinder();

		map = tilemapReader.readMap("assets/maps/map1.xml");

		tilemapRenderer = new TilemapRenderer(map);
		new CircularContextMenuRenderer();

		// "assets/unittypes/" + unittype + ".xml"
		BlueprintManager.getInstance().registerComponentType(ColliderC.name, ColliderC.class);
		BlueprintManager.getInstance().registerComponentType(Control.name, Control.class);
		BlueprintManager.getInstance().registerComponentType(PositionC.name, PositionC.class);
		BlueprintManager.getInstance().registerComponentType(SelectableC.name, SelectableC.class);
		BlueprintManager.getInstance().registerComponentType(VelocityC.name, VelocityC.class);
		BlueprintManager.getInstance().registerComponentType(VisualC.name, VisualC.class);
		BlueprintManager.getInstance().registerComponentType(InteractableC.name, InteractableC.class);

		BlueprintManager.getInstance().readBlueprintFromXML("assets/unittypes/robocop.xml");
		BlueprintManager.getInstance().readBlueprintFromXML("assets/objects/chair.xml");
		// utp.newUnit("robocop");

		int chair = BlueprintManager.getInstance().createEntityFromBlueprint("chair");
		EntityManager.getInstance().getComponent(chair, PositionC.class).position.x = 2f;
		EntityManager.getInstance().getComponent(chair, PositionC.class).position.z = 2f;

		EntityManager.getInstance().getComponent(chair, VisualC.class).setScale(0.5f, 0.5f, 0.5f);

		EntityManager.getInstance().getComponent(BlueprintManager.getInstance().createEntityFromBlueprint("robocop"),
				PositionC.class).position.x = 1f;

		EntityManager.getInstance().getComponent(BlueprintManager.getInstance().createEntityFromBlueprint("robocop"),
				PositionC.class).position.z = -1f;

		EntityManager.getInstance().getComponent(BlueprintManager.getInstance().createEntityFromBlueprint("robocop"),
				PositionC.class).position.x = -1f;

	}

	Map<Tile, Integer> pathToEntity = new HashMap<>();

	public void render() {

		systemM.update(Gdx.graphics.getDeltaTime());

		RenderManager.getInstance().render();
	}
}
