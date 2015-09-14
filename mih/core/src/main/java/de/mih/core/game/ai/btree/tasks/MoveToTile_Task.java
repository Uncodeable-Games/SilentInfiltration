package de.mih.core.game.ai.btree.tasks;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.steer.utils.Path;
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
		MoveOrder order = (MoveOrder) entityM.getComponent(object, OrderableC.class).currentorder;
		
		Tile endPos = order.tilemap.getTileAt((int)order.target.x, (int)order.target.z);
		Tile currentTile = order.tilemap.getTileAt(order.tilemap.coordToIndex_x((int)pos.position.x), order.tilemap.coordToIndex_z((int)pos.position.z));

		Tile tmp = currentTile;
		float dist = pos.position.dst(endPos.getCenter());

		Vector3 pos2 = order.target;
		
		if(order.path.containsKey(currentTile))
		{
			tmp = order.path.get(currentTile);
			pos2 = tmp.getCenter();
		}
//		while(order.path.containsKey(currentTile) && dist > 0.001 && order.path.get(tmp) != currentTile){
//			tmp = order.path.get(tmp);
//			dist = tmp.getCenter().dst(pos.position);
//		}
//		desired_velocity = normalize(target - position) * max_velocity
//				steering = desired_velocity - velocity
		Vector3 desiredVel = new Vector3(pos2);
		desiredVel.sub(pos.position).nor().scl(vel.maxspeed);
		
		vel.velocity.x = pos2.x - pos.position.x;
		vel.velocity.y = pos2.y - pos.position.y;
		vel.velocity.z = pos2.z - pos.position.z;
		//vel.velocity.clamp(-vel.maxspeed, vel.maxspeed);
		vel.velocity.setLength(vel.maxspeed);
		
		vel.steering = desiredVel.sub(vel.velocity);
		

		//vel.velocity.setLength(vel.maxspeed);
		
		if (dist <= 0.1 || vel.velocity.len() <= 1)// order.tilemap.getTileAt((int)pos.position.x, (int)pos.position.z) == order.end)
		{
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
