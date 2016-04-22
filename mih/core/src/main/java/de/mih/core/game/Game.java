package de.mih.core.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import de.mih.core.engine.ability.AbilityManager;
import de.mih.core.engine.ai.navigation.NavigationManager;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.ecs.EventManager;
import de.mih.core.engine.ecs.SystemManager;
import de.mih.core.engine.io.AdvancedAssetManager;
import de.mih.core.engine.io.BlueprintManager;
import de.mih.core.engine.lua.LuaScriptManager;
import de.mih.core.engine.render.RenderManager;
import de.mih.core.engine.tilemap.Tilemap;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.input.InGameInput;
import de.mih.core.game.input.ui.UserInterface;
import de.mih.core.game.player.Player;
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
	private AbilityManager       abilityManager;
	private LuaScriptManager     luaScriptManager;

	private ControllerSystem controllS;
	private MoveSystem       moveS;
	private OrderSystem      orderS;
	private PlayerSystem     playerS;
	private RenderSystem     renderS;
	private StatsSystem      statsSystem;

	private StateMachineSystem stateMachineS;

	private Tilemap         tilemap;
	private TilemapRenderer tilemapRenderer;

	private InputMultiplexer inputMultiplexer;
	private UserInterface    ui;
	private InGameInput      ingameinput;

	private PerspectiveCamera camera;

	private Player activePlayer;

	private static Game currentGame;

	private boolean editMode;
	public  boolean isGameOver;

	private BitmapFont font = new BitmapFont();

	public static Game getCurrentGame()
	{
		return currentGame;
	}

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
		this.luaScriptManager = new LuaScriptManager();
		this.navigationManager = new NavigationManager();
		this.assetManager = new AdvancedAssetManager(renderManager);

		this.loadResources();

		this.ui = new UserInterface();

		// RenderManager
		camera = new PerspectiveCamera(75, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(8f, 10f, 56f);
		camera.lookAt(8f, 0f, 53f);
		camera.near = 0.1f;
		camera.far = 300f;
		this.renderManager.setCamera(camera);

		// Stuff // Tilemap
		tilemap = this.blueprintManager.readTilemapBlueprint("assets/maps/map1.json");

		activePlayer = new Player("localplayer", 0, Player.PlayerType.Attacker);

		int robo = this.blueprintManager.createEntityFromBlueprint("robocop.json");
		this.entityManager.getComponent(robo, PositionC.class).setPos(8, 0, 53);

		this.activePlayer.setHero(robo);

		// Input
		inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(ui);
		ingameinput = new InGameInput(this);
		inputMultiplexer.addProcessor(ingameinput);
		Gdx.input.setInputProcessor(inputMultiplexer);

		// Renderer
		tilemapRenderer = new TilemapRenderer(this.tilemap, this.renderManager);

		// Systems
		moveS = new MoveSystem(this.systemManager, this);
		orderS = new OrderSystem(this.systemManager, this);
		renderS = new RenderSystem(this.systemManager, this);
		controllS = new ControllerSystem(this.systemManager, this);
		playerS = new PlayerSystem(this.systemManager, this);
		stateMachineS = new StateMachineSystem(systemManager, this);
		statsSystem = new StatsSystem();

		tilemap.calculateRooms();
		tilemap.calculatePhysicBody();

		navigationManager.calculateNavigation();
	}

	public void update()
	{
		this.getSystemManager().update(Gdx.graphics.getDeltaTime());
	}

	void loadResources()
	{
		this.assetManager.loadTextures("assets/icons");
		this.assetManager.loadTextures("assets/textures");
		this.blueprintManager.readEntityBlueprint("assets/data/unittypes");
		this.abilityManager.registerAbilities("assets/data/abilities");

		this.assetManager.assetManager.finishLoading();
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

	public LuaScriptManager getLuaScriptManager()
	{
		return luaScriptManager;
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

	public void render()
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		this.getRenderManager().render();
		this.getRenderManager().spriteBatch.begin();

		if (this.isEditMode())
		{
			font.draw(this.getRenderManager().spriteBatch, "EDIT MODE - (F10) to calculate Navigation (F11) to save (F12) to close", 10,
					Gdx.graphics.getHeight()-10);
			font.draw(this.getRenderManager().spriteBatch, "(w) place/remove wall", 10, Gdx.graphics.getHeight()-26);
			font.draw(this.getRenderManager().spriteBatch, "(s) place/remove halfwall", 10, Gdx.graphics.getHeight()-42);
			font.draw(this.getRenderManager().spriteBatch, "(d) place/remove door", 10, Gdx.graphics.getHeight()-58);
			font.draw(this.getRenderManager().spriteBatch, "(a) place/remove halfdoor", 10, Gdx.graphics.getHeight()-74);
		}

		this.getRenderManager().spriteBatch.end();
	}
}
