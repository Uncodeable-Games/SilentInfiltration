package de.mih.core.game.ai.btree.tasks;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;

import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.game.ai.orders.MoveOrder;
import de.mih.core.game.components.OrderableC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.VelocityC;

public class MoveToTile_Task extends LeafTask<Integer> {


	@Override
	public void run(Integer object) {
		EntityManager entityM = EntityManager.getInstance();
		
		VelocityC vel = entityM.getComponent(object, VelocityC.class);
		PositionC pos = entityM.getComponent(object, PositionC.class);
		MoveOrder order =(MoveOrder) entityM.getComponent(object, OrderableC.class).currentorder;
		
		
		PositionC endpos = entityM.getComponent(order.tilemap.getTileAt(order.target.x, order.target.z), PositionC.class);
		
		int tmp = order.tilemap.getTileAt(order.target.x, order.target.z);
		
		float dist = entityM.getComponent(tmp, PositionC.class).position.dst2(pos.position);
		
		while(order.path.get(tmp) != null && dist > 0.001 && order.path.get(tmp) != order.tilemap.getTileAt(pos.position.x, pos.position.z)){
			tmp = order.path.get(tmp);
			dist = entityM.getComponent(tmp, PositionC.class).position.dst2(pos.position);
		}
		
		PositionC pos2 = entityM.getComponent(tmp,PositionC.class);
		
		vel.velocity.x = pos2.position.x - pos.position.x;
		vel.velocity.y = pos2.position.y - pos.position.y;
		vel.velocity.z = pos2.position.z - pos.position.z;
		vel.velocity.setLength(vel.maxspeed);
		
		if (order.tilemap.getTileAt(pos.position.x, pos.position.z) == order.tilemap.getTileAt(endpos.position.x, endpos.position.z)){
			order = null;
			vel.velocity.setZero();
			success();
		}
		
		
	}

	@Override
	protected Task<Integer> copyTo(Task<Integer> task) {
		task = this;
		return task;
	}

}
