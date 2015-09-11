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
import de.mih.core.engine.tilemap.Tile;
import de.mih.core.engine.tilemap.Tilemap;
import de.mih.core.game.components.OrderableC;
import de.mih.core.game.components.TilemapC;

public class MoveOrder extends BaseOrder {

	static String BtreePath = "assets/btrees/movetotile.tree";

	public Map<Tile, Tile> path;
	public Tilemap tilemap;
	public Vector3 target;

	public MoveOrder(Vector3 target, Map<Tile, Tile> path, Tilemap tilemap) {
		this.target = target;
		this.path = path;
		this.tilemap = tilemap;
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
