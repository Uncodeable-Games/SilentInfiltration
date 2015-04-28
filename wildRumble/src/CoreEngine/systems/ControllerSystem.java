package CoreEngine.systems;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import CoreEngine.components.Control;
import CoreEngine.components.PositionC;
import CoreEngine.components.VelocityC;
import CoreEngine.ecs.BaseSystem;
import CoreEngine.ecs.EntityManager;
import CoreEngine.ecs.EventManager;
import CoreEngine.ecs.SystemManager;
import Exceptions.ComponentNotFoundEx;

public class ControllerSystem extends BaseSystem {

	public ControllerSystem(SystemManager systemManager,
			EntityManager entityManager, EventManager eventManager) {
		super(systemManager, entityManager, eventManager);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean matchesSystem(int entityId) {
		return entityManager.hasComponent(entityId, VelocityC.class)
				&& entityManager.hasComponent(entityId, Control.class)
				&& entityManager.hasComponent(entityId, PositionC.class);
	}

	@Override
	public void update(long dt, int entity) throws ComponentNotFoundEx {
		int speed = 3;
		VelocityC veloComp = entityManager
					.getComponent(entity, VelocityC.class);
		Control control = entityManager.getComponent(entity, Control.class);
		PositionC position = entityManager
				.getComponent(entity, PositionC.class);

		if (control.withwasd) {

			if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
				speed = 5;
			}

			if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
				veloComp.velocity.y = -2 * speed;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
				veloComp.velocity.y = 2 * speed;
			}

			if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
				veloComp.velocity.x = -2 * speed;

			}
			if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
				veloComp.velocity.x = 2 * speed;
			}

		}

		if (control.withmouse) {
			position.angle = (int) Math.toDegrees(Math.atan2(Mouse.getY()
					- position.position.y, Mouse.getX() - position.position.x)) - 90;
		}

	}

	@Override
	public void render(int entity) {
		// TODO Auto-generated method stub

	}

}
