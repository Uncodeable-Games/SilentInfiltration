package de.mih.core.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;

import de.mih.core.engine.ai.navigation.Pathfinder;
import de.mih.core.engine.ecs.BlueprintManager;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.ecs.RenderManager;
import de.mih.core.engine.io.AdvancedAssetManager;
import de.mih.core.engine.io.TilemapParser;
import de.mih.core.engine.tilemap.Room;
import de.mih.core.engine.tilemap.Tilemap;
import de.mih.core.game.components.AttachmentC;
import de.mih.core.game.components.BorderC;
import de.mih.core.game.components.ColliderC;
import de.mih.core.game.components.Control;
import de.mih.core.game.components.InteractableC;
import de.mih.core.game.components.InventoryC;
import de.mih.core.game.components.OrderableC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.SelectableC;
import de.mih.core.game.components.StatsC;
import de.mih.core.game.components.UnittypeC;
import de.mih.core.game.components.VelocityC;
import de.mih.core.game.components.VisualC;
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

public class Game {
	public ControllerSystem controllS;
	public MoveSystem moveS;
	public OrderSystem orderS;
	public PlayerSystem playerS;
	public RenderSystem renderS;
	
	public TilemapParser tilemapP;
	public Tilemap tilemap;
	public TilemapRenderer tilemapR;
	
	public InputMultiplexer inputMulti;
	public UserInterface ui;
	public CircularContextMenu contextMenu;
	public CircularContextMenuRenderer contextmenuR;
	public InGameInput ingameinput;
	
	public AdvancedAssetManager assetManager;
	
	public Pathfinder pathfinder;
	
	public PerspectiveCamera camera;
	
	public Player activePlayer;
	int cam_target = -1;
	
	public Game(){
		
	}
	
	public void init(String path){
		
		// Stuff
		tilemapP = new TilemapParser();
		pathfinder = new Pathfinder();
		activePlayer = new Player("localplayer", 0, EntityManager.getInstance());

		// AssetManager
		assetManager = AdvancedAssetManager.getInstance();
		assetManager.assetManager.load("assets/textures/contextmenu_bg.png", Texture.class);
		assetManager.assetManager.load("assets/icons/sit.png", Texture.class);
		assetManager.assetManager.load("assets/icons/goto.png", Texture.class);
		assetManager.assetManager.load("assets/ui/buttons/testbutton.png", Texture.class);
		assetManager.assetManager.load("assets/ui/backgrounds/b_bottom_right.png", Texture.class);
		assetManager.assetManager.load("assets/ui/backgrounds/b_bottom_left.png", Texture.class);
		assetManager.assetManager.finishLoading();

		// RenderManager
		camera = new PerspectiveCamera(75, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(2f, 5f, 3f);
		camera.lookAt(0f, 0f, 0f);
		camera.near = 0.1f;
		camera.far = 300f;
		RenderManager.getInstance().setCamera(camera);
		
		// Components
		BlueprintManager.getInstance().registerComponentType(ColliderC.name, ColliderC.class);
		BlueprintManager.getInstance().registerComponentType(Control.name, Control.class);
		BlueprintManager.getInstance().registerComponentType(PositionC.name, PositionC.class);
		BlueprintManager.getInstance().registerComponentType(SelectableC.name, SelectableC.class);
		BlueprintManager.getInstance().registerComponentType(VelocityC.name, VelocityC.class);
		BlueprintManager.getInstance().registerComponentType(VisualC.name, VisualC.class);
		BlueprintManager.getInstance().registerComponentType(OrderableC.name,OrderableC.class);
		BlueprintManager.getInstance().registerComponentType(InteractableC.name, InteractableC.class);
		BlueprintManager.getInstance().registerComponentType(StatsC.name, StatsC.class);
		BlueprintManager.getInstance().registerComponentType(InventoryC.name, InventoryC.class);
		BlueprintManager.getInstance().registerComponentType(BorderC.name, BorderC.class);
		BlueprintManager.getInstance().registerComponentType(UnittypeC.name, UnittypeC.class);
		BlueprintManager.getInstance().registerComponentType(AttachmentC.name, AttachmentC.class);

		
		// Blueprints
		BlueprintManager.getInstance().readBlueprintFromXML("assets/unittypes/robocop.xml");
		BlueprintManager.getInstance().readBlueprintFromXML("assets/objects/chair.xml");
		BlueprintManager.getInstance().readBlueprintFromXML("assets/unittypes/wall.xml");
		BlueprintManager.getInstance().readBlueprintFromXML("assets/unittypes/door.xml");

		// Tilemap
		tilemap = tilemapP.readMap(path);
		

		// TODO: DELETE
		int chair = BlueprintManager.getInstance().createEntityFromBlueprint("chair");
		EntityManager.getInstance().getComponent(chair, PositionC.class).setPos(2f, 0, 3f);
		
		chair = BlueprintManager.getInstance().createEntityFromBlueprint("chair");
		EntityManager.getInstance().getComponent(chair, PositionC.class).setPos(3f, 0, 7f);
		
		chair = BlueprintManager.getInstance().createEntityFromBlueprint("chair");
		EntityManager.getInstance().getComponent(chair, PositionC.class).setPos(6f, 0, 6f);
	
		int robo = BlueprintManager.getInstance().createEntityFromBlueprint("robocop");
		EntityManager.getInstance().getComponent(robo, PositionC.class).setPos(1,0,1);
		//
		
		// Input
		inputMulti = new InputMultiplexer();
		ui = new UserInterface();
		inputMulti.addProcessor(ui);
		contextMenu = new CircularContextMenu();
		inputMulti.addProcessor(contextMenu);	
		ingameinput = new InGameInput(this);
		inputMulti.addProcessor(ingameinput);
		Gdx.input.setInputProcessor(inputMulti);
		
		// Renderer
		tilemapR = new TilemapRenderer();
		contextmenuR = new CircularContextMenuRenderer(this.contextMenu);
		
		// Systems
		moveS = new MoveSystem(this);
		orderS = new OrderSystem(this);
		renderS = new RenderSystem(this);
		controllS = new ControllerSystem(this);
		playerS = new PlayerSystem(this);
		
		tilemap.calculateRooms();
	}
}
