package de.mih.core.game.ai.btree.tasks;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.math.Vector2;
import de.mih.core.engine.ai.navigation.NavPoint;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.game.Game;
import de.mih.core.game.ai.orders.MoveOrder;
import de.mih.core.game.ai.orders.MoveOrder.MoveState;
import de.mih.core.game.components.OrderableC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.VelocityC;

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
		if(order.isFinished())
			order.state = MoveOrder.MoveState.Aborted;
		if (last == null && order.path.size() > 0)
		{
			last = order.path.get(order.path.size() - 1);
			order.path.remove(last);
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
					start = order.path.get(0);
					order.path.remove(0);
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
					if (!order.path.isEmpty())
					{
						if (next == order.path.get(0))
						{
							order.path.remove(0);
						}
					}
					if (order.path.isEmpty())
					{
						movetarget = last.pos;
					}
					else
					{
						next = next.router.get(order.path.get(0)).nav;
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
