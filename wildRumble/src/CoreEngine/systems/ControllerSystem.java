package CoreEngine.systems;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import CoreEngine.components.Controll;
import CoreEngine.components.PositionC;
import CoreEngine.components.VelocityC;
import CoreEngine.ecs.BaseSystem;
import CoreEngine.ecs.EntityManager;
import CoreEngine.ecs.EventManager;
import CoreEngine.ecs.SystemManager;

public class ControllerSystem extends BaseSystem{

	public ControllerSystem(SystemManager systemManager,
			EntityManager entityManager, EventManager eventManager) {
		super(systemManager, entityManager, eventManager);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean matchesSystem(int entityId) {
		return entityManager.hasComponent(entityId, VelocityC.class) &&
				entityManager.hasComponent(entityId, Controll.class) &&
				entityManager.hasComponent(entityId, PositionC.class);
	}

	@Override
	public void update(long dt, int entity) {
		int speed = 1;
		VelocityC veloComp = entityManager.getComponent(entity, VelocityC.class);
		PositionC posComp = entityManager.getComponent(entity, PositionC.class);

		
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			speed = 3;
		}
		veloComp.velocity = new Vector2f();
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			veloComp.velocity.y = - 2 * speed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			veloComp.velocity.y = 2 * speed;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			veloComp.velocity.x = - 2 * speed;

		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			veloComp.velocity.x =  2 * speed;

		}

		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			//image_size.x++;
			//image_size.y++;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			//image_size.x--;
			//image_size.y--;
		}

		float angle = (float) (Math.toDegrees(Math.atan2(Mouse.getY() - posComp.position.y,
				Mouse.getX() - posComp.position.x)) - 90);		
		posComp.rotation = angle;
	}

	@Override
	public void render(int entity) {
		// TODO Auto-generated method stub
		
	}

}
