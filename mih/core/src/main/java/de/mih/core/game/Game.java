package de.mih.core.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import de.mih.core.engine.ai.Pathfinder;
import de.mih.core.engine.ecs.BlueprintManager;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.ecs.EventManager;
import de.mih.core.engine.ecs.RenderManager;
import de.mih.core.engine.ecs.SystemManager;
import de.mih.core.engine.io.AdvancedAssetManager;
import de.mih.core.engine.io.TilemapParser;
import de.mih.core.engine.tilemap.Tilemap;
import de.mih.core.game.components.ColliderC;
import de.mih.core.game.components.Control;
import de.mih.core.game.components.InteractableC;
import de.mih.core.game.components.InventoryC;
import de.mih.core.game.components.OrderableC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.SelectableC;
import de.mih.core.game.components.StatsC;
import de.mih.core.game.components.VelocityC;
import de.mih.core.game.components.VisualC;
import de.mih.core.game.input.InGameInput;
import de.mih.core.game.input.contextmenu.CircularContextMenu;
import de.mih.core.game.player.Interaction;
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
	public CircularContextMenu contextMenu;
	public CircularContextMenuRenderer contextmenuR;
	public InGameInput ingameinput;
	
	public AdvancedAssetManager assetManager;
	public BitmapFont font;
	
	public Pathfinder pathfinder;
	
	public PerspectiveCamera camera;
	
	public Player activePlayer;
	int cam_target = -1;
	
	public Game(String path){
		
		//TODO: DELETE
		Interaction.game = this;
		//
		
		
		tilemapP = new TilemapParser();
		pathfinder = new Pathfinder();
		
		activePlayer = new Player("localplayer", 0, EntityManager.getInstance());

		assetManager = AdvancedAssetManager.getInstance();
		assetManager.assetManager.load("assets/textures/contextmenu_bg.png", Texture.class);
		assetManager.assetManager.load("assets/icons/sit.png", Texture.class);
		assetManager.assetManager.load("assets/icons/goto.png", Texture.class);
//		assetManager.assetManager.load("assets/models/wall.obj", Model.class);
//		assetManager.assetManager.load("assets/models/door.obj", Model.class);
//		assetManager.assetManager.load("assets/models/selectioncircle.obj", Model.class);
//		assetManager.assetManager.load("assets/models/chair.obj", Model.class);

		camera = new PerspectiveCamera(75, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(2f, 5f, 3f);
		camera.lookAt(0f, 0f, 0f);
		camera.near = 0.1f;
		camera.far = 300f;

		assetManager.assetManager.finishLoading();
		this.font = new BitmapFont(); 
		
		
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

		BlueprintManager.getInstance().readBlueprintFromXML("assets/unittypes/robocop.xml");
		BlueprintManager.getInstance().readBlueprintFromXML("assets/objects/chair.xml");
		BlueprintManager.getInstance().readBlueprintFromXML("assets/unittypes/wall.xml");
		BlueprintManager.getInstance().readBlueprintFromXML("assets/unittypes/door.xml");

		RenderManager.getInstance().setCamera(camera);

		tilemap = tilemapP.readMap(path);

		// TODO: DELETE
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
		//
		
		
		inputMulti = new InputMultiplexer();
		
		contextMenu = new CircularContextMenu();
		inputMulti.addProcessor(contextMenu);
		
		ingameinput = new InGameInput(this);
		inputMulti.addProcessor(ingameinput);

		Gdx.input.setInputProcessor(inputMulti);
		
		
		tilemapR = new TilemapRenderer(tilemap);
		contextmenuR = new CircularContextMenuRenderer(this.contextMenu);
		
		
		moveS = new MoveSystem(this);
		orderS = new OrderSystem(this);
		renderS = new RenderSystem(this);
		controllS = new ControllerSystem(this);
		playerS = new PlayerSystem(this);
		
	}
}
