package de.mih.core.game;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import de.mih.core.engine.ai.Pathfinder;
import de.mih.core.engine.ai.orders.MoveOrder;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.ecs.EventManager;
import de.mih.core.engine.ecs.SystemManager;
import de.mih.core.engine.ecs.events.orderevents.OrderToPoint_Event;
import de.mih.core.engine.io.TilemapReader;
import de.mih.core.engine.io.UnitTypeParser;
import de.mih.core.game.components.ColliderC;
import de.mih.core.game.components.Control;
import de.mih.core.game.components.NodeC;
import de.mih.core.game.components.OrderableC;
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

	EntityManager entityM = EntityManager.getInstance();
	SystemManager systemM = SystemManager.getInstance();
	EventManager eventM = EventManager.getInstance();
	RenderSystem rs;
	ControllerSystem cs;
	MoveSystem ms;
	UnitTypeParser utp;
	Pathfinder pf;
	TilemapReader tr;
	InputMultiplexer input;
	int map;
	InGameInput inGameInput;
	CircularContextMenu contextMenu;

	static AssetManager assetManager;

	public static Player activePlayer;
	int cam_target = -1;

	Map<Integer, Integer> path;

	public void create() {

		activePlayer = new Player("localplayer", 0, entityM);

		assetManager = new AssetManager();
		// Gdx.files.internal("assets/textures/contextmenu_bg.png");
		assetManager.load("assets/textures/contextmenu_bg.png", Texture.class);

		assetManager.finishLoading();
		rs = new RenderSystem(new PerspectiveCamera(75, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
		input = new InputMultiplexer();

		cs = new ControllerSystem(rs, Gdx.input);

		new PlayerSystem(rs);

		while (!assetManager.isLoaded("assets/textures/contextmenu_bg.png")) {
			System.out.println("Loading textures!");
		}
		contextMenu = new CircularContextMenu(50,
				assetManager.get("assets/textures/contextmenu_bg.png", Texture.class));

		// contextMenu.getButton(0).addClickListener(() ->
		// System.out.println("Button 1 pressed!"));

		contextMenu.getButton(0).addClickListener(() -> {
			PositionC pos = entityM.getComponent(activePlayer.selectedunits.get(0), PositionC.class);
			TilemapC tilemap = entityM.getComponent(map, TilemapC.class);

			MoveOrder order = new MoveOrder(rs.getMouseTarget(0f, Gdx.input),
					pf.findShortesPath(tilemap.getTileAt(pos.position.x, pos.position.z),
							tilemap.getTileAt(rs.getMouseTarget(0f, Gdx.input).x, rs.getMouseTarget(0f, Gdx.input).z)),
					tilemap);

			entityM.getComponent(activePlayer.selectedunits.get(0), OrderableC.class).newOrder(order);
		});
		contextMenu.getButton(1).addClickListener(() -> System.out.println("Button 2 pressed!"));
		contextMenu.getButton(2).addClickListener(() -> System.out.println("Button 3 pressed!"));
		contextMenu.getButton(3).addClickListener(() -> System.out.println("Button 4 pressed!"));
		contextMenu.getButton(4).addClickListener(() -> System.out.println("Button 5 pressed!"));
		contextMenu.getButton(5).addClickListener(() -> System.out.println("Button 6 pressed!"));

		inGameInput = new InGameInput(activePlayer, contextMenu, entityM, rs.camera);
		input.addProcessor(contextMenu);
		input.addProcessor(inGameInput);
		input.addProcessor(cs);

		Gdx.input.setInputProcessor(input);
		tr = new TilemapReader(rs);
		utp = new UnitTypeParser(rs);
		pf = new Pathfinder();

		map = tr.readMap("assets/maps/map1.xml");

		new OrderSystem(pf, entityM.getComponent(map, TilemapC.class));

		ms = new MoveSystem(entityM.getComponent(map, TilemapC.class));

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

		// this.contextMenu.update();
		this.spriteBatch.begin();
		this.contextMenu.render(spriteBatch);
		this.spriteBatch.end();
	}
}
