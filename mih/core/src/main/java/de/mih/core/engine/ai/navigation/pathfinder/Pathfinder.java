package de.mih.core.engine.ai.navigation.pathfinder;

import java.util.ArrayList;
import com.badlogic.gdx.math.Vector3;
import de.mih.core.engine.ai.navigation.NavPoint;
import de.mih.core.engine.ai.navigation.NavigationManager;
import de.mih.core.engine.ai.navigation.pathfinder.PathGenerator.PathGenerator;
import de.mih.core.engine.ai.navigation.pathfinder.PathGenerator.Paths.DoorPath;
import de.mih.core.engine.ai.navigation.pathfinder.PathGenerator.Paths.Path;
import de.mih.core.engine.tilemap.Room;
import de.mih.core.engine.tilemap.TileBorder;
import de.mih.core.engine.tilemap.Tilemap;
import de.mih.core.game.Game;

public class Pathfinder {

	public PathGenerator pathGenerator = new PathGenerator();

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

		if (startroom == null || endroom == null){
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
		first.calculateVisibility(startroom);
		last.calculateVisibility(endroom);
		Game.getCurrentGame().getNavigationManager().get(startroom).remove(first);
		Game.getCurrentGame().getNavigationManager().get(endroom).remove(last);

		// If target is in line of sight return direct Path;
		if (first.isVisibleBy(last)) {
			Path tmp = new Path();
			tmp.add(first);
			tmp.add(last);
			tmp.distance = first.getDistance(last);
			return tmp;
		}

		// Get the shortest Path through doors to the target
		DoorPath min = getShortestDoorPath(first, last,
				getShortestDoorPaths(first, last, pathGenerator.generatePathsAcrossRooms(startroom, endroom)));

		// If there's no Path to the target or the startroom and the endroom are
		// the same ...
		if (min == DoorPath.NOPATH || startroom == endroom) {
			Path path = Path.NOPATH;

			if (startroom == endroom) {
				// if the startroom and the endroom are the same room generate a
				// possible Path to the target
				path = pathGenerator.generatePathInsideRoom(first, last);
				// and if there's no possible Path across other rooms return
				// this path
				if (min == DoorPath.NOPATH) {
					return path;
				} else {
					// otherwise check if the Path across other rooms is shorter
					// (like in U-shaped Rooms)
					if (path.distance < min.distance + first.getDistance(min.start) + last.getDistance(min.end)) {
						return path;
					}
				}
				// if there's no possible Path across other rooms and the
				// startroom and the endroom are not the same return NOPATH.
			} else {
				System.out.println("No Doorpath and startroom != endroom");
				return path;
			}
		}

		// Convert the DoorPath to a Path usable by orders.
		return min.convertToPath(last);
	}

	/**
	 * Finds the shortest path from a list of possible paths for every NavPoint
	 * visible from 'first' to every NavPoint visible from 'last'. The correlate
	 * NavPoints are geting saved in the diffent paths. WARNING: These are not
	 * usable paths for orders! They have to be converted before use.
	 * 
	 * @param first
	 * @param last
	 * @param doorPaths
	 *            A List with the paths to be checked;
	 * @return A list with the shortest paths.
	 */
	private ArrayList<DoorPath> getShortestDoorPaths(NavPoint first, NavPoint last, ArrayList<DoorPath> doorPaths) {
		ArrayList<DoorPath> shortpaths = new ArrayList<DoorPath>();
		if (doorPaths.isEmpty())
			return shortpaths;
		for (NavPoint nav1 : first.getVisibleNavPoints()) {
			for (NavPoint nav2 : last.getVisibleNavPoints()) {
				DoorPath min = getShortestDoorPath(nav1, nav2, doorPaths);

				min.distance = min.distance + nav1.getDistance(min.get(0)) + nav2.getDistance(min.get(min.size() - 1));
				min.start = nav1;
				min.end = nav2;
				shortpaths.add(min);
			}
		}
		return shortpaths;
	}

	/**
	 * Finds the shortest path from 'first' to 'last' from a list of possible
	 * paths
	 * 
	 * @param start
	 * @param end
	 * @param paths
	 *            A List with the paths to be checked;
	 * @return
	 */
	private DoorPath getShortestDoorPath(NavPoint start, NavPoint end, ArrayList<DoorPath> paths) {
		if (paths.isEmpty())
			return DoorPath.NOPATH;
		DoorPath min = paths.get(0);
		for (DoorPath path : paths) {
			if (path.distance + start.getDistance(path.start) + end.getDistance(path.end) < min.distance
					+ start.getDistance(min.start) + end.getDistance(min.end)) {
				min = path;
			}
		}
		return min;

	}

}
