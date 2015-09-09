package de.mih.core.engine.ai.btree.tasks;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;

import de.mih.core.engine.ai.btree.Unit;
import de.mih.core.engine.ai.orders.MoveOrder;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.VelocityC;

public class MoveToTile_Task extends LeafTask<Unit> {

	@Override
	protected Task<Unit> copyTo(Task<Unit> arg0) {
		return null;
	}

	@Override
	public void run(Unit arg0) {
		int e =arg0.getEntity();
		VelocityC vel = arg0.entityM.getComponent(e, VelocityC.class);
		PositionC pos = arg0.entityM.getComponent(e, PositionC.class);
		MoveOrder mo = (MoveOrder) arg0.currentorder;
		PositionC endpos = arg0.entityM.getComponent(mo.end, PositionC.class);
		
		int tmp = mo.end;
		
		float dist = arg0.entityM.getComponent(tmp, PositionC.class).position.dst2(pos.position);
		
		while(mo.path.get(tmp) != null && dist > 0.001 && mo.path.get(tmp) != mo.tilemap.getTileAt(pos.position.x, pos.position.z)){
			tmp = mo.path.get(tmp);
			dist = arg0.entityM.getComponent(tmp, PositionC.class).position.dst2(pos.position);
		}
		
		PositionC pos2 = arg0.entityM.getComponent(tmp,PositionC.class);
		
		vel.velocity.x = pos2.position.x - pos.position.x;
		vel.velocity.y = pos2.position.y - pos.position.y;
		vel.velocity.z = pos2.position.z - pos.position.z;
		vel.velocity.setLength(vel.maxspeed);
		
		if (mo.tilemap.getTileAt(pos.position.x, pos.position.z) == mo.tilemap.getTileAt(endpos.position.x, endpos.position.z)){
			arg0.currentorder = null;
			vel.velocity.setZero();
			success();
		}
		
		
	}

}
