package de.mih.core.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;

import de.mih.core.engine.ai.navigation.Pathfinder;
import de.mih.core.engine.ecs.BlueprintManager;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.ecs.EventManager;
import de.mih.core.engine.ecs.SystemManager;
import de.mih.core.engine.io.AdvancedAssetManager;
import de.mih.core.engine.io.TilemapParser;
import de.mih.core.engine.render.RenderManager;
import de.mih.core.engine.tilemap.Room;
import de.mih.core.engine.tilemap.Tilemap;
import de.mih.core.game.components.*;
import de.mih.core.game.components.info.*;
import de.mih.core.game.input.InGameInput;
import de.mih.core.game.input.contextmenu.CircularContextMenu;
import de.mih.core.game.input.ui.UserInterface;
import de.mih.core.game.player.Player;
import de.mih.core.game.render.CircularContextMenuRenderer;
import de.mih.core.game.render.TilemapRenderer;
import de.mih.core.game.systems.ControllerSystem;
import de.mih.core.game.systems.MoveSystem;
import de.mih.core.game.systems.OrderSystem;
import de.mih.core.game.systems.PlayerSystem;
import de.mih.core.game.systems.RenderSystem;

public class Game
{
	EntityManager entityManager;
	EventManager eventManager;
	BlueprintManager blueprintManager;
	RenderManager renderManager;
	SystemManager systemManager;
	AdvancedAssetManager assetManager;

	ControllerSystem controllS;
	MoveSystem moveS;
	OrderSystem orderS;
	PlayerSystem playerS;
	RenderSystem renderS;

	TilemapParser tilemapParser;
	Tilemap tilemap;
	TilemapRenderer tilemapRenderer;

	InputMultiplexer inputMultiplexer;
	UserInterface ui;
	CircularContextMenu contextMenu;
	CircularContextMenuRenderer contextmenuR;
	InGameInput ingameinput;


	Pathfinder pathfinder;

	PerspectiveCamera camera;

	Player activePlayer;
	int cam_target = -1;

	static Game currentGame;

