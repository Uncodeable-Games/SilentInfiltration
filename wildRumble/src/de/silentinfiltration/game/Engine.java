package de.silentinfiltration.game;

import static org.lwjgl.opengl.GL11.glLoadIdentity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Rectangle;
import org.lwjgl.util.vector.Vector2f;

import de.silentinfiltration.engine.Window;
import de.silentinfiltration.engine.ai.Node;
import de.silentinfiltration.engine.ai.Pathfinder;
import de.silentinfiltration.engine.ai.behaviourtree.ParentTaskController;
import de.silentinfiltration.engine.ai.behaviourtree.Task;
import de.silentinfiltration.engine.ecs.EntityManager;
import de.silentinfiltration.engine.ecs.EventManager;
import de.silentinfiltration.engine.ecs.SystemManager;
import de.silentinfiltration.engine.exceptions.ComponentNotFoundEx;
import de.silentinfiltration.engine.io.TilemapReader;
import de.silentinfiltration.game.components.CCamera;
import de.silentinfiltration.game.components.Collision;
import de.silentinfiltration.game.components.Control;
import de.silentinfiltration.game.components.PositionC;
import de.silentinfiltration.game.components.VelocityC;
import de.silentinfiltration.game.components.Visual;
import de.silentinfiltration.game.systems.CameraSystem;
import de.silentinfiltration.game.systems.CollisionSystem;
import de.silentinfiltration.game.systems.ControllerSystem;
import de.silentinfiltration.game.systems.OrderSystem;
import de.silentinfiltration.game.systems.RenderSystem;
import de.silentinfiltration.game.systems.MoveSystem;
import de.silentinfiltration.game.tilemap.IsometricTileMapRenderer;
import de.silentinfiltration.engine.render.Sprite;
import de.silentinfiltration.engine.tilemap.Tile;
import de.silentinfiltration.engine.tilemap.Tilemap;

public class Engine {
	// TODO sollten wir mal umbenennen die klasse :D
	static EntityManager entityM;
	static SystemManager systemM;
	static EventManager eventM;
	static AssetManager assetM;
	static IsometricTileMapRenderer tilemapRenderer;

	// TODO: Necessary?
	static int hero = -1;
	static int enemy = -1;
	static int cam = -1;
	static Tilemap map;
	static boolean ready = false;

