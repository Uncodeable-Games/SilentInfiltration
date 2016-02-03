package de.mih.core.engine.ai.navigation.pathfinder;

import java.util.ArrayList;
import com.badlogic.gdx.math.Vector3;
import de.mih.core.engine.ai.navigation.NavPoint;
import de.mih.core.engine.ai.navigation.NavPoint.Tuple;
import de.mih.core.engine.ai.navigation.NavigationManager;
import de.mih.core.engine.ai.navigation.pathfinder.PathGenerator.AStar;
import de.mih.core.engine.ai.navigation.pathfinder.PathGenerator.Node;
import de.mih.core.engine.tilemap.Door;
import de.mih.core.engine.tilemap.Room;
import de.mih.core.engine.tilemap.TileBorder;
import de.mih.core.engine.tilemap.Tilemap;
import de.mih.core.game.Game;

public class Pathfinder {

	
	private AStar aStar = new AStar();
	
	/**
	 * Generates a path from v_start to v_end
	 * 
	 * @param v_start
	 * @param v_end
	 * @return
	 */
	public Path getPath(Vector3 v_start, Vector3 v_end) {

		Room startroom = Game.getCurrentGame().getTilemap().getRoomAt(v_start.x, v_start.z);
		Room endroom = Game.getCurrentGame().getTilemap().getRoomAt(v_end.x, v_end.z);

		if (startroom == null || endroom == null) {
			System.out.println("startroom or endroom are null");
			return Path.NOPATH;
		}

		// Create NavPoints where the Unit starts and where it wants to go and
		// calculate the Visibility (They need to be added to the Room NavPoints
		// so they can see each other, but needed to be removed after 'cause
		// they should be ignored by other checks)
		NavPoint first = new NavPoint(v_start.x, v_start.z);
		NavPoint last = new NavPoint(v_end.x, v_end.z);
		Game.getCurrentGame().getNavigationManager().get(startroom).add(first);
		Game.getCurrentGame().getNavigationManager().get(endroom).add(last);
		initNavPoint(first);
		initNavPoint(last);
		Game.getCurrentGame().getNavigationManager().get(startroom).remove(first);
		Game.getCurrentGame().getNavigationManager().get(endroom).remove(last);

		// If target is in line of sight return direct Path;
		if (first.isVisibleBy(last)) {
			Path tmp = new Path();
			tmp.add(first);
			tmp.add(last);
			return tmp;
		}
		return convertNodeListToPath(aStar.generatePath(first, last));
	}

	private Path convertNodeListToPath(ArrayList<Node> nodelist) {
		
		if (nodelist.isEmpty()) return Path.getNoPath();
		
		Path path = new Path();
		path.add((NavPoint) nodelist.remove(0));
		Room curroom = path.get(0).getRoom();
		for (int i = 1; i < nodelist.size() - 2; i++) {
			NavPoint nav1 = Game.getCurrentGame().getNavigationManager().getDoorNavPointByRoom((Door) nodelist.get(i),
					curroom);
			NavPoint nav2 = Game.getCurrentGame().getNavigationManager()
					.getDoorNavPointbyPartner((Door) nodelist.remove(i), nav1);
			path.add(nav1);
			path.add(nav2);
			curroom = nav2.getRoom();
		}

		System.out.println(nodelist);
		
		NavPoint last = (NavPoint) nodelist.get(0);
		path.add(last.getNextNavPoint(path.get(path.size() - 1)));
		path.add(last);
		return Path.NOPATH;
	}

	private void initNavPoint(NavPoint nav) {
		nav.calculateVisibility();
		for (NavPoint neigbour : nav.getVisibleNavPoints()) {
			for (NavPoint target : neigbour.getReachableNavPoints()) {
				if (!nav.isReachableBy(target) || nav.getDistanceToNavPoint(target) > nav.getDistance(neigbour)
						+ neigbour.getDistance(target)) {
					nav.addToRouter(target, new Tuple(neigbour,nav.getDistance(neigbour)
							+ neigbour.getDistance(target)));
				}
			}
		}
	}
}
