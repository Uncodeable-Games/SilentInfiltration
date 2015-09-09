package de.mih.core.game.systems;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;

import de.mih.core.engine.ai.Pathfinder;
import de.mih.core.engine.ai.btree.Unit;
import de.mih.core.engine.ai.orders.MoveOrder;
import de.mih.core.engine.ecs.BaseSystem;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.ecs.SystemManager;
import de.mih.core.engine.ecs.events.BaseEvent;
import de.mih.core.engine.ecs.events.orderevents.OrderToPoint_Event;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.TilemapC;

public class OrderSystem extends BaseSystem {
	
	Pathfinder pf;
	TilemapC tilemap;

	public OrderSystem(SystemManager systemManager, EntityManager entityManager, Pathfinder p, TilemapC map) {
		super(systemManager, entityManager);
		pf = p;
		tilemap = map;
		OrderToPoint_Event.register(this);
	}

	@Override
	public boolean matchesSystem(int entityId) {
		return Unit.isEntityAUnit(entityId);
	}

	@Override
	public void update(double dt) {	
	}

	@Override
	public void update(double dt, int entity) {
		if (Unit.getUnitByEntity(entity).currentorder != null && Unit.getUnitByEntity(entity).btree != null)
			Unit.getUnitByEntity(entity).btree.step();
	}

	@Override
	public void render() {	
	}

	@Override
	public void render(int entity) {	
	}

	@Override
	public void onEventRecieve(Class<? extends BaseEvent> event, ArrayList<Object> params) {
		if (event.equals(OrderToPoint_Event.class)){
			Integer entity = (Integer) params.get(0);
			Vector3 target = (Vector3) params.get(1);
			PositionC pos = entityManager.getComponent(entity, PositionC.class);
			
			Unit.getUnitByEntity(entity).newOrder(new MoveOrder(pf.findShortesPath(tilemap.getTileAt(pos.position.x, pos.position.z), tilemap.getTileAt(target.x, target.z)), tilemap.getTileAt(target.x, target.z), tilemap));
		}
	}

}
