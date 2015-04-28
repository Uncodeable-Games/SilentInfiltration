package CoreEngine.systems;

import CoreEngine.components.PositionC;
import CoreEngine.components.VelocityC;
import CoreEngine.ecs.BaseSystem;
import CoreEngine.ecs.EntityManager;
import CoreEngine.ecs.Event;
import CoreEngine.ecs.EventManager;
import CoreEngine.ecs.SystemManager;
import Exceptions.ComponentNotFoundEx;

public class MoveSystem extends BaseSystem {

	//TODO: Delete!
	long dttest;
	
	public MoveSystem(SystemManager systemManager, EntityManager entityManager,
			EventManager eventManager) {
		super(systemManager, entityManager, eventManager);
		
		//TODO: Is there a better place to register for events? 
		eventManager.registerForEvent(this, 1);
	}

	@Override
	public boolean matchesSystem(int entityId) {
		return entityManager.hasComponent(entityId, PositionC.class)
				&& entityManager.hasComponent(entityId, VelocityC.class);
	}

	@Override
	public void update(long dt, int entity) throws ComponentNotFoundEx {
		dttest = dt;
		PositionC pos = entityManager.getComponent(entity, PositionC.class);
		VelocityC vel = entityManager.getComponent(entity, VelocityC.class);

		if (Math.abs(vel.velocity.x) < 0.5) {
			vel.velocity.x = 0;
		}
		if (Math.abs(vel.velocity.y) < 0.5) {
			vel.velocity.y = 0;
		}

		vel.velocity.x = vel.velocity.x * vel.drag;
		vel.velocity.y = vel.velocity.y * vel.drag;

		pos.position.x += vel.velocity.x * dt / 60;
		pos.position.y += vel.velocity.y * dt / 60;
	}

	@Override
	public void render(int entity) {

	}

	public void receiveEvent(Event e) throws ComponentNotFoundEx {
		if (e.eventType == 1) {
			if (entityManager.hasComponent(e.entityID, VelocityC.class) && entityManager.hasComponent(e.entityID, VelocityC.class)) {
				VelocityC vel = entityManager.getComponent(e.entityID,
						VelocityC.class);
				PositionC pos = entityManager.getComponent(e.entityID,
						PositionC.class);
				pos.position.x -= vel.velocity.x * dttest / 60;
				pos.position.y -= vel.velocity.y * dttest / 60;
				vel.velocity.x = 0;
				vel.velocity.y = 0;
			}
		}
	}

}
