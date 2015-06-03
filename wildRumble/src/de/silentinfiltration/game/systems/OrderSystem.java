package de.silentinfiltration.game.systems;

import de.silentinfiltration.engine.ai.Node;
import de.silentinfiltration.engine.ai.Pathfinder;
import de.silentinfiltration.engine.ai.behaviourtree.Blackboard;
import de.silentinfiltration.engine.ai.behaviourtree.ParentTaskController;
import de.silentinfiltration.engine.ai.behaviourtree.Sequence;
import de.silentinfiltration.engine.ai.behaviourtree.Task;
import de.silentinfiltration.engine.ecs.BaseSystem;
import de.silentinfiltration.engine.ecs.EntityManager;
import de.silentinfiltration.engine.ecs.EventManager;
import de.silentinfiltration.engine.ecs.SystemManager;
import de.silentinfiltration.engine.exceptions.ComponentNotFoundEx;
import de.silentinfiltration.engine.tilemap.Tilemap;
import de.silentinfiltration.game.ai.behaviourtree.MoveToTileTask;
import de.silentinfiltration.game.components.AIControlled;
import de.silentinfiltration.game.components.Control;
import de.silentinfiltration.game.components.PositionC;
import de.silentinfiltration.game.components.VelocityC;

public class OrderSystem extends BaseSystem {

	Tilemap tilemap;
	
	public OrderSystem(SystemManager systemManager,
			EntityManager entityManager, EventManager eventManager, Tilemap map) {
		super(systemManager, entityManager, eventManager);
		tilemap = map;
		
	}

	@Override
	public boolean matchesSystem(int entityId) {
		return entityManager.hasComponent(entityId, VelocityC.class)
				&& entityManager.hasComponent(entityId, Control.class)
				&& entityManager.hasComponent(entityId, PositionC.class)
				&& entityManager.hasComponent(entityId, AIControlled.class);
	}

	@Override
	public void update(double dt, int entity) throws ComponentNotFoundEx {
			
	}

	@Override
	public void render(int entity) throws ComponentNotFoundEx {

	}
	
	public void moveToDestination(int entity ,Node destination) throws ComponentNotFoundEx{
		Blackboard bb = new Blackboard();
		bb.entityM = entityManager;
		bb.entity = entity;
		bb.map = tilemap;
		bb.map.getTileAt(0, 0);
		bb.destination = bb.map.getTileAt((int)destination.x, (int)destination.y);
		bb.pf = new Pathfinder();
		Task newTask = new Sequence(bb);
		((ParentTaskController)newTask.GetControl()).Add(new MoveToTileTask(bb));
		newTask.Start();
	}

}
