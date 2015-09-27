package de.mih.core.engine.ai.navigation;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;

import de.mih.core.engine.GameStates.GameStateManager;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.tilemap.Room;
import de.mih.core.game.MiH;
import de.mih.core.game.components.ColliderC;
import de.mih.core.game.components.VelocityC;

public class Pathfinder {

	NavPoint first = new NavPoint();
	NavPoint last = new NavPoint();

	ArrayList<ColliderC> allcolliders = new ArrayList<ColliderC>();

	public NavPoint[] getPath(Vector3 start, Vector3 end) {
		Room r = GameStateManager.getInstance().getCurrentGame().tilemap.getRoomAt(GameStateManager.getInstance().getCurrentGame().tilemap.coordToIndex_x(start.x), GameStateManager.getInstance().getCurrentGame().tilemap.coordToIndex_z(start.z));

		first.pos.set(start.x, start.z);
		last.pos.set(end.x, end.z);

		for (Integer i : r.entitiesInRoom) {
			if (EntityManager.getInstance().hasComponent(i, ColliderC.class)
					&& !EntityManager.getInstance().hasComponent(i, VelocityC.class)) {
				allcolliders.add(EntityManager.getInstance().getComponent(i, ColliderC.class));
			}
		}

		boolean intersecting = false;
		for (ColliderC col2 : allcolliders) {
			if (NavPoint.LineIntersectsRect(first.pos, last.pos, col2.getNavCollider())) {
				intersecting = true;
				break;
			}
		}
		if (!intersecting) {
			NavPoint[] path = { last, last };
			return path;

		}

		first.calculateVisibility(r);
		last.calculateVisibility(r);

		NavPoint[] path = new NavPoint[2];
		path[0] = (NavPoint) first.visibleNavPoints.keySet().toArray()[0];
		path[1] = (NavPoint) last.visibleNavPoints.keySet().toArray()[0];
		for (NavPoint n1 : first.visibleNavPoints.keySet()) {
			for (NavPoint n2 : last.visibleNavPoints.keySet()) {
				if (n1.router.get(n2).dist + first.visibleNavPoints.get(n1)
						+ last.visibleNavPoints.get(n2) < path[0].router.get(path[1]).dist
								+ first.visibleNavPoints.get(path[0]) + last.visibleNavPoints.get(path[1])) {
					path[0] = n1;
					path[1] = n2;
				}
			}
		}
		return path;
	}
}
