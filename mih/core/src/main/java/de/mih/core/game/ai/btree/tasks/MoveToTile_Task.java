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
	NavPoint next;
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

		switch (order.state)
		{
			case Moving:
				if (next == null)
				{
					next = order.path.remove(0);
				}

				movetarget.set(next.getPos().x, next.getPos().y);

				if (next.getPos().dst2(pos.getX(), pos.getZ()) < 0.02f)
				{	
					if (!order.path.isEmpty())
						next = order.path.remove(0);
					else {
						order.state = MoveState.GoalReached;
						vel.velocity.setZero();
						return;
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
