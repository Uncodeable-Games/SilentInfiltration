package de.mih.core.game;

import java.util.Map;

import de.mih.core.engine.ai.Pathfinder;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.ecs.EventManager;
import de.mih.core.engine.ecs.SystemManager;
import de.mih.core.engine.io.TilemapReader;
import de.mih.core.engine.io.UnitTypeParser;
import de.mih.core.game.components.ColliderC;
import de.mih.core.game.components.Control;
import de.mih.core.game.components.NodeC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.TilemapC;
import de.mih.core.game.components.VelocityC;
import de.mih.core.game.components.Visual;
import de.mih.core.game.input.ContextMenu;
import de.mih.core.game.input.InGameInput;
import de.mih.core.game.player.Player;
import de.mih.core.game.systems.ControllerSystem;
import de.mih.core.game.systems.MoveSystem;
import de.mih.core.game.systems.RenderSystem;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class MiH extends ApplicationAdapter {

	static EntityManager entityM;
	static SystemManager systemM;
	static EventManager eventM;
	static RenderSystem rs;
	static ControllerSystem cs;
	static MoveSystem ms;
	static UnitTypeParser utp;
	static Pathfinder pf;
	static TilemapReader tr;
	static InputMultiplexer input;
	static int map;
	static InGameInput inGameInput;
	static ContextMenu contextMenu;
	
	static AssetManager assetManager;
	
	Player activePlayer;
	int cam_target = -1;

	Map<Integer, Integer> path;

	public void create() {
		entityM = new EntityManager();
		systemM = new SystemManager(entityM, 5);
		eventM = new EventManager();
		
		activePlayer = new Player("localplayer",0,entityM);

		
		assetManager = new AssetManager();
		//Gdx.files.internal("assets/textures/contextmenu_bg.png");
		assetManager.load("assets/textures/contextmenu_bg.png", Texture.class);

		assetManager.finishLoading();
		rs = new RenderSystem(systemM, entityM, eventM,
				new PerspectiveCamera(75, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
		input = new InputMultiplexer();
		
		cs = new ControllerSystem(systemM, entityM, eventM, rs, Gdx.input);
		
		while(!assetManager.isLoaded("assets/textures/contextmenu_bg.png"))
		{
			System.out.println("Loading textures!");
		}
		contextMenu = new ContextMenu(50, assetManager.get("assets/textures/contextmenu_bg.png",Texture.class));
		inGameInput = new InGameInput(activePlayer,contextMenu,entityM,rs.camera);
		input.addProcessor(contextMenu);
		input.addProcessor(inGameInput);
		input.addProcessor(cs);

		Gdx.input.setInputProcessor(input);
		tr = new TilemapReader(rs, entityM);
		utp = new UnitTypeParser(rs, entityM);
		pf = new Pathfinder(entityM);

		map = tr.readMap("assets/maps/map1.xml");
		
		ms = new MoveSystem(systemM, entityM, eventM, entityM.getComponent(map, TilemapC.class));
	
		// Robocop!!!111elf
		utp.newUnit("robocop");
		
	
		entityM.getComponent(utp.newUnit("robobot"), PositionC.class).position.x = 1f;
		
		entityM.getComponent(utp.newUnit("robobot"), PositionC.class).position.z = -1f;
		
		entityM.getComponent(utp.newUnit("robobot"), PositionC.class).position.x = -1f;
		//

		// TODO: Delete! (Pathfinder-Test)
		end = entityM.getComponent(map, TilemapC.class).getTileAt(1, 1);
		//
		
		this.spriteBatch = new SpriteBatch();
	}

	// TODO: Delete! (Pathfinder-Test)
	TilemapC tilemap;
	int start = -1;
	int end = -1;
	//
	SpriteBatch spriteBatch;

	public void render() {
		
		// TODO: Delete! (Pathfinder-Test)
		for (int i = 0; i < entityM.entityCount; i++) {
			if (entityM.hasComponent(i, NodeC.class)) {
				if (!entityM.getComponent(i, NodeC.class).blocked && entityM.hasComponent(i, Visual.class)) {
					entityM.removeComponent(i, entityM.getComponent(i, Visual.class));
				}
			}
		}
		tilemap = entityM.getComponent(map, TilemapC.class);
		int x = tilemap.cordToIndex_x(rs.getMouseTarget(0, Gdx.input).x);
		int z = tilemap.cordToIndex_z(rs.getMouseTarget(0, Gdx.input).z);
		start = tilemap.getTileAt(0, 0);
		if (x >= 0 && x < tilemap.length && z >= 0 && z < tilemap.width) {
			if (!entityM.getComponent(tilemap.getTileAt(x, z), NodeC.class).blocked)
				end = tilemap.getTileAt(x, z);
		}
		path = pf.findShortesPath(start, end);
		int tmp = end;
		while (path.get(tmp) != null) {
			entityM.addComponent(tmp, new Visual("redbox", rs));
			entityM.getComponent(tmp, Visual.class).pos.y = tilemap.TILE_SIZE / 2f;
			entityM.getComponent(tmp, Visual.class).setScale(tilemap.TILE_SIZE,tilemap.TILE_SIZE, tilemap.TILE_SIZE);
			tmp = path.get(tmp);
		}
		entityM.addComponent(tmp, new Visual("redbox", rs));
		entityM.getComponent(tmp, Visual.class).pos.y = tilemap.TILE_SIZE / 2f;
		entityM.getComponent(tmp, Visual.class).setScale(tilemap.TILE_SIZE,tilemap.TILE_SIZE, tilemap.TILE_SIZE);
		//

		systemM.update(Gdx.graphics.getDeltaTime());
		systemM.render(Gdx.graphics.getDeltaTime());
		
		this.contextMenu.update();
		this.spriteBatch.begin();
		this.contextMenu.render(spriteBatch);
		this.spriteBatch.end();
	}
}
