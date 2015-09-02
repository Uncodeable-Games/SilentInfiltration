package de.mih.core.game.systems;

import de.mih.core.engine.ecs.BaseSystem;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.ecs.Event;
import de.mih.core.engine.ecs.EventManager;
import de.mih.core.engine.ecs.SystemManager;
import de.mih.core.engine.exceptions.ComponentNotFoundEx;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.VelocityC;


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
		if (Math.abs(vel.velocity.x) < 0.5) {
			vel.velocity.x = 0;
		}
		if (Math.abs(vel.velocity.y) < 0.5) {
			vel.velocity.y = 0;
		}
		if (Math.abs(vel.velocity.z) < 0.5) {
			vel.velocity.z = 0;
		}

		vel.velocity.x = vel.velocity.x * vel.drag;
		vel.velocity.y = vel.velocity.y * vel.drag;
		vel.velocity.z = vel.velocity.z * vel.drag;

		double tmp = pos.position.x + vel.velocity.x * dt ;
		pos.position.x = (float) tmp;
		
		tmp = pos.position.y + vel.velocity.y * dt ;
		pos.position.y = (float) tmp;
		
		tmp = pos.position.z + vel.velocity.z * dt ;
		pos.position.z = (float) tmp;
	}

	@Override
	public void render(int entity) {

	}
	
	@Override
	public void receiveEvent(Event e, double dt) throws ComponentNotFoundEx {
	}

	@Override
	public void update(double dt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		
	}

}
