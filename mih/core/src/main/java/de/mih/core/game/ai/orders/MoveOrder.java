package de.mih.core.game.ai.orders;

import com.badlogic.gdx.math.Vector2;
import de.mih.core.engine.ai.BaseOrder;
import de.mih.core.engine.ai.navigation.NavPoint;
import de.mih.core.engine.ai.navigation.pathfinder.Path;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.game.Game;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.VelocityC;
import de.mih.core.game.events.order.OrderFinishedEvent;

public class MoveOrder extends BaseOrder
{

	static String BtreePath = "assets/btrees/movetotile.tree";

	public Path      path;
	public MoveState state;
	boolean isFinished = false;
	boolean isStopped  = false;
	NavPoint next;
	Vector2 movetarget = new Vector2();

	public enum MoveState
	{
		Moving,
		NodeReached,
		MoveToGoal,
		GoalReached,
		Finished,
		Stopped,
	}

	public MoveOrder(Path path)
	{
		this.path = path;
		this.state = MoveState.Moving;
	}

	@Override
	public void handle()
	{//int entity) {
		EntityManager entityM = Game.getCurrentGame().getEntityManager();

		VelocityC vel = entityM.getComponent(this.entityID, VelocityC.class);
		PositionC pos = entityM.getComponent(this.entityID, PositionC.class);

		switch (this.state)
		{
			case Moving:
				if (next == null)
				{
					next = this.path.remove(0);
				}

				movetarget.set(next.getPos().x, next.getPos().y);

				if (next.getPos().dst2(pos.getX(), pos.getZ()) < 0.02f)
				{
					if (!this.path.isEmpty())
						next = this.path.remove(0);
					else
					{
						this.state = MoveState.GoalReached;
						vel.velocity.setZero();
						return;
					}
				}

				vel.velocity.x = movetarget.x - pos.getX();
				vel.velocity.z = movetarget.y - pos.getZ();
				vel.velocity.setLength(vel.maxspeed);
				break;
			case GoalReached:
				this.finish();
				this.state = MoveState.Finished;
				break;
			default:
				break;
		}
		
		/*if (!order.isinit) {

			order.btree = BTreeParser.readInBTree(BtreePath, entity);
			order.isinit = true;
			Vector3 target = new Vector3(path.getNavPoints(path.size()-1).getPos().x,0,path.getNavPoints(path.size()-1).getPos().y);
			Game.getCurrentGame().getEventManager().fire(new OrderToPointEvent(entity, target));
		}

		if (order.btree != null) {
			order.btree.step();
		}*/
	}

	@Override
	public boolean isFinished()
	{
		return isFinished;
	}

	@Override
	public void finish()
	{

		Game.getCurrentGame().getEventManager().fire(new OrderFinishedEvent(entityID,this.getID()));
		isFinished = true;
	}
	
	@Override
	public void stop()
	{
		this.state = MoveState.Stopped;
		this.isStopped = true;
		//Fire OrderStopped?
	}
	
	@Override
	public boolean isStopped()
	{
		return isStopped;
	}
}
