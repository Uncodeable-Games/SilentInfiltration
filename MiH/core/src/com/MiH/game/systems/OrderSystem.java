//package com.MiH.game.systems;
//
//import com.MiH.engine.ai.Node;
//import com.MiH.engine.ai.Pathfinder;
//import com.MiH.engine.ai.behaviourtree.Blackboard;
//import com.MiH.engine.ai.behaviourtree.ParentTaskController;
//import com.MiH.engine.ai.behaviourtree.Sequence;
//import com.MiH.engine.ai.behaviourtree.Task;
//import com.MiH.engine.ecs.BaseSystem;
//import com.MiH.engine.ecs.EntityManager;
//import com.MiH.engine.ecs.EventManager;
//import com.MiH.engine.ecs.SystemManager;
//import com.MiH.engine.exceptions.ComponentNotFoundEx;
//import com.MiH.game.components.AIControlled;
//import com.MiH.game.components.Control;
//import com.MiH.game.components.PositionC;
//import com.MiH.game.components.VelocityC;
//
//public class OrderSystem extends BaseSystem {
//
//	Tilemap tilemap;
//	
//	public OrderSystem(SystemManager systemManager,
//			EntityManager entityManager, EventManager eventManager, Tilemap map) {
//		super(systemManager, entityManager, eventManager);
//		tilemap = map;
//		
//	}
//
//	@Override
//	public boolean matchesSystem(int entityId) {
//		return entityManager.hasComponent(entityId, VelocityC.class)
//				&& entityManager.hasComponent(entityId, Control.class)
//				&& entityManager.hasComponent(entityId, PositionC.class)
//				&& entityManager.hasComponent(entityId, AIControlled.class);
//	}
//
//	@Override
//	public void update(double dt, int entity) throws ComponentNotFoundEx {
//			
//	}
//
//	@Override
//	public void render(int entity) throws ComponentNotFoundEx {
//
//	}
//	
//	public void moveToDestination(int entity ,Node destination) throws ComponentNotFoundEx{
//		Blackboard bb = new Blackboard();
//		bb.entityM = entityManager;
//		bb.entity = entity;
//		bb.map = tilemap;
//		bb.map.getTileAt(0, 0);
//		bb.destination = bb.map.getTileAt((int)destination.x, (int)destination.y);
//		bb.pf = new Pathfinder();
//		Task newTask = new Sequence(bb);
//		((ParentTaskController)newTask.GetControl()).Add(new MoveToTileTask(bb));
//		newTask.Start();
//	}
//
//}
