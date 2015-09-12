package de.mih.core.game.ai.btree.tasks;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.math.Vector3;

import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.tilemap.Tile;
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
		
		Tile endPos = order.tilemap.getTileAt((int)order.target.x, (int)order.target.z);
		Tile tmp = order.tilemap.getTileAt((int)order.target.x, (int)order.target.z);
		
		float dist = tmp.getCenter().dst(endPos.getCenter());
		
		while(order.path.get(tmp) != null && dist > 0.001 && order.path.get(tmp) != order.tilemap.getTileAt((int)pos.position.x, (int)pos.position.z)){
			tmp = order.path.get(tmp);
			dist = tmp.getCenter().dst(pos.position);
		}
		
		Vector3 pos2 = tmp.getCenter();
		
		vel.velocity.x = pos2.x - pos.position.x;
		vel.velocity.y = pos2.y - pos.position.y;
		vel.velocity.z = pos2.z - pos.position.z;
		vel.velocity.setLength(vel.maxspeed);
		
		if (order.tilemap.getTileAt((int)pos.position.x, (int)pos.position.z) == order.tilemap.getTileAt((int)endPos.getCenter().x, (int)endPos.getCenter().z)){
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
