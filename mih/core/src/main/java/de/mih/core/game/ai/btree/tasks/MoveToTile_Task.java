package de.mih.core.game.ai.btree.tasks;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.steer.utils.Path;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import de.mih.core.engine.ai.navigation.NavPoint;
import de.mih.core.engine.ai.navigation.Pathfinder;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.tilemap.Tile;
import de.mih.core.game.Game;
import de.mih.core.game.ai.orders.MoveOrder;
import de.mih.core.game.ai.orders.MoveOrder.MoveState;
import de.mih.core.game.components.OrderableC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.VelocityC;
import de.mih.core.game.events.order.OrderFinishedEvent;

public class MoveToTile_Task extends LeafTask<Integer>
{

	Vector2 movetarget = new Vector2();
	NavPoint start;
	NavPoint next;
	NavPoint last;
	boolean finished = false;

	@Override
	public void run(Integer object)
	{
		//
		// if(finished)
		// return;
		EntityManager entityM = Game.getCurrentGame().getEntityManager();

		VelocityC vel = entityM.getComponent(object, VelocityC.class);
		PositionC pos = entityM.getComponent(object, PositionC.class);
		MoveOrder order = (MoveOrder) entityM.getComponent(object, OrderableC.class).currentorder;

		if (last == null)
		{
			last = order.path.navpoints.get(order.path.navpoints.size() - 1);
			order.path.navpoints.remove(last);
		}
		switch (order.state)
		{
			case Moving:
				if (last.pos.dst2(pos.getPos().x, pos.getPos().z) < 0.02f)
				{
					order.state = MoveState.GoalReached;
					
					// Game.getCurrentGame().getEventManager().fire(new
					// OrderFinishedEvent(object, order));
					vel.velocity.setZero();
					// entityM.getComponent(object,
					// OrderableC.class).currentorder = null;
					return;
				}

				if (start == null)
				{
					start = order.path.navpoints.get(0);
					order.path.navpoints.remove(0);
				}
				if (next == null)
				{
					next = start;
				}

				if (movetarget != last.pos)
				{
					movetarget.set(next.pos.x, next.pos.y);
				}

				if (next.pos.dst2(pos.getX(), pos.getZ()) < 0.02f && !(movetarget == last.pos))
				{
					if (!order.path.navpoints.isEmpty())
					{
						if (next == order.path.navpoints.get(0))
						{
							order.path.navpoints.remove(0);
						}
					}
					if (order.path.navpoints.isEmpty())
					{
						movetarget = last.pos;
					}
					else
					{
						next = next.router.get(order.path.navpoints.get(0)).nav;
						movetarget.set(next.pos.x, next.pos.y);
					}
				}

				vel.velocity.x = movetarget.x - pos.getX();
				vel.velocity.z = movetarget.y - pos.getZ();
				vel.velocity.setLength(vel.maxspeed);
				break;
			case GoalReached:
				success();
				order.finish();
				order.state = MoveState.Finished;
				break;
			default:
				break;
		}

	}

	@Override
	protected Task<Integer> copyTo(Task<Integer> task)
	{
		task = this;
		return task;
	}

}
