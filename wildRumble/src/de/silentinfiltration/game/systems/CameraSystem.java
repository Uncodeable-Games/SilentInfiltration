package de.silentinfiltration.game.systems;

import de.silentinfiltration.engine.ecs.BaseSystem;
import de.silentinfiltration.engine.ecs.EntityManager;
import de.silentinfiltration.engine.ecs.EventManager;
import de.silentinfiltration.engine.ecs.SystemManager;
import de.silentinfiltration.engine.exceptions.ComponentNotFoundEx;
import de.silentinfiltration.game.components.CCamera;
import de.silentinfiltration.game.components.PositionC;

public class CameraSystem extends BaseSystem {

	public CameraSystem(SystemManager systemManager,
			EntityManager entityManager, EventManager eventManager) {
		super(systemManager, entityManager, eventManager);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean matchesSystem(int entityId) {
		return entityManager.hasComponent(entityId, CCamera.class)
				&& entityManager.hasComponent(entityId, PositionC.class);
	}

	@Override
	public void update(long dt, int entity) throws ComponentNotFoundEx {
		PositionC pos = entityManager.getComponent(entity, PositionC.class);
		CCamera cam = entityManager.getComponent(entity, CCamera.class);
		if(!cam.active)
			return;
		if(cam.focus > -1 && entityManager.hasComponent(cam.focus, PositionC.class)){
			PositionC tmp = entityManager.getComponent(cam.focus, PositionC.class);
			pos.position.x = tmp.position.x - cam.screen.getWidth()/2;
			pos.position.y = tmp.position.y - cam.screen.getHeight()/2;
		}
	}

	@Override
	public void render(int entity) throws ComponentNotFoundEx {
		// TODO Auto-generated method stub

	}

}
