package de.silentinfiltration.game.systems;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import de.silentinfiltration.engine.ecs.BaseSystem;
import de.silentinfiltration.engine.ecs.EntityManager;
import de.silentinfiltration.engine.ecs.EventManager;
import de.silentinfiltration.engine.ecs.SystemManager;
import de.silentinfiltration.game.components.Control;
import de.silentinfiltration.game.components.PositionC;
import de.silentinfiltration.game.components.VelocityC;
import de.silentinfiltration.engine.exceptions.ComponentNotFoundEx;

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
		float speed = 0.5f;
		VelocityC veloComp = entityManager
					.getComponent(entity, VelocityC.class);
		Control control = entityManager.getComponent(entity, Control.class);
		PositionC position = entityManager
				.getComponent(entity, PositionC.class);

		if (control.withwasd) {

			if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
				speed = 0.75f;
			}

			if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
				veloComp.velocity.y = -1* speed;
			}
			else if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
				veloComp.velocity.y = 1 * speed;
			}
//			else
//				veloComp.velocity.y = 0;

			if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
				veloComp.velocity.x = 1 * speed;

			}
			else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
				veloComp.velocity.x = -1 * speed;
			}
			
//			float length = veloComp.velocity.length();
//			if(Math.abs(length) > 1){
//				veloComp.velocity.x /= length;
//				veloComp.velocity.y /= length;
//			}
			
			//System.out.println(veloComp.velocity);
//			else 
//				veloComp.velocity.x = 0;

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
