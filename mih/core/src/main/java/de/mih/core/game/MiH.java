package de.mih.core.game;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import de.mih.core.engine.ai.Pathfinder;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.ecs.SystemManager;
import de.mih.core.engine.ecs.events.orderevents.OrderToPoint_Event;
import de.mih.core.engine.io.TilemapReader;
import de.mih.core.engine.io.UnitTypeParser;
import de.mih.core.game.components.ColliderC;
import de.mih.core.game.components.Control;
import de.mih.core.game.components.NodeC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.TilemapC;
import de.mih.core.game.components.VelocityC;
import de.mih.core.game.components.Visual;
import de.mih.core.game.input.CircularContextMenu;
import de.mih.core.game.input.ClickListener;
import de.mih.core.game.input.InGameInput;
import de.mih.core.game.player.Player;
import de.mih.core.game.systems.ControllerSystem;
import de.mih.core.game.systems.MoveSystem;
import de.mih.core.game.systems.OrderSystem;
import de.mih.core.game.systems.PlayerSystem;
import de.mih.core.game.systems.RenderSystem;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;


public class MiH extends ApplicationAdapter {

	static EntityManager entityM;
	static SystemManager systemM;
	static RenderSystem rs;
	static ControllerSystem cs;
	static MoveSystem ms;
	static UnitTypeParser utp;
	static Pathfinder pf;
	static TilemapReader tr;
	static InputMultiplexer input;
	static int map;
	static InGameInput inGameInput;
	static CircularContextMenu contextMenu;
	
	static AssetManager assetManager;
	
	public static Player activePlayer;
	int cam_target = -1;

	Map<Integer, Integer> path;
	
	public void create() {
		entityM = new EntityManager();
		systemM = new SystemManager(entityM, 5);
		
		activePlayer = new Player("localplayer",0,entityM);
		

		
		assetManager = new AssetManager();
		//Gdx.files.internal("assets/textures/contextmenu_bg.png");
		assetManager.load("assets/textures/contextmenu_bg.png", Texture.class);

		assetManager.finishLoading();
		rs = new RenderSystem(systemM, entityM,
				new PerspectiveCamera(75, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
		input = new InputMultiplexer();
		
		cs = new ControllerSystem(systemM, entityM, rs, Gdx.input);
		
		new PlayerSystem(systemM, entityM,rs);
		
		while(!assetManager.isLoaded("assets/textures/contextmenu_bg.png"))
		{
			System.out.println("Loading textures!");
		}
		contextMenu = new CircularContextMenu(50, assetManager.get("assets/textures/contextmenu_bg.png",Texture.class));
		
		//contextMenu.getButton(0).addClickListener(() -> System.out.println("Button 1 pressed!"));
		contextMenu.getButton(0).addClickListener(() -> {OrderToPoint_Event.fire(activePlayer.selectedunits.get(0), rs.getMouseTarget(0f, Gdx.input), null);});
		contextMenu.getButton(1).addClickListener(() -> System.out.println("Button 2 pressed!"));
		contextMenu.getButton(2).addClickListener(() -> System.out.println("Button 3 pressed!"));
		contextMenu.getButton(3).addClickListener(() -> System.out.println("Button 4 pressed!"));
		contextMenu.getButton(4).addClickListener(() -> System.out.println("Button 5 pressed!"));
		contextMenu.getButton(5).addClickListener(() -> System.out.println("Button 6 pressed!"));

		inGameInput = new InGameInput(activePlayer,contextMenu,entityM,rs.camera);
		input.addProcessor(contextMenu);
		input.addProcessor(inGameInput);
		input.addProcessor(cs);

		Gdx.input.setInputProcessor(input);
		tr = new TilemapReader(rs, entityM);
		utp = new UnitTypeParser(rs, entityM);
		pf = new Pathfinder(entityM);

		
		
		map = tr.readMap("assets/maps/map1.xml");
		
		new OrderSystem(systemM, entityM,pf,entityM.getComponent(map, TilemapC.class));
		
		ms = new MoveSystem(systemM, entityM, entityM.getComponent(map, TilemapC.class));
	
		// Robocop!!!111elf
		utp.newUnit("robocop");
		
	
		entityM.getComponent(utp.newUnit("robobot"), PositionC.class).position.x = 1f;
		
		entityM.getComponent(utp.newUnit("robobot"), PositionC.class).position.z = -1f;
		
		entityM.getComponent(utp.newUnit("robobot"), PositionC.class).position.x = -1f;
		//
		
		this.spriteBatch = new SpriteBatch();
	}

	SpriteBatch spriteBatch;

	public void render() {

		systemM.update(Gdx.graphics.getDeltaTime());
		systemM.render(Gdx.graphics.getDeltaTime());
		
	//	this.contextMenu.update();
		this.spriteBatch.begin();
		this.contextMenu.render(spriteBatch);
		this.spriteBatch.end();
	}
}
