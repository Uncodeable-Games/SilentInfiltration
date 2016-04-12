package de.mih.core.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import de.mih.core.engine.ability.Ability;
import de.mih.core.engine.ability.AbilityManager;
import de.mih.core.engine.ability.Castable;
import de.mih.core.engine.ai.navigation.NavigationManager;
import de.mih.core.engine.ecs.BlueprintManager;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.ecs.EventManager;
import de.mih.core.engine.ecs.SystemManager;
import de.mih.core.engine.io.AdvancedAssetManager;
import de.mih.core.engine.io.TilemapParser;
import de.mih.core.engine.render.RenderManager;
import de.mih.core.engine.tilemap.Tilemap;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.input.InGameInput;
import de.mih.core.game.input.contextmenu.CircularContextMenu;
import de.mih.core.game.input.ui.UserInterface;
import de.mih.core.game.player.Player;
import de.mih.core.game.render.CircularContextMenuRenderer;
import de.mih.core.game.render.TilemapRenderer;
import de.mih.core.game.systems.*;

public class Game
{
	private EntityManager        entityManager;
	private EventManager         eventManager;
	private BlueprintManager     blueprintManager;
	private RenderManager        renderManager;
	private SystemManager        systemManager;
	private AdvancedAssetManager assetManager;
	private NavigationManager    navigationManager;
	private AbilityManager abilityManager;

	private ControllerSystem controllS;
	private MoveSystem       moveS;
	private OrderSystem      orderS;
	private PlayerSystem     playerS;
	private RenderSystem     renderS;

	private StateMachineSystem stateMachineS;

	private TilemapParser   tilemapParser;
	private Tilemap         tilemap;
	private TilemapRenderer tilemapRenderer;

	private InputMultiplexer            inputMultiplexer;
	private UserInterface               ui;
	private CircularContextMenu         contextMenu;
	private CircularContextMenuRenderer contextmenuR;
	private InGameInput                 ingameinput;

	private PerspectiveCamera camera;

	private Player activePlayer;
	int cam_target = -1;

	private static Game currentGame;

	private boolean editMode;
	public  int     robo;
	public  boolean isGameOver;

	public Game()
	{
		currentGame = this;
		editMode = false;
	}

	public void init(String path)
	{
		// Manager setup
		this.entityManager = new EntityManager();
		this.blueprintManager = new BlueprintManager(this.entityManager);
		this.renderManager = new RenderManager(this.entityManager);
		this.systemManager = new SystemManager(renderManager, entityManager, 30);
		this.eventManager = new EventManager();
		this.abilityManager = new AbilityManager();

		// AssetManager
		assetManager = new AdvancedAssetManager(renderManager);
		this.loadResources();

		// RenderManager
		camera = new PerspectiveCamera(75, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(20f, 10f, 8f);
		camera.lookAt(20f, 0f, 5f);
		camera.near = 0.1f;
		camera.far = 300f;
		this.renderManager.setCamera(camera);

		// Stuff // Tilemap
		tilemapParser = new TilemapParser(this.blueprintManager, this.entityManager);
		tilemap = tilemapParser.readMap(path);
		
		// TODO: TEMPMOVE!
		this.navigationManager = new NavigationManager();

		activePlayer = new Player("localplayer", 0, this.entityManager);

		robo = this.blueprintManager.createEntityFromBlueprint("robocop.json");
		this.entityManager.getComponent(robo, PositionC.class).setPos(20, 0, 2);

		// Input
		inputMultiplexer = new InputMultiplexer();
		// ui = new UserInterface(renderManager, assetManager);
		// inputMultiplexer.addProcessor(ui);
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
		stateMachineS = new StateMachineSystem(systemManager, this);

		tilemap.calculateRooms();
		tilemap.calculatePhysicBody();

		// Game gym stuff
		navigationManager.calculateNavigation();

		Ability a = new Ability(1, new Castable()
		{
			public void noTarget(int caster)
			{
				System.out.println("hallo "+caster);
			}
		});


	}

	public void update()
	{

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

		// blueprints

		this.blueprintManager.readBlueprintFromJson("assets/unittypes/robocop.json");
		this.blueprintManager.readBlueprintFromJson("assets/unittypes/door.json");
		this.blueprintManager.readBlueprintFromJson("assets/unittypes/wall.json");
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

	public NavigationManager getNavigationManager()
	{
		return navigationManager;
	}

	public AbilityManager getAbilityManager()
	{
		return abilityManager;
	}

	/**
	 * Only for refactoring reasons!
	 *
	 * @return
	 */
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

	public void toggleEditMode()
	{
		this.editMode = !editMode;
	}

	public boolean isEditMode()
	{
		return editMode;
	}
}
