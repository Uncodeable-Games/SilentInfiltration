package CoreEngine.systems;

import CoreEngine.components.TestComponent;
import CoreEngine.ecs.EntityManager;
import CoreEngine.ecs.BaseSystem;
import CoreEngine.ecs.EventManager;
import CoreEngine.ecs.SystemManager;

public class TestSystem extends BaseSystem {

	public TestSystem(SystemManager systemManager, EntityManager entityManager, EventManager eventManager) {
		super(systemManager, entityManager, eventManager);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean matchesSystem(int entity) {
		return entityManager.hasComponent(entity, TestComponent.class);
		//return false;
	}

	@Override
	public void update(long dt, int entity) {
		// TODO Auto-generated method stub
	}

	@Override
	public void render(int entity) {
		// TODO Auto-generated method stub

	}

}
