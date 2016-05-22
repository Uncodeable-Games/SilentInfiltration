package de.mih.core.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.PerspectiveCamera;

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
import de.mih.core.game.systems.AbilitySystem;
import de.mih.core.game.systems.ControllerSystem;
import de.mih.core.game.systems.MoveSystem;
import de.mih.core.game.systems.OrderSystem;
import de.mih.core.game.systems.PlayerSystem;
import de.mih.core.game.systems.RenderSystem;
import de.mih.core.game.systems.StateMachineSystem;
import de.mih.core.game.systems.StatsSystem;

public class GameLogic
{

	protected EntityManager entityManager;
	protected EventManager eventManager;
	protected BlueprintManager blueprintManager;
	protected SystemManager systemManager;
	protected AdvancedAssetManager assetManager;
	protected NavigationManager navigationManager;
	protected AbilityManager abilityManager;
	protected LuaScriptManager luaScriptManager;
	
	protected ControllerSystem controllS;
	protected MoveSystem moveS;
	protected OrderSystem orderS;
	protected PlayerSystem playerS;
	protected StatsSystem statsSystem;
	protected StateMachineSystem stateMachineS;
	protected AbilitySystem abilitySystem;
	
	protected Tilemap tilemap;
	public boolean isGameOver;
	
	public static GameLogic currentGame;

	public static GameLogic getCurrentGame()
	{
		return currentGame;
	}

	public GameLogic()
	{
		super();
		currentGame = this;
	}

	public void update(double deltaTime)
	{
		this.getSystemManager().update(deltaTime);
	}
	

	protected void loadResources()
	{
		this.blueprintManager.readEntityBlueprint("assets/data/unittypes");
		this.abilityManager.registerAbilities("assets/data/abilities");
	
		this.assetManager.assetManager.finishLoading();
	}
	
	public void init(String path)
	{
		this.init(path, true);
	}
	
	public void init(String path, boolean noGraphics)
	{
		// Manager setup
		this.entityManager = new EntityManager();
		this.blueprintManager = new BlueprintManager(this.entityManager, noGraphics);
		this.eventManager = new EventManager();
		this.systemManager = new SystemManager(this.eventManager, this.entityManager);
		this.abilityManager = new AbilityManager();
		this.luaScriptManager = new LuaScriptManager();
		this.navigationManager = new NavigationManager();
		
		//TODO: fix
		this.assetManager = new AdvancedAssetManager();

		this.loadResources();

		this.blueprintManager.readEntityBlueprint("assets/data/unittypes");
		this.abilityManager.registerAbilities("assets/data/abilities");

		// Stuff // Tilemap
		tilemap = this.blueprintManager.readTilemapBlueprint("assets/maps/map1.json");

		
		// Systems
		moveS = new MoveSystem(this.systemManager, this);
		orderS = new OrderSystem(this.systemManager, this);
		controllS = new ControllerSystem(this.systemManager, this);
		playerS = new PlayerSystem(this.systemManager, this);
		stateMachineS = new StateMachineSystem(this.systemManager, this);
		statsSystem = new StatsSystem(this.systemManager,this);
		abilitySystem = new AbilitySystem(this.systemManager, this);

		tilemap.calculateRooms();
		tilemap.calculatePhysicBody();

		navigationManager.calculateNavigation();
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

	public Tilemap getTilemap()
	{
		return tilemap;
	}
}