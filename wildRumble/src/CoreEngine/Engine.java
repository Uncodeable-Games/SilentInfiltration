package CoreEngine;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.vector.Vector2f;

import CoreEngine.components.Controll;
import CoreEngine.components.PositionC;
import CoreEngine.components.TestComponent;
import CoreEngine.components.VelocityC;
import CoreEngine.ecs.EntityManager;
import CoreEngine.ecs.EventManager;
import CoreEngine.ecs.SystemManager;
import CoreEngine.systems.ControllerSystem;
import CoreEngine.systems.TestSystem;
import CoreEngine.systems.MoveSystem;
import Widgetclasses.Hero;
import Widgetclasses.Map;
import Widgetclasses.Moveable;
import Widgetclasses.Obstracle;
import Widgetclasses.Units;
import Widgetclasses.Widget;
import static org.lwjgl.opengl.GL11.*;


public class Engine {
	
	public static CoreGame coreGame;
	static EntityManager entityM;
	static SystemManager systemM;
	static EventManager eventM;
	static int entity = -1;
	static Hero hero;
	static boolean ready = false;

	public static void main (String[] args){

		createWindow();
		createGame();
		initGame();
		gameLoop();
		cleanup();
	}
	
	private static void createWindow(){
		Window.create(1024, 768);
	}
	
	private static void createGame(){
		coreGame = new CoreGame();
	}
	
	public static Map map;
	
	public static void initGame(){
		map = new Map(1024,768);
		
		hero = new Hero("Player1", "/res/player_texture.png");
		hero.col = 25;
		hero.col_height = 1;
		int bla = 5;
		Widget.bla(bla);
		System.out.println(bla);
		
		Units enemy = new Units("Player1", "/res/indian_texture.png");
		enemy.col = 20;
		enemy.col_height = 1;
		
		enemy.cord_x = 500;
		enemy.cord_y = 500;
		
		
		entityM = new EntityManager();
		systemM = new SystemManager(entityM);
		eventM = new EventManager();
		
		//TestSystem ts = new TestSystem(systemM, entityM, eventM);
		MoveSystem mv = new MoveSystem(systemM, entityM, eventM);
		ControllerSystem cs = new ControllerSystem(systemM, entityM, eventM);
		
		entity = entityM.createEntity();		
		entityM.addComponent(entity, new PositionC(new Vector2f(40,40)));
		entityM.addComponent(entity, new VelocityC(new Vector2f()));
		entityM.addComponent(entity, new Controll());

		performanceTestECS();
		ready = true;


	}
	
	private static void performanceTestECS()
	{
		for(int i = 0; i < 100000; i++){
			int entity = entityM.createEntity();		
			entityM.addComponent(entity, new PositionC(new Vector2f(40,40)));
			entityM.addComponent(entity, new VelocityC(new Vector2f()));
			entityM.addComponent(entity, new Controll());
		}
	}
	
	static long dt = 0;
	static long lastTime = System.currentTimeMillis();
//	
//	static long logicRate = 60;
//	static long timestep = 1000 / logicRate;
//	static long timeCount = 0;
	
	private static void gameLoop(){
		while(!Display.isCloseRequested()){
			long currentTime = System.currentTimeMillis();
			dt = currentTime - lastTime;
			if(!ready)
				continue;
			System.out.println(dt);
			
			systemM.update(dt);
//			timeCount += dt;
//			while(timeCount >= timestep){
//				systemM.update(timestep);
//				timeCount -= timestep;
//			}
			if(entity >= 0 && hero != null && entityM.hasComponent(entity, PositionC.class)){
				PositionC pc = entityM.getComponent(entity, PositionC.class);
				hero.cord_x = (int) pc.position.x;
				hero.cord_y = (int) pc.position.y;
				hero.angle = (int) pc.rotation;
			}
			Window.clear();
//			coreGame.input();
//			coreGame.logic();
			coreGame.render();
			
			Window.update();
			lastTime = currentTime;
		}
	}
	
	private static void cleanup(){
		coreGame.dispose();
		Window.destroy();
	}
	
}