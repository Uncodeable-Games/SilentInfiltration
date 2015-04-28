package CoreEngine;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import CoreEngine.components.Collision;
import CoreEngine.components.Control;
import CoreEngine.components.PositionC;
import CoreEngine.components.VelocityC;
import CoreEngine.components.Visual;
import CoreEngine.ecs.EntityManager;
import CoreEngine.ecs.EventManager;
import CoreEngine.ecs.SystemManager;
import CoreEngine.systems.CollisionSystem;
import CoreEngine.systems.ControllerSystem;
import CoreEngine.systems.RenderSystem;
import CoreEngine.systems.MoveSystem;
import Exceptions.ComponentNotFoundEx;

public class Engine {

	static EntityManager entityM;
	static SystemManager systemM;
	static EventManager eventM;
	
	//TODO: Necessary?
	static int hero = -1;
	static int enemy = -1;
	
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

	public static void initGame() throws ComponentNotFoundEx {

		entityM = new EntityManager();
		systemM = new SystemManager(entityM);
		eventM = new EventManager();

		MoveSystem mv = new MoveSystem(systemM, entityM, eventM);
		ControllerSystem cs = new ControllerSystem(systemM, entityM, eventM);
		RenderSystem rs = new RenderSystem(systemM, entityM, eventM);
		CollisionSystem cols = new CollisionSystem(systemM, entityM, eventM);

		//TODO: Find an easier Way to initialize entities 
		hero = entityM.createEntity();
		entityM.addComponent(hero, 
				new Visual("/res/player_texture.png"),
				new PositionC(new Vector2f(200, 200)), 
				new VelocityC(new Vector2f()), 
				new Control(),
				new Collision(true));	
		entityM.getComponent(hero, VelocityC.class).drag = 0.99f;
		entityM.getComponent(hero, Control.class).withmouse = true;
		entityM.getComponent(hero, Control.class).withwasd = true;
		entityM.getComponent(hero, Collision.class).ccol = 20;

		enemy = entityM.createEntity();
		entityM.addComponent(enemy, 
				new Visual("/res/indian_texture.png"), 
				new PositionC(new Vector2f(500, 500)),
				new Collision(true));
		entityM.getComponent(enemy, Collision.class).ccol = 20;
	
		
//		entityM.getComponent(enemy, Control.class).withmouse = true;	//Exception-Test

		ready = true;
		// performanceTestECS();

	}

	@SuppressWarnings("unused")
	private static void performanceTestECS() {
		for (int i = 0; i < 10000; i++) {
			int entity = entityM.createEntity();
			entityM.addComponent(entity, new PositionC(new Vector2f(0,0)));
		}
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
//			System.out.println(dt);
			Window.clear();

			try {
				systemM.update(dt);
			} catch (ComponentNotFoundEx e) {
				e.printStackTrace();
			}
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