	public Game()
	{
		currentGame = this;
	}

	
	void registerComponents()
	{
		this.blueprintManager.registerComponentInfoType("collider", ColliderComponentInfo.class);
		this.blueprintManager.registerComponentInfoType("control", ControlComponentInfo.class);
		this.blueprintManager.registerComponentInfoType("position", PositionComponentInfo.class);
		this.blueprintManager.registerComponentInfoType("selectable", SelectabelComponentInfo.class);
		this.blueprintManager.registerComponentInfoType("velocity", VelocityComponentInfo.class);
		this.blueprintManager.registerComponentInfoType("visual", VisualComponentInfo.class);
		this.blueprintManager.registerComponentInfoType("orderable", OrderableComponentInfo.class);
		this.blueprintManager.registerComponentInfoType("interactable", InteractableComponentInfo.class);
		this.blueprintManager.registerComponentInfoType("stats", StatsComponentInfo.class);
		this.blueprintManager.registerComponentInfoType("inventory", InventoryComponentInfo.class);
		this.blueprintManager.registerComponentInfoType("border", BorderComponentInfo.class);
		this.blueprintManager.registerComponentInfoType("unittype", UnittypeComponentInfo.class);
		this.blueprintManager.registerComponentInfoType("attachment", AttachmentComponentInfo.class);
	}
	public void init(String path)
	{

		// Manager setup
		this.entityManager = new EntityManager();
		this.blueprintManager = new BlueprintManager(this.entityManager);
		this.renderManager = new RenderManager(this.entityManager);
		this.systemManager = new SystemManager(renderManager, entityManager, 30);
		this.eventManager = new EventManager();
		
		this.registerComponents();

		// AssetManager
		assetManager = new AdvancedAssetManager(renderManager);
		this.loadResources();

		// RenderManager
		camera = new PerspectiveCamera(75, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(2f, 5f, 3f);
		camera.lookAt(0f, 0f, 0f);
		camera.near = 0.1f;
		camera.far = 300f;
		this.renderManager.setCamera(camera);

		// Stuff // Tilemap
		tilemapParser = new TilemapParser(this.blueprintManager, this.entityManager);

		tilemap = tilemapParser.readMap(path);

		pathfinder = new Pathfinder(this.tilemap);
		activePlayer = new Player("localplayer", 0, this.entityManager);

		// TODO: DELETE
		int chair = this.blueprintManager.createEntityFromBlueprint("chair");
		this.entityManager.getComponent(chair, PositionC.class).setPos(2f, 0, 3f);

		chair = this.blueprintManager.createEntityFromBlueprint("chair");
		this.entityManager.getComponent(chair, PositionC.class).setPos(3f, 0, 7f);

		chair = this.blueprintManager.createEntityFromBlueprint("chair");
		this.entityManager.getComponent(chair, PositionC.class).setPos(6f, 0, 6f);

		int robo = this.blueprintManager.createEntityFromBlueprint("robocop");
		this.entityManager.getComponent(robo, PositionC.class).setPos(1, 0, 1);
		//

		// Input
		inputMultiplexer = new InputMultiplexer();
		ui = new UserInterface(renderManager, assetManager);
		inputMultiplexer.addProcessor(ui);
		contextMenu = new CircularContextMenu();
		inputMultiplexer.addProcessor(contextMenu);
		ingameinput = new InGameInput(this);
		inputMultiplexer.addProcessor(ingameinput);
		Gdx.input.setInputProcessor(inputMultiplexer);

		// Renderer
		tilemapRenderer = new TilemapRenderer(this.tilemap, this.renderManager);
		contextmenuR = new CircularContextMenuRenderer(this.renderManager, this.contextMenu);

		// Systems
		moveS = new MoveSystem(this.systemManager, this);
		orderS = new OrderSystem(this.systemManager, this);
		renderS = new RenderSystem(this.systemManager, this);
		controllS = new ControllerSystem(this.systemManager, this);
		playerS = new PlayerSystem(this.systemManager, this);

		tilemap.calculateRooms();
	}
	
	
	void loadResources()
	{
		assetManager.assetManager.load("assets/textures/contextmenu_bg.png", Texture.class);
		assetManager.assetManager.load("assets/icons/sit.png", Texture.class);
		assetManager.assetManager.load("assets/icons/goto.png", Texture.class);
		assetManager.assetManager.load("assets/ui/buttons/testbutton.png", Texture.class);
		assetManager.assetManager.load("assets/ui/backgrounds/b_bottom_right.png", Texture.class);
		assetManager.assetManager.load("assets/ui/backgrounds/b_bottom_left.png", Texture.class);
		assetManager.assetManager.finishLoading();

		//blueprints
		this.blueprintManager.readBlueprintFromXML("assets/unittypes/robocop.xml");
		System.out.println("chair: " + this.blueprintManager.readBlueprintFromXML("assets/objects/chair.xml"));
		this.blueprintManager.readBlueprintFromXML("assets/unittypes/wall.xml");
		this.blueprintManager.readBlueprintFromXML("assets/unittypes/door.xml");
	}

	public EntityManager getEntityManager()
	{
		return entityManager;
	}

	public EventManager getEventManager()
	{
		return eventManager;
	}

	public BlueprintManager getBlueprintManager()
	{
		return blueprintManager;
	}

	public RenderManager getRenderManager()
	{
		return renderManager;
	}

	public SystemManager getSystemManager()
	{
		return systemManager;
	}

	public Pathfinder getPathfinder()
	{
		return pathfinder;
	}

	/**
	 * Only for refactoring reasons!
	 * @return
	 */
	@Deprecated
	public static Game getCurrentGame()
	{
		return currentGame;
	}

	public AdvancedAssetManager getAssetManager()
	{
		return assetManager;
	}


	public PerspectiveCamera getCamera()
	{
		return camera;
	}


	public Player getActivePlayer()
	{
		return activePlayer;
	}


	public CircularContextMenu getContextMenu()
	{
		return contextMenu;
	}


	public TilemapParser getTilemapParser()
	{
		return tilemapParser;
	}


	public Tilemap getTilemap()
	{
		return tilemap;
	}


	public RenderSystem getRenderSystem()
	{
		return renderS;
	}


	public UserInterface getUI()
	{
		return ui;
	}
}
