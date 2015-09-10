package de.mih.core.game.ai.orders;

import java.util.Map;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Event;

import de.mih.core.engine.ai.BTreeParser;
import de.mih.core.engine.ai.BaseOrder;
import de.mih.core.engine.ai.Pathfinder;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.ecs.EventManager;
import de.mih.core.engine.ecs.events.BaseEvent;
import de.mih.core.engine.ecs.events.orderevents.OrderToPoint_Event;
import de.mih.core.game.components.OrderableC;
import de.mih.core.game.components.TilemapC;

public class MoveOrder extends BaseOrder {

	static String BtreePath = "assets/btrees/movetotile.tree";

	public Map<Integer, Integer> path;
	public TilemapC tilemap;
	public Vector3 target;

	public MoveOrder(Vector3 t, Map<Integer, Integer> p, TilemapC map) {
		target = t;
		path = p;
		tilemap = map;
	}

	@Override
	public void handle(int entity) {
		OrderableC order = EntityManager.getInstance().getComponent(entity, OrderableC.class);
		if (!order.isinit) {

			order.btree = BTreeParser.readInBTree(BtreePath, entity);
			order.isinit = true;

			EventManager.getInstance().fire(new OrderToPoint_Event(entity, target));
		}

		if (order.btree != null) {
			order.btree.step();
		}
	}
}
