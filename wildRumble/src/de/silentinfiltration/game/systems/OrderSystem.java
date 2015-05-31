package de.silentinfiltration.game.systems;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.util.vector.Vector2f;

import de.silentinfiltration.engine.ai.Node;
import de.silentinfiltration.engine.ai.Order;
import de.silentinfiltration.engine.ai.Pathfinder;
import de.silentinfiltration.engine.ai.SimpleOrder;
import de.silentinfiltration.engine.ecs.BaseSystem;
import de.silentinfiltration.engine.ecs.EntityManager;
import de.silentinfiltration.engine.ecs.Event;
import de.silentinfiltration.engine.ecs.EventManager;
import de.silentinfiltration.engine.ecs.SystemManager;
import de.silentinfiltration.engine.exceptions.ComponentNotFoundEx;
import de.silentinfiltration.game.components.Control;
import de.silentinfiltration.game.components.PositionC;
import de.silentinfiltration.game.components.VelocityC;
import de.silentinfiltration.engine.tilemap.Tile;
import de.silentinfiltration.engine.tilemap.Tilemap;

public class OrderSystem extends BaseSystem {

	Map<Integer, Deque<Order>> orderQueue = new HashMap<Integer, Deque<Order>>();

	public Tilemap tilemap;

	public OrderSystem(SystemManager systemManager,
			EntityManager entityManager, EventManager eventManager) {
		super(systemManager, entityManager, eventManager);
		eventManager.registerForEvent(this, "MoveOrder");
	}

	@Override
	public boolean matchesSystem(int entityId) {
		return orderQueue.containsKey(entityId) && entityManager.hasComponent(entityId, PositionC.class)
				&& entityManager.hasComponent(entityId, VelocityC.class)
				&& entityManager.hasComponent(entityId, Control.class);
	}

	@Override
	public void update(double dt, int entity) throws ComponentNotFoundEx {
		//if  return;
		Deque<Order> oq = orderQueue.get(entity);
		Order tmp = oq.peek();
		Order supertmp = tmp;
		while(!(tmp instanceof SimpleOrder)){
			if (tmp.orderQueue == null) System.out.println("ERROR No orderQueue");
			oq = tmp.orderQueue;
			supertmp = tmp;
			tmp = oq.peek();
		}
		SimpleOrder currentOrder = (SimpleOrder) tmp;
		
		if (currentOrder.order == "move"){
			
			Vector2f pos = entityManager.getComponent(entity, PositionC.class).position;
			
			if (tilemap.getTileAt((int)pos.x, (int)pos.y) != currentOrder.target){
				VelocityC vel = entityManager.getComponent(entity, VelocityC.class);
				Tile goal = (Tile) currentOrder.target;
				float tempx = goal.x - pos.x;
				float tempy = goal.y - pos.y;
				vel.velocity.x = (float) (tempx/(Math.sqrt( tempx*tempx + tempy*tempy)) * vel.maxspeed);
				vel.velocity.y = (float) (tempy/(Math.sqrt( tempx*tempx + tempy*tempy)) * vel.maxspeed);
				
			} else	{
				oq.pop();
				if(oq.isEmpty()){ 
					if (!currentOrder.superOrder.clearStack()){
						orderQueue.remove(entity);
						return;
					}
					}
			}
		}
		if (orderQueue.get(entity).isEmpty()) orderQueue.remove(entity);
		
	}

	@Override
	public void render(int entity) throws ComponentNotFoundEx {
		// TODO Auto-generated method stub

	}

	public void receiveEvent(Event e, double dt) throws ComponentNotFoundEx {
		Order newOrder = new Order(e.entityID, null);
		
		if (e.eventType == "MoveOrder") {
			//orderQueue = new HashMap<Integer, Deque<Order>>();
			orderQueue.put(e.entityID, new ArrayDeque<Order>());
			
			PositionC pos = entityManager.getComponent(e.entityID,
					PositionC.class);
			Tile goal = (Tile) e.ObjectQueue.get(0);
			Tile start = tilemap.getTileAt((int)pos.position.x,
					(int) pos.position.y);
			Pathfinder pf = new Pathfinder();
			Map<Node,Node> path = pf.findShortesPath(start, goal);
			
			Node tmp = goal;
			while (path != null && path.containsKey(tmp)) {
				newOrder.orderQueue.push(SimpleOrder.simpleMoveOrder(
						e.entityID, tmp, newOrder));
				tmp = path.get(tmp);
			}
			newOrder.orderQueue.push(SimpleOrder.simpleMoveOrder(e.entityID,
					tmp, newOrder));
		}
		
		
		if (!orderQueue.containsKey(e.entityID)){
			orderQueue.put(e.entityID, new ArrayDeque<Order>());
		}
		orderQueue.get(e.entityID).add(newOrder);
	}

}
