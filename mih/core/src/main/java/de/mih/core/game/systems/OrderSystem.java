package de.mih.core.game.systems;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;

import de.mih.core.engine.ai.BTreeParser;
import de.mih.core.engine.ai.Pathfinder;
import de.mih.core.engine.ai.orders.MoveOrder;
import de.mih.core.engine.ecs.BaseSystem;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.ecs.EventManager;
import de.mih.core.engine.ecs.SystemManager;
import de.mih.core.engine.ecs.events.BaseEvent;
import de.mih.core.engine.ecs.events.orderevents.OrderToPoint_Event;
import de.mih.core.game.components.OrderableC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.TilemapC;

public class OrderSystem extends BaseSystem {

	EntityManager entityM = EntityManager.getInstance();
	EventManager eventM = EventManager.getInstance();

	Pathfinder pf;
	TilemapC tilemap;

	public OrderSystem(Pathfinder p, TilemapC map) {
		super();
		pf = p;
		tilemap = map;
		eventM.register(this, OrderToPoint_Event.class);
	}

	@Override
	public boolean matchesSystem(int entityId) {
		return EntityManager.getInstance().hasComponent(entityId, OrderableC.class);
	}

	@Override
	public void update(double dt) {
	}

	@Override
	public void update(double dt, int entity) {
		OrderableC order = entityM.getComponent(entity, OrderableC.class);
		
		if (order.currentorder == null) return;
		
		if (!order.isinit){
			if (order.currentorder.getClass().equals(MoveOrder.class)){
				MoveOrder o = (MoveOrder) order.currentorder;
				order.btree = BTreeParser.readInBTree("assets/btrees/movetotile.tree", entity);
				order.isinit = true;
				
				eventM.fire(new OrderToPoint_Event(entity, o.target));
			}
		}
		
		if (order.btree != null) {
			order.btree.step();
		}
	}

	@Override
	public void render() {
	}

	@Override
	public void render(int entity) {
	}

	@Override
	public void onEventRecieve(BaseEvent event) {	
	}

}
