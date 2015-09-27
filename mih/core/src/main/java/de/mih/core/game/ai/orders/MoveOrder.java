package de.mih.core.game.ai.orders;

import java.util.Map;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Event;

import de.mih.core.engine.ai.BTreeParser;
import de.mih.core.engine.ai.BaseOrder;
import de.mih.core.engine.ai.navigation.NavPoint;
import de.mih.core.engine.ai.navigation.Pathfinder;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.ecs.EventManager;
import de.mih.core.engine.ecs.events.BaseEvent;
import de.mih.core.engine.ecs.events.orderevents.OrderToPoint_Event;
import de.mih.core.engine.tilemap.Tile;
import de.mih.core.engine.tilemap.Tilemap;
import de.mih.core.game.components.OrderableC;

public class MoveOrder extends BaseOrder {

	static String BtreePath = "assets/btrees/movetotile.tree";

	public NavPoint[] path;
	public Vector3 target;
	public MoveState state;
	
	public enum MoveState
	{
		Moving,
		NodeReached,
		MoveToGoal,
		GoalReached,
		Finished
	}

	public MoveOrder(Vector3 target,NavPoint[] path) {
		this.target = target;
		this.path = path;
		this.state = MoveState.Moving;
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
