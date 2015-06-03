package de.silentinfiltration.game.ai.behaviourtree;

import java.util.HashMap;
import java.util.Map;

import de.silentinfiltration.engine.ai.Node;
import de.silentinfiltration.engine.ai.behaviourtree.Blackboard;
import de.silentinfiltration.engine.ai.behaviourtree.LeafTask;
import de.silentinfiltration.engine.exceptions.ComponentNotFoundEx;
import de.silentinfiltration.engine.tilemap.Tile;
import de.silentinfiltration.game.components.PositionC;
import de.silentinfiltration.game.components.VelocityC;


public class MoveToTileTask extends LeafTask {

	Node tempgoal;


	public MoveToTileTask(Blackboard bb) {
		super(bb);
	}


	public boolean CheckConditions() {
		return true;
	}


	public void DoAction() {

		if (bb.map.getTileAt((int) bb.pos.position.x, (int) bb.pos.position.y) == bb.destination) {
			control.FinishWithSuccess();
			return;
		}

		bb.currentPath = bb.pf.findShortesPath(bb.map.getTileAt(
				(int) bb.pos.position.x, (int) bb.pos.position.y),
				bb.destination);

		tempgoal = bb.destination;

		while (bb.currentPath.get(tempgoal) != bb.map.getTileAt(
				(int) bb.pos.position.x, (int) bb.pos.position.y)) {
			tempgoal = bb.currentPath.get(tempgoal);
		}

		float tempx = tempgoal.x - bb.pos.position.x;
		float tempy = tempgoal.y - bb.pos.position.y;

		bb.vel.velocity.x = (float) (tempx
				/ (Math.sqrt(tempx * tempx + tempy * tempy)) * bb.vel.maxspeed);
		bb.vel.velocity.y = (float) (tempy
				/ (Math.sqrt(tempx * tempx + tempy * tempy)) * bb.vel.maxspeed);
	}


	public void End() {
		
	}

	public void Start() throws ComponentNotFoundEx {
		bb.pos = bb.entityM.getComponent(bb.entity, PositionC.class);
		bb.vel = bb.entityM.getComponent(bb.entity, VelocityC.class);
	}
}