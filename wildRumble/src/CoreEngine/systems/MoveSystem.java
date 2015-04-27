package CoreEngine.systems;

import CoreEngine.components.PositionC;
import CoreEngine.components.VelocityC;
import CoreEngine.ecs.BaseSystem;
import CoreEngine.ecs.EntityManager;
import CoreEngine.ecs.EventManager;
import CoreEngine.ecs.SystemManager;

public class MoveSystem extends BaseSystem {

	
	
	public MoveSystem(SystemManager systemManager, EntityManager entityManager, EventManager eventManager) {
		super(systemManager, entityManager, eventManager);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean matchesSystem(int entityId) {
		return entityManager.hasComponent(entityId, PositionC.class) && 
				entityManager.hasComponent(entityId, VelocityC.class);
	}

	@Override
	public void update(long dt, int entity) {
		PositionC pos = entityManager.getComponent(entity, PositionC.class);
		VelocityC vel = entityManager.getComponent(entity, VelocityC.class);
		
		pos.position.x += vel.velocity.x * dt / 60;
		pos.position.y += vel.velocity.y * dt / 60;

	}

	@Override
	public void render(int entity) {
		// TODO Auto-generated method stub

	}

}
