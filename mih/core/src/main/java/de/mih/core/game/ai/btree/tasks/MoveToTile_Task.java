package de.mih.core.game.ai.btree.tasks;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.steer.utils.Path;
import com.badlogic.gdx.math.Vector3;

import de.mih.core.engine.ai.navigation.NavPoint;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.tilemap.Tile;
import de.mih.core.game.ai.orders.MoveOrder;
import de.mih.core.game.components.OrderableC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.VelocityC;

public class MoveToTile_Task extends LeafTask<Integer> {

	Vector3 movetarget = new Vector3();

	@Override
	public void run(Integer object) {

		EntityManager entityM = EntityManager.getInstance();

		VelocityC vel = entityM.getComponent(object, VelocityC.class);
		PositionC pos = entityM.getComponent(object, PositionC.class);
		MoveOrder order = (MoveOrder) entityM.getComponent(object, OrderableC.class).currentorder;

		
		if (order.target.dst2(pos.getPos()) < 0.1f) {
			//System.out.println("succsess");
			success();
			vel.velocity.setZero();
			entityM.getComponent(object, OrderableC.class).currentorder = null;
			return;
		}
		
		if (movetarget != order.target) {
			movetarget.set(order.path[0].pos.x, 0, order.path[0].pos.y);
		}

		if (order.path[0].pos.dst2(pos.getX(), pos.getZ()) < 0.1f && !(movetarget == order.target)) {
			if (order.path[0] == order.path[1]) {
				//System.out.println("movint to end");
				movetarget = order.target;
			} else {
				movetarget.set(order.path[0].pos.x, 0, order.path[0].pos.y);
			}
			order.path[0] = order.path[0].router.get(order.path[1]).nav;
		}

		vel.velocity.x = movetarget.x - pos.getX();
		vel.velocity.y = movetarget.y - pos.getY();
		vel.velocity.z = movetarget.z - pos.getZ();
		vel.velocity.setLength(vel.maxspeed);
	}

	@Override
	protected Task<Integer> copyTo(Task<Integer> task) {
		task = this;
		return task;
	}

}
