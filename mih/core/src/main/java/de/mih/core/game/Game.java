package de.mih.core.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import de.mih.core.engine.ai.Pathfinder;
import de.mih.core.engine.ecs.BlueprintManager;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.ecs.EventManager;
import de.mih.core.engine.ecs.SystemManager;
import de.mih.core.engine.ecs.events.BaseEvent;
import de.mih.core.engine.ecs.events.BaseEvent.GlobalEvent;
import de.mih.core.engine.ecs.events.EventListener;
import de.mih.core.engine.io.AdvancedAssetManager;
import de.mih.core.engine.io.TilemapParser;
import de.mih.core.engine.physic.Geometry;
import de.mih.core.engine.physic.Line;
import de.mih.core.engine.render.RenderManager;
import de.mih.core.engine.tilemap.Tilemap;
import de.mih.core.game.ai.guard.Observing;
import de.mih.core.game.ai.guard.Patrol;
import de.mih.core.game.ai.guard.Watching;
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
import de.mih.core.game.systems.StateMachineSystem;

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
	StateMachineSystem stateMachineS;

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
	
	boolean editMode;
	public int robo;

	public Game()
	{
		currentGame = this;
		editMode = false;
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
		camera.position.set(20f, 10f, 8f);
		camera.lookAt(20f, 0f, 5f);
		camera.near = 0.1f;
		camera.far = 300f;
		this.renderManager.setCamera(camera);

		// Stuff // Tilemap
		tilemapParser = new TilemapParser(this.blueprintManager, this.entityManager);

		tilemap = tilemapParser.readMap(path);

		pathfinder = new Pathfinder(this);
		activePlayer = new Player("localplayer", 0, this.entityManager);

		// TODO: DELETE
//		int chair = this.blueprintManager.createEntityFromBlueprint("chair");
//		this.entityManager.getComponent(chair, PositionC.class).setPos(2f, 0, 3f);
//
//		chair = this.blueprintManager.createEntityFromBlueprint("chair");
//		this.entityManager.getComponent(chair, PositionC.class).setPos(3f, 0, 7f);
//
//		chair = this.blueprintManager.createEntityFromBlueprint("chair");
//		this.entityManager.getComponent(chair, PositionC.class).setPos(6f, 0, 6f);

		robo = this.blueprintManager.createEntityFromBlueprint("robocop");
		this.entityManager.getComponent(robo, PositionC.class).setPos(20, 0, 1);
		//

		// Input
		inputMultiplexer = new InputMultiplexer();
		//ui = new UserInterface(renderManager, assetManager);
		//inputMultiplexer.addProcessor(ui);
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
		
		//Game gym stuff
		setUPDemo();
	}
	
	public int guard;
	public int guard2;
	public Line sight;
	
	//TODO: more guards, maybe a second level
	public void setUPDemo()
	{
		this.entityManager.getComponent(robo, Control.class).withwasd = true;
		
		List<Integer> waypoints = new ArrayList<>();
		
		int entity = this.entityManager.createEntity();
		guard = entity;
		StateMachineComponent guard = new StateMachineComponent();
		StateMachineComponent patrol = new StateMachineComponent();
		Observing obState = new Observing(guard, patrol, this);
		
		sight = obState.sight;
		obState.setTarget(robo);
		guard.addState("OBSERVE", obState);
		
		Patrol patrolState = new Patrol(patrol, this);
		patrol.addState("PATROL", patrolState);
		
		patrol.current = patrol.states.get("PATROL");
		guard.current = guard.states.get("OBSERVE");
		
		
		this.entityManager.addComponent(this.guard, new AttachmentC(this.guard));
		this.entityManager.getComponent(this.guard, AttachmentC.class).addAttachment(1, assetManager.getModelByName("cone"));

		
		this.entityManager.addComponent(entity, guard,  new PositionC(new Vector3(14, 0, 15)), new VelocityC(), new VisualC("robocop"), new OrderableC());
		//this.entityManager.getComponent(robo, VelocityC.class).maxspeed = 4;
		patrol.entityID = guard.entityID;
		this.entityManager.getComponent(entity, VelocityC.class).maxspeed = 5;
		assert(this.entityManager.hasComponent(entity, OrderableC.class));
		System.out.println(entity);
		
		
		int wp1 = this.entityManager.createEntity();
		this.entityManager.addComponent(wp1, new PositionC(new Vector3(14, 0, 8)), new VisualC("redbox"));
		waypoints.add(wp1);
		int wp2 = this.entityManager.createEntity();
		this.entityManager.addComponent(wp2, new PositionC(new Vector3(14, 0, 20)), new VisualC("redbox"));
		waypoints.add(wp2);
		int wp3 = this.entityManager.createEntity();
		this.entityManager.addComponent(wp3, new PositionC(new Vector3(30, 0, 20)), new VisualC("redbox"));
		waypoints.add(wp3);
		int wp4 = this.entityManager.createEntity();
		this.entityManager.addComponent(wp4, new PositionC(new Vector3(30, 0, 8)), new VisualC("redbox"));
		waypoints.add(wp4);
		
		patrolState.setWaypoints(waypoints);

		
		patrol.current.onEnter();
		guard.current.onEnter();
		
		guard2 = this.entityManager.createEntity();
		this.entityManager.addComponent(this.guard2, new AttachmentC(this.guard2));
		this.entityManager.getComponent(this.guard2, AttachmentC.class).addAttachment(1, assetManager.getModelByName("cone"));

		StateMachineComponent smc = new StateMachineComponent();
		StateMachineComponent sub = new StateMachineComponent();
		//smc.entityID = guard2;
		sub.entityID = guard2;
		Observing observing2 = new Observing(smc, sub, this);
		observing2.setTarget(robo);
		smc.addState("OBSERVE", observing2);

		smc.current = smc.states.get("OBSERVE");

		Watching watching = new Watching(sub, this);
		watching.maxFacing = 90f;
		watching.minFacing = 0f;
		watching.rotateSpeed = 0.5f;
		
		sub.addState("WATCHING", watching);
		sub.current = sub.states.get("WATCHING");
		

		this.entityManager.addComponent(guard2, smc,  new PositionC(new Vector3(30, 0, 7)), new VelocityC(), new VisualC("robocop"), new OrderableC());
		
		sub.current.onEnter();
		smc.current.onEnter();
//		EventListener<GlobalEvent> onDetect = new EventListener<GlobalEvent>()
//		{
//
//
//			@Override
//			public void handleEvent(GlobalEvent event)
//			{
//				if(event.message.equals("PLAYER_DETECTED")) 
//				{
//					isGameOver = true;
//				}
//			}
//
//		};
		//eventManager.register(GlobalEvent.class, onDetect);
	}
	
	public void update()
	{
		PositionC t = entityManager.getComponent(robo, PositionC.class);
		Vector3 tmp = t.getPos().cpy();
		tmp.y = 10f;
		tmp.z += 2f;
		//this.camera.direction.set(t.facing);
		//this.camera.position.set(tmp);
	}
	
	public boolean isGameOver;

	
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
		//System.out.println("chair: " + this.blueprintManager.readBlueprintFromXML("assets/objects/chair.xml"));
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
	
	public void toggleEditMode() {
		this.editMode = !editMode;
	}
	
	public boolean isEditMode()
	{
		return editMode;
	}
	
}
