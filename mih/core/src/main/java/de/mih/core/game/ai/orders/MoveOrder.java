package de.mih.core.game.ai.orders;

import java.util.Map;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Event;

import de.mih.core.engine.ai.BTreeParser;
import de.mih.core.engine.ai.BaseOrder;
import de.mih.core.engine.ai.navigation.NavPoint;
import de.mih.core.engine.ai.navigation.Pathfinder;
import de.mih.core.engine.ai.navigation.Pathfinder.Path;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.ecs.EventManager;
import de.mih.core.engine.ecs.events.BaseEvent;
import de.mih.core.engine.tilemap.Tile;
import de.mih.core.engine.tilemap.Tilemap;
import de.mih.core.game.Game;
import de.mih.core.game.components.OrderableC;
import de.mih.core.game.events.order.OrderFinishedEvent;
import de.mih.core.game.events.order.OrderToPointEvent;

public class MoveOrder extends BaseOrder {

	static String BtreePath = "assets/btrees/movetotile.tree";

	public Path path;
	public MoveState state;
	boolean isFinished = false;

	public enum MoveState
	{
		Moving,
		NodeReached,
		MoveToGoal,
		GoalReached,
		Finished
	}

	public MoveOrder(Path path) {
		this.path = path;
		this.state = MoveState.Moving;
	}

	@Override
	public void handle(int entity) {
		OrderableC order = Game.getCurrentGame().getEntityManager().getComponent(entity, OrderableC.class);
		if (!order.isinit) {

			order.btree = BTreeParser.readInBTree(BtreePath, entity);
			order.isinit = true;
			Vector3 target = new Vector3(path.get(path.size()-1).pos.x,0,path.get(path.size()-1).pos.y);
			Game.getCurrentGame().getEventManager().fire(new OrderToPointEvent(entity, target));
		}

		if (order.btree != null) {
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
