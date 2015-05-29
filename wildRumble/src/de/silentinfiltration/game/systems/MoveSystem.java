package de.silentinfiltration.game.systems;

import de.silentinfiltration.engine.ecs.BaseSystem;
import de.silentinfiltration.engine.ecs.EntityManager;
import de.silentinfiltration.engine.ecs.Event;
import de.silentinfiltration.engine.ecs.EventManager;
import de.silentinfiltration.engine.ecs.SystemManager;
import de.silentinfiltration.engine.exceptions.ComponentNotFoundEx;
import de.silentinfiltration.game.components.PositionC;
import de.silentinfiltration.game.components.VelocityC;


public class MoveSystem extends BaseSystem {


	public MoveSystem(SystemManager systemManager, EntityManager entityManager,
			EventManager eventManager) {
		super(systemManager, entityManager, eventManager);
		
		//TODO: Is there a better place to register for events? 
		eventManager.registerForEvent(this, "onCollision");
	}

	@Override
	public boolean matchesSystem(int entityId) {
		return entityManager.hasComponent(entityId, PositionC.class)
				&& entityManager.hasComponent(entityId, VelocityC.class);
	}

	@Override
	public void update(double dt, int entity) throws ComponentNotFoundEx {
		PositionC pos = entityManager.getComponent(entity, PositionC.class);
		VelocityC vel = entityManager.getComponent(entity, VelocityC.class);
		System.out.println(entity);
		if (Math.abs(vel.velocity.x) < 0.5) {
			vel.velocity.x = 0;
		}
		if (Math.abs(vel.velocity.y) < 0.5) {
			vel.velocity.y = 0;
		}

		vel.velocity.x = vel.velocity.x * vel.drag;
		vel.velocity.y = vel.velocity.y * vel.drag;

		double tmp = pos.position.x + vel.velocity.x * dt / 60;
		//if(tmp >= 0 && tmp <= 9)
			pos.position.x = (float) tmp;
		tmp = pos.position.y + vel.velocity.y * dt / 60;
		//aif(tmp >= 0 && tmp <= 9)
			pos.position.y = (float) tmp;
	}

	@Override
	public void render(int entity) {

	}
	
	@Override
	public void receiveEvent(Event e, double dt) throws ComponentNotFoundEx {
		if (e.eventType == "onCollision") {
			if (entityManager.hasComponent(e.entityID, VelocityC.class) && entityManager.hasComponent(e.entityID, VelocityC.class)) {
				VelocityC vel = entityManager.getComponent(e.entityID,
						VelocityC.class);
				PositionC pos = entityManager.getComponent(e.entityID,
						PositionC.class);
				pos.position.x -= vel.velocity.x * dt ;
				pos.position.y -= vel.velocity.y * dt;
				//System.out.println(pos.position);
				vel.velocity.x = 0;
				vel.velocity.y = 0;
			}
		}
	}

}
