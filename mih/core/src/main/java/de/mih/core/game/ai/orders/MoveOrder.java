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
import de.mih.core.engine.tilemap.Tile;
import de.mih.core.engine.tilemap.Tilemap;
import de.mih.core.game.Game;
import de.mih.core.game.components.OrderableC;
import de.mih.core.game.events.order.OrderFinishedEvent;
import de.mih.core.game.events.order.OrderToPointEvent;

public class MoveOrder extends BaseOrder
{

	static String BtreePath = "assets/btrees/movetotile.tree";

	public Map<Tile, Tile> path;
	public Tilemap tilemap;
	public Vector3 target;
	public Tile start, end, currentGoal;
	public MoveState state;
	boolean isFinished = false;

	public enum MoveState
	{
		Moving, NodeReached, MoveToGoal, GoalReached, Finished
	}

	public MoveOrder(Vector3 target, Tile start, Tile end, Map<Tile, Tile> path, Tilemap tilemap)
	{
		this.target = target;
		this.path = path;
		this.start = start;
		this.end = end;
		//System.out.println("path found");
		Tile tmp = start;
//		while (tmp != null)
//		{
//			System.out.println(tmp + " -> " + path.get(tmp));
//			tmp = path.get(tmp);
//			if (tmp == end)
//				break;
//		}
		this.state = MoveState.Moving;
		this.currentGoal = path.get(start);
		this.tilemap = tilemap;

	}

	@Override
	public void handle(int entity)
	{
		OrderableC order = Game.getCurrentGame().getEntityManager().getComponent(entity, OrderableC.class);
		if (!order.isinit)
		{

			order.btree = BTreeParser.readInBTree(BtreePath, entity);
			order.isinit = true;

			Game.getCurrentGame().getEventManager().fire(new OrderToPointEvent(entity, target));
		}

		if (order.btree != null)
		{
			order.btree.step();
		}
	}

	@Override
	public boolean isFinished()
	{
		return isFinished;
	}

	@Override
	public void finish()
	{
		Game.getCurrentGame().getEventManager().fire(new OrderFinishedEvent(entityID,this));
		isFinished = true;
	}
}