	public static void main(String[] args) {

		createWindow();
		try {
			initGame();
		} catch (ComponentNotFoundEx e) {
			e.printStackTrace();
		}
		try {
			gameLoop();
		} catch (ComponentNotFoundEx e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cleanup();
	}

	private static void createWindow() {
		Window.create(1024, 768);
	}

	public static void loadAssets() {
		try {
			assetM.loadTexture("player_texture", "/res/player_texture.png",
					"PNG", new Vector2f(16,16));
			assetM.loadTexture("indian_texture", "/res/indian_texture.png",
					"PNG", new Vector2f(16,16));
			assetM.loadTexture("basic_tile", "/res/basic_tile.png", "PNG", new Vector2f(32,32));
			assetM.loadTexture("basic_block", "/res/basic_block_tile.png",
					"PNG", new Vector2f(32,32));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void initGame() throws ComponentNotFoundEx {

		entityM = new EntityManager();
		systemM = new SystemManager(entityM, 5);
		eventM = new EventManager();
		assetM = new AssetManager();
		tilemapRenderer = new IsometricTileMapRenderer();
		tilemapRenderer.viewport = new Rectangle(0, 0, 1024, 768);
		loadAssets();

		// balabla
		
		TilemapReader tReader = new TilemapReader(assetM);
		map = tReader.readMap("/res/maps/map1.xml");
		

		CollisionSystem cols = new CollisionSystem(systemM, entityM, eventM);

		MoveSystem mv = new MoveSystem(systemM, entityM, eventM);

		CameraSystem camSys = new CameraSystem(systemM, entityM, eventM, 2);

		RenderSystem rs = new RenderSystem(systemM, entityM, eventM);
		
		OrderSystem os = new OrderSystem(systemM, entityM, eventM, map);
		
		ControllerSystem cs = new ControllerSystem(systemM, entityM, eventM, os, map);
		
		//int tilesize = 10;
		
		//map = new Tilemap(tilesize, tilesize);
//		map.tile_height = 32;
//		map.tile_width = 64;
		
		/*for (int i = 0; i < tilesize; i++) {
			for (int j = 0; j < tilesize; j++) {
				Tile tile = new Tile();
				tile.image_size = new Vector2f(32, 32);
				if (i == 3 && j != 7) {
					tile.tex = assetM.getTexture("basic_block");
					tile.blocked = true;
					// tile.image_size.y = 32;
				} else
					tile.tex = assetM.getTexture("basic_tile");
				tile.x=i;
				tile.y=j;
				map.setTileAt(i, j, tile);
			}
		}*/
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map.width; j++) {
		
				//System.out.println(i + ", " + j);// + " : " + map.getTileAt(i,j) == null);
		
				if(i == 7 && j == 0)
				{
				//	System.out.println(map.getTileAt(i, j));
					
				}
				if (i > 0)
					map.getTileAt(i, j).neighbours.add(map.getTileAt(i - 1, j));
				if (j > 0)
					map.getTileAt(i, j).neighbours.add(map.getTileAt(i, j - 1));
				if (i < map.width - 1)
					map.getTileAt(i, j).neighbours.add(map.getTileAt(i + 1, j));
				if (j < map.length - 1)
					map.getTileAt(i, j).neighbours.add(map.getTileAt(i, j + 1));
				if (i > 0 && j > 0)
					map.getTileAt(i, j).neighbours.add(map.getTileAt(i - 1,
							j - 1));
				if (i > 0 && j < map.width - 1)
					map.getTileAt(i, j).neighbours.add(map.getTileAt(i - 1,
							j + 1));
				if (j > 0 && i < map.length - 1)
					map.getTileAt(i, j).neighbours.add(map.getTileAt(i + 1,
							j - 1));
				if (i < map.width - 1 && j < map.length - 1)
					map.getTileAt(i, j).neighbours.add(map.getTileAt(i + 1,
							j + 1));

			}
		}
		rs.tilemap = map;
		rs.tilemapRenderer = tilemapRenderer;
		tilemapRenderer.tilemap = map;
		// TODO: Find an easier Way to initialize entities
		hero = entityM.createEntity();
		entityM.addComponent(hero,
				new Visual(assetM.getTexture("player_texture")), new PositionC(
						new Vector2f(0, 0)), new VelocityC(new Vector2f()),
				new Control(), new Collision(true, true));
		// new CCamera(new Rectangle(0,0,1024,768)));
		entityM.getComponent(hero, VelocityC.class).drag = 0.5f;
		entityM.getComponent(hero, VelocityC.class).maxspeed = 1;
		entityM.getComponent(hero, Control.class).withmouse = true;
		entityM.getComponent(hero, Control.class).withwasd = true;
		entityM.getComponent(hero, Collision.class).ccol = 0.7f;

		enemy = entityM.createEntity();
		entityM.addComponent(enemy,
				new Visual(assetM.getTexture("indian_texture")), new PositionC(
						new Vector2f(9, 9)), new Collision(true, true));
		entityM.getComponent(enemy, Collision.class).ccol = 0.7f;

		cam = entityM.createEntity();
		entityM.addComponent(cam, new VelocityC(new Vector2f()), new PositionC(
				new Vector2f(0, 0)), new CCamera(new Rectangle(0, 0, 1024,
				768), true), new Control());
		rs.setCamera(cam);
		entityM.getComponent(cam, Control.class).withkeys = true;
		entityM.getComponent(cam, VelocityC.class).drag = 0.2f;
		entityM.getComponent(cam, VelocityC.class).maxspeed = 200f;
		//SPEED : 50 / 100 tiles per second
		entityM.getComponent(cam, CCamera.class).focus = hero;
		
		cs.cam = cam;
		cs.tilemap = map;
		
		tilemapRenderer.cam = entityM.getComponent(cam, PositionC.class).position;

		// entityM.getComponent(enemy, Control.class).withmouse = true;
		// //Exception-Test

		ready = true;
		// performanceTestECS();
		pf = new Pathfinder();
		tilemapRenderer.cam = entityM.getComponent(cam, PositionC.class).position;


	}

	static Pathfinder pf;
	// long dt = 0;
	// static float lastTime = System.currentTimeMillis();
	static final double second = 1;
	static double targetUPS = 60;
	final static double frameTime = second / targetUPS;
	final static double maxFrameSkips = 10;

	static double currentTime;
	static double previousTime;
	static double elapsed;

	static double lag = 0;

	static double lastUPSUpdate = 0;
	static double lastFPSUpdate = 0;

	static int updatesProcessed = 0;
	static int framesProcessed = 0;
	static int skippedFrames = 0;

	private static void gameLoop() throws ComponentNotFoundEx {
		int ups = 0;
		int fps = 0;
		previousTime = (double) System.currentTimeMillis() / 1000.0f;
		while (!Display.isCloseRequested()) {
			double currentTime = (double) System.currentTimeMillis() / 1000.0f;

			
			elapsed = currentTime - previousTime;
			if(Math.abs(elapsed) < 0.01f)
				continue;

			lag += elapsed;

			while (lag > frameTime && skippedFrames < maxFrameSkips) {
				update(frameTime);

				lag -= frameTime;
				skippedFrames++;

				// Calculate the UPS counters
				updatesProcessed++;

				if (currentTime - lastUPSUpdate >= second) {
					ups = updatesProcessed;
					Display.setTitle("UPS: " + ups);

					updatesProcessed = 0;
					lastUPSUpdate = currentTime;
				}
			}

			Window.clear();
			double lagOffset = (double) (lag / frameTime);
			render(lagOffset);
			// Calculate the FPS counters
			framesProcessed++;

			if (currentTime - lastFPSUpdate >= second) {
				fps = framesProcessed;
				Display.setTitle(Display.getTitle() + " FPS: " + fps);

				framesProcessed = 0;
				lastFPSUpdate = currentTime;
			}
//			vec.x += 5;
//			if(vec.x  >= 1024)
//			{
//				vec.x = 0;
//				vec.y -= 5;
//			}
//			if(vec.y <= -768)
//			{
//				vec.y = 0;
//			}
			System.out.println("vec: " + vec);
			// Swap the buffers and update the game
			Display.update();

			skippedFrames = 0;
			previousTime = currentTime;
			continue;
		}

	}

	static Node start, goal;
	static Vector2f mouse = new Vector2f();
	static Vector2f tempvec = new Vector2f();
	static Map<Node,Node> currentPath = new HashMap<>();
	private static void update(double dt) {
		if (hero > -1 && entityM.hasComponent(hero, PositionC.class)) {
//			for (int i = 0; i < 10; i++) {
//				for (int j = 0; j < 10; j++) {
//					map.getTileAt(i, j).previous = null;
//					map.getTileAt(i, j).isPath = false;
//					map.getTileAt(i, j).f = map.getTileAt(i, j).g = 0;
//				}
//			}
			
			Vector2f pos = null;
			try {
				pos = entityM.getComponent(hero, PositionC.class).position;
			} catch (ComponentNotFoundEx e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			start = map.getTileAt(Math.min(9, (int)pos.x),
					Math.min(9, (int)pos.y));

			// start = map.getTileAt(0, 0);

			Vector2f campos = null;
			try {
				campos = entityM.getComponent(cam, PositionC.class).position;
			} catch (ComponentNotFoundEx e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (goal == null)
				goal = map.getTileAt(0, 0);
			
			mouse = new Vector2f(Mouse.getX() - campos.x, -Mouse.getY() - campos.y  );
			
			
//			mouse.x -= campos.x / 2.0;
//			mouse.y -= campos.y / 2.0;
			tempvec = map.screenToMap(mouse);
			
//			System.out.println("mouse");
//			System.out.println(mouse);
//			System.out.println(tempvec);
			if (tempvec.x >= 0 && tempvec.x < 10 && tempvec.y >= 0
					&& tempvec.y < 10)
				goal = map.getTileAt((int)tempvec.x,(int)tempvec.y);
			else
				goal = map.getTileAt(9,9);
			currentPath = pf.findShortesPath(start, goal);
			try {
				ParentTaskController.updateTasks(dt);
			} catch (ComponentNotFoundEx e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		try {
			systemM.update(dt);
			tilemapRenderer.cam = entityM.getComponent(cam, PositionC.class).position;
			// System.out.println(tilemapRenderer.cam);
		} catch (ComponentNotFoundEx e) {
			e.printStackTrace();
		}
	}
	static Vector2f vec = new Vector2f();

	/**
	* Creates the behavior tree and populates
	* the node hierarchy
	*/
	private static void render(double lagOffset) {
		Sprite tmp = assetM.getTexture("player_texture");
		
		

		GL11.glPushMatrix();
		glLoadIdentity();
		tmp.draw(new Vector2f(990,-32));
		GL11.glEnd();
		GL11.glPopMatrix();

		GL11.glPushMatrix();
		glLoadIdentity();
		tmp.draw(new Vector2f(32,-768));
		GL11.glEnd();
		GL11.glPopMatrix();

	
		GL11.glPushMatrix();
		glLoadIdentity();
		tmp.draw(new Vector2f(990,-768));
		GL11.glEnd();
		GL11.glPopMatrix();
		Vector2f c  = new Vector2f();
		try {
			c = entityM.getComponent(cam, PositionC.class).position;
		} catch (ComponentNotFoundEx e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//GL11.glViewport((int)c.x- Display.getWidth()/2,(int)c.y - Display.getHeight()/2,Display.getWidth(),Display.getHeight());
		tilemapRenderer.currentPath = currentPath;
		tilemapRenderer.render();
		System.out.println(currentPath);
		try {
			
			systemM.render(lagOffset);
		} catch (ComponentNotFoundEx e) {
			e.printStackTrace();
		}
		
		GL11.glPushMatrix();
		glLoadIdentity();
		tmp.draw(new Vector2f(Mouse.getX(),-Mouse.getY()));
		GL11.glEnd();
		GL11.glPopMatrix();
		
		GL11.glPushMatrix();
		System.out.println(tempvec);
		Vector2f t = new Vector2f(tempvec.x * 32, tempvec.y * 16);
		glLoadIdentity();
		try {
			tmp.draw(add( entityM.getComponent(cam, PositionC.class).position,t));
		} catch (ComponentNotFoundEx e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GL11.glEnd();
		GL11.glPopMatrix();
		
	}
	
	private static Vector2f add(Vector2f a, Vector2f b)
	{
		return new Vector2f(a.x + b.x , a.y + b.y);
	}

	private static void cleanup() {
		RenderSystem.disposeall(entityM);
		Window.destroy();
	}

}
