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
import de.mih.core.game.components.OrderableC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.VelocityC;

public class MoveToTile_Task extends LeafTask<Integer> {

	Vector2 movetarget = new Vector2();
	NavPoint next;
	NavPoint last;

	@Override
	public void run(Integer object) {

		EntityManager entityM = Game.getCurrentGame().getEntityManager();

		VelocityC vel = entityM.getComponent(object, VelocityC.class);
		PositionC pos = entityM.getComponent(object, PositionC.class);
		MoveOrder order = (MoveOrder) entityM.getComponent(object, OrderableC.class).currentorder;

		if (last == null){
			last = order.path.path.get(order.path.path.size() -1);
			order.path.path.remove(last);
		}
		
		if (last.pos.dst2(pos.getPos().x,pos.getPos().z) < 0.02f) {
			success();
			vel.velocity.setZero();
			entityM.getComponent(object, OrderableC.class).currentorder = null;
			return;
		}

		if (order.path.start == null) {
			order.path.start = order.path.path.get(0);
			order.path.path.remove(0);
		}
		if (next == null) {
			next = order.path.start;
		}

		if (movetarget != last.pos) {
			movetarget.set(next.pos.x,next.pos.y);
		}

		if (next.pos.dst2(pos.getX(), pos.getZ()) < 0.02f && !(movetarget == last.pos)) {
			if (!order.path.path.isEmpty()) {
				if (next == order.path.path.get(0)) {
					order.path.path.remove(0);
				}
			}
			if (order.path.path.isEmpty()) {
				movetarget = last.pos;
			} else {
				next = next.router.get(order.path.path.get(0)).nav;
				movetarget.set(next.pos.x, next.pos.y);
			}
		}

		vel.velocity.x = movetarget.x - pos.getX();
		vel.velocity.z = movetarget.y - pos.getZ();
		vel.velocity.setLength(vel.maxspeed);
	}

	@Override
	protected Task<Integer> copyTo(Task<Integer> task) {
		task = this;
		return task;
	}

}
