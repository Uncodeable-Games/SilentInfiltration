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

	public void DoAction(double dt) {

		//TODO: add check for center of tile
		if (bb.map.getTileAt((int) bb.pos.position.x, (int) bb.pos.position.y) == bb.destination) {
			control.FinishWithSuccess();
			return;
		}

//		bb.currentPath = bb.pf.findShortesPath(bb.map.getTileAt(
//				(int) bb.pos.position.x, (int) bb.pos.position.y),
//				bb.destination);

		
		if(bb.currentNode.equals(bb.map.getTileAt(
				(int) bb.pos.position.x, (int) bb.pos.position.y)))
		{
			bb.currentNode = bb.currentPath.get(bb.currentNode);
		}
		//System.out.println(tempgoal);
//		while (bb.currentPath.containsKey(tempgoal) && !bb.currentPath.get(tempgoal).equals(bb.map.getTileAt(
//				(int) bb.pos.position.x, (int) bb.pos.position.y))) {
//			System.out.println(bb.currentPath.containsKey(tempgoal));
//
//			tempgoal = bb.currentPath.get(tempgoal);
//		}
		//bb.currentNode = tempgoal;

		float tempx = bb.currentNode.x - bb.pos.position.x;
		float tempy = bb.currentNode.y - bb.pos.position.y;

		bb.vel.velocity.x = (float) (tempx
				/ (Math.sqrt(tempx * tempx + tempy * tempy)) * bb.vel.maxspeed ) ;
		bb.vel.velocity.y = (float) (tempy
				/ (Math.sqrt(tempx * tempx + tempy * tempy)) * bb.vel.maxspeed );
	}

	public void End() {

	}

	public void Start() throws ComponentNotFoundEx {
		bb.pos = bb.entityM.getComponent(bb.entity, PositionC.class);
		bb.vel = bb.entityM.getComponent(bb.entity, VelocityC.class);
	}
}