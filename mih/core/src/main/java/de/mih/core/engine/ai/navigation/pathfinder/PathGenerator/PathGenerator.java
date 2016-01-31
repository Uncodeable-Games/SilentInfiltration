package de.mih.core.engine.ai.navigation.pathfinder.PathGenerator;

import java.util.ArrayList;

import de.mih.core.engine.ai.navigation.NavPoint;
import de.mih.core.engine.ai.navigation.pathfinder.PathGenerator.Paths.DoorPath;
import de.mih.core.engine.ai.navigation.pathfinder.PathGenerator.Paths.Path;
import de.mih.core.engine.tilemap.Room;
import de.mih.core.engine.tilemap.TileBorder;
import de.mih.core.game.Game;

public class PathGenerator {
//
//	AStar<DoorPath, TileBorder> aStarDoor = new AStar<DoorPath, TileBorder>(DoorPath.class);
//
//	/**
//	 * Generates the shortest path from one door to another across rooms using
//	 * the A*-Algorithm. WARNING: This is not a usable path for orders! It has
//	 * to be converted before use.
//	 * 
//	 * @param startdoor
//	 * @param enddoor
//	 * @return
//	 */
//	public DoorPath generatePath(TileBorder startdoor, TileBorder enddoor) {
//		return aStarDoor.generatePath(startdoor, enddoor);
//	}
//
//	/**
//	 * Generates all possible paths across all rooms from the 'startroom' to the
//	 * 'endroom' WARNING: These are not usable paths for orders! They have to be
//	 * converted before use.
//	 * 
//	 * @param startroom
//	 * @param endroom
//	 * @return A list with all possible paths
//	 */
//	public ArrayList<DoorPath> generatePathsAcrossRooms(Room startroom, Room endroom) {
//		ArrayList<DoorPath> doorPaths = new ArrayList<DoorPath>();
//		for (TileBorder startdoor : startroom.allDoors) {
//			for (TileBorder enddoor : endroom.allDoors) {
//				DoorPath path = generatePath(startdoor, enddoor);
//				if (path != DoorPath.NOPATH) {
//					path.start = Game.getCurrentGame().getNavigationManager().getDoorNavPointByRoom(startdoor,
//							startroom);
//					path.end = Game.getCurrentGame().getNavigationManager().getDoorNavPointByRoom(enddoor, endroom);
//					doorPaths.add(path);
//				}
//			}
//		}
//		return doorPaths;
//	}
//
//	/**
//	 * Returns the shortest path inside a room from 'first' to 'last'. If the
//	 * navpoints aren't in the same room the NOPATH gets returned.
//	 * 
//	 * @param start
//	 *            The starting NavPoint
//	 * @param end
//	 *            The targeted NavPoint
//	 * @return The shortest path
//	 */
//	public Path generatePathInsideRoom(NavPoint start, NavPoint end) {
//		if (start.getRoom() != end.getRoom())
//			return Path.NOPATH;
//		NavPoint min1 = start.getVisibleNavPoints().get(0);
//		NavPoint min2 = end.getVisibleNavPoints().get(0);
//		for (NavPoint nav1 : start.getVisibleNavPoints()) {
//			for (NavPoint nav2 : end.getVisibleNavPoints()) {
//				if (start.getDistance(nav1) + nav1.getDistance(nav2) + end.getDistance(nav2) < start.getDistance(min1)
//						+ min1.getDistance(min2) + end.getDistance(min2)) {
//					min1 = nav1;
//					min2 = nav2;
//				}
//			}
//		}
//		Path path = new Path();
//		path.add(min1);
//		path.add(min2);
//		path.add(end);
//		path.distance = start.getDistance(min1) + min1.getDistance(min2) + end.getDistance(min2);
//		return path;
//
//	}
}
