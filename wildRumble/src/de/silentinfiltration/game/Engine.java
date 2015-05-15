package de.silentinfiltration.game;

import java.io.IOException;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.Rectangle;
import org.lwjgl.util.vector.Vector2f;

import de.silentinfiltration.engine.Window;
import de.silentinfiltration.engine.ecs.EntityManager;
import de.silentinfiltration.engine.ecs.EventManager;
import de.silentinfiltration.engine.ecs.SystemManager;
import de.silentinfiltration.engine.exceptions.ComponentNotFoundEx;
import de.silentinfiltration.game.components.CCamera;
import de.silentinfiltration.game.components.Collision;
import de.silentinfiltration.game.components.Control;
import de.silentinfiltration.game.components.PositionC;
import de.silentinfiltration.game.components.VelocityC;
import de.silentinfiltration.game.components.Visual;
import de.silentinfiltration.game.systems.CameraSystem;
import de.silentinfiltration.game.systems.CollisionSystem;
import de.silentinfiltration.game.systems.ControllerSystem;
import de.silentinfiltration.game.systems.RenderSystem;
import de.silentinfiltration.game.systems.MoveSystem;
import de.silentinfiltration.game.tilemap.IsometricTileMapRenderer;
import de.silentinfiltration.game.tilemap.Tile;
import de.silentinfiltration.game.tilemap.Tilemap;

public class Engine {
	//TODO sollten wir mal umbenennen die klasse :D
	static EntityManager entityM;
	static SystemManager systemM;
	static EventManager eventM;
	static AssetManager assetM;
	static IsometricTileMapRenderer tilemapRenderer;
	
	//TODO: Necessary?
	static int hero = -1;
	static int enemy = -1;
	static int cam = -1;
	
	static boolean ready = false;

	public static void main(String[] args) {

		createWindow();
		try {
			initGame();
		} catch (ComponentNotFoundEx e) {
			e.printStackTrace();
		}
		gameLoop();
		cleanup();
	}

	private static void createWindow() {
		Window.create(1024, 768);
	}
	
	public static void loadAssets()
	{
		try {
			assetM.loadTexture("player_texture", "/res/player_texture.png", "PNG");
			assetM.loadTexture("indian_texture", "/res/indian_texture.png", "PNG");
			assetM.loadTexture("basic_tile", "/res/basic_tile.png", "PNG");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void initGame() throws ComponentNotFoundEx {

		entityM = new EntityManager();
		systemM = new SystemManager(entityM);
		eventM = new EventManager();
		assetM = new AssetManager();
		tilemapRenderer = new IsometricTileMapRenderer();
		tilemapRenderer.viewport = new Rectangle(100,100,800,600);
		loadAssets();

		
		//balabla
		ControllerSystem cs = new ControllerSystem(systemM, entityM, eventM);

		CollisionSystem cols = new CollisionSystem(systemM, entityM, eventM);

		MoveSystem mv = new MoveSystem(systemM, entityM, eventM);
		
		CameraSystem camSys = new CameraSystem(systemM, entityM, eventM);
		
		RenderSystem rs = new RenderSystem(systemM, entityM, eventM);

		int tilesize = 20;
		Tilemap map = new Tilemap(tilesize,tilesize);
		for(int i= 0; i< tilesize; i++){
			for(int j = 0; j < tilesize; j++){
				Tile tile = new Tile();
				tile.image_size = new Vector2f(32, 16);
				tile.tex = assetM.getTexture("basic_tile");
				map.setTileAt(i, j, tile);
			}
		}
		tilemapRenderer.tilemap = map;
		//TODO: Find an easier Way to initialize entities 
		hero = entityM.createEntity();
		entityM.addComponent(hero, 
				new Visual(assetM.getTexture("player_texture")),
				new PositionC(new Vector2f(512, 384)), 
				new VelocityC(new Vector2f()), 
				new Control(),
				new Collision(true, true));
				//new CCamera(new Rectangle(0,0,1024,768)));	
		entityM.getComponent(hero, VelocityC.class).drag = 0.5f;
		entityM.getComponent(hero, Control.class).withmouse = true;
		entityM.getComponent(hero, Control.class).withwasd = true;
		entityM.getComponent(hero, Collision.class).ccol = 20;

		enemy = entityM.createEntity();
		entityM.addComponent(enemy, 
				new Visual(assetM.getTexture("indian_texture")), 
				new PositionC(new Vector2f(500, 500)),
				new Collision(true, true));
		entityM.getComponent(enemy, Collision.class).ccol = 20;
		
		cam = entityM.createEntity();
		entityM.addComponent(cam, new VelocityC(new Vector2f()),new PositionC(new Vector2f(0, 0)), new CCamera(new Rectangle(0,0,1024,768), true));
		rs.setCamera(cam);
		entityM.getComponent(cam, CCamera.class).focus = hero;
	

//		entityM.getComponent(enemy, Control.class).withmouse = true;	//Exception-Test

		ready = true;
		// performanceTestECS();

	}
	

	static long dt = 0;
	static long lastTime = System.currentTimeMillis();

	private static void gameLoop() {
		while (!Display.isCloseRequested()) {
			long currentTime = System.currentTimeMillis();
			
			//TODO: Why not global?
			dt = currentTime - lastTime;
			
			if (!ready)
				continue;
			//System.out.println(dt);
			Window.clear();

			try {
				systemM.update(dt);
				tilemapRenderer.cam = entityM.getComponent(cam, PositionC.class).position;
			} catch (ComponentNotFoundEx e) {
				e.printStackTrace();
			}
			tilemapRenderer.render();
			try {
				systemM.render(dt);
			} catch (ComponentNotFoundEx e) {
				e.printStackTrace();
			}

			Window.update();
			lastTime = currentTime;
		}
	}

	private static void cleanup() {
		RenderSystem.disposeall(entityM);
		Window.destroy();
	}

}