package de.mih.core.engine.ai.navigation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.badlogic.gdx.math.Vector3;
import de.mih.core.engine.tilemap.Room;
import de.mih.core.engine.tilemap.TileBorder;
import de.mih.core.engine.tilemap.Tilemap;
import de.mih.core.game.Game;

public class Pathfinder {

	public static class Path {
		public ArrayList<NavPoint> path = new ArrayList<NavPoint>();
		public NavPoint start;
	}

	static class DoorPath {
		public ArrayList<TileBorder> path = new ArrayList<TileBorder>();
		float dist = Float.MAX_VALUE;
		NavPoint first;
		NavPoint last;
	}

	static class Node {
		TileBorder nav;
		Node pre;
		float value;
	}

	public Path getPath(Vector3 v_start, Vector3 v_end) {
		NavigationManager navM = Game.getCurrentGame().getNavigationManager();
		Tilemap tilemap = Game.getCurrentGame().getTilemap();

		Room startroom = tilemap.getRoomAt(tilemap.coordToIndex(v_start.x), tilemap.coordToIndex(v_start.z));
		Room endroom = tilemap.getRoomAt(tilemap.coordToIndex(v_end.x), tilemap.coordToIndex(v_end.z));

		ArrayList<TileBorder> allDoors = new ArrayList<TileBorder>();
		for (Room r : tilemap.getRooms()) {
			for (TileBorder door : r.allDoors) {
				if (!allDoors.contains(door)) {
					allDoors.add(door);
				}
			}
		}

		ArrayList<DoorPath> allDoorPaths = new ArrayList<DoorPath>();

		for (TileBorder startdoor : startroom.allDoors) {
			for (TileBorder enddoor : endroom.allDoors) {
				ArrayList<Node> openlist = new ArrayList<Node>();
				ArrayList<Node> closedlist = new ArrayList<Node>();

				Node start = new Node();
				start.nav = startdoor;
				start.pre = null;
				start.value = startdoor.getPos().dst(enddoor.getPos());
				openlist.add(start);
				Node current = null;
				while (!openlist.isEmpty()) {
					current = getMin(openlist);
					if (current.nav == enddoor)
						break;
					openlist.remove(current);
					closedlist.add(current);
					for (TileBorder door : navM.getDoorNeighbours(current.nav).keySet()) {
						if (!contains(closedlist, door) && !contains(openlist, door) && current.nav != door) {
							Node tmp = new Node();
							tmp.nav = door;
							tmp.pre = current;
							tmp.value = navM.getDoorNeighbours(current.nav).get(door) + current.value
									+ current.nav.getPos().dst(enddoor.getPos());
							openlist.add(tmp);
						}
					}
				}

				DoorPath doorpath = new DoorPath();
				Node tmp = current;
				while (tmp != start) {
					doorpath.path.add(tmp.nav);
					tmp = tmp.pre;
				}
				doorpath.path.add(start.nav);
				doorpath.dist = current.value;
				Collections.reverse(doorpath.path);
				allDoorPaths.add(doorpath);
			}
		}

		NavPoint first = new NavPoint(v_start.x, v_start.z);
		NavPoint last = new NavPoint(v_end.x, v_end.z);
		navM.get(startroom).add(first);
		navM.get(endroom).add(last);
		first.calculateVisibility(startroom);
		last.calculateVisibility(endroom);
		navM.get(startroom).remove(first);
		navM.get(endroom).remove(last);
		
		if (first.visibleNavPoints.containsKey(last)){
			Path tmp = new Path();
			tmp.path.add(first);
			tmp.path.add(last);
			return tmp;
		}

		ArrayList<DoorPath> shortestpaths = new ArrayList<DoorPath>();

		for (NavPoint nav1 : first.visibleNavPoints.keySet()) {
			for (NavPoint nav2 : last.visibleNavPoints.keySet()) {

				DoorPath min = (DoorPath) allDoorPaths.toArray()[0];
				for (DoorPath path : allDoorPaths) {
					if ((path.dist + nav1.router.get(navM.getDoorNavPointByRoom(path.path.get(0), startroom)).dist
							+ nav2.router.get(navM.getDoorNavPointByRoom(path.path.get(path.path.size() - 1),
									endroom)).dist) < (min.dist
											+ nav1.router
													.get(navM.getDoorNavPointByRoom(min.path.get(0), startroom)).dist
											+ nav2.router.get(navM.getDoorNavPointByRoom(
													min.path.get(min.path.size() - 1), endroom)).dist)) {
						min = path;
					}
				}
				min.dist = min.dist + nav1.router.get(navM.getDoorNavPointByRoom(min.path.get(0), startroom)).dist
						+ nav2.router.get(navM.getDoorNavPointByRoom(min.path.get(min.path.size() - 1), endroom)).dist;
				min.first = nav1;
				min.last = nav2;
				shortestpaths.add(min);
			}
		}
		
		DoorPath min = shortestpaths.get(0);
		for (DoorPath tmp : shortestpaths){
			if (tmp.dist + first.visibleNavPoints.get(tmp.first) + last.visibleNavPoints.get(tmp.last) < min.dist + first.visibleNavPoints.get(min.first) + last.visibleNavPoints.get(min.last)){
				min = tmp;
			}
		}
		
		Path path = new Path();
		path.path.add(min.first);
		if (startroom == endroom) {
			if (first.router.get(last).dist < (min.dist
					+ first.router.get(navM.getDoorNavPointByRoom(min.path.get(0), startroom)).dist
					+ last.router.get(navM.getDoorNavPointByRoom(min.path.get(min.path.size() - 1), endroom)).dist)) {
				path.path.add(last);
				return path;
			}
		}
		Room curroom = startroom;
		for (TileBorder door : min.path) {
			NavPoint nav1 = navM.getDoorNavPointByRoom(door, curroom);
			NavPoint nav2 = navM.getDoorNavPointbyPartner(door, nav1);
			path.path.add(nav1);
			path.path.add(nav2);
			curroom = tilemap.getRoomAt(tilemap.coordToIndex(nav2.pos.x), tilemap.coordToIndex(nav2.pos.y));
		}
		path.path.add(min.last);
		path.path.add(last);
		return path;
	}

	private Node getMin(ArrayList<Node> list) {
		Node min = (Node) list.toArray()[0];
		for (Node node : list) {
			if (node.value < min.value)
				min = node;
		}
		return min;
	}

	private boolean contains(ArrayList<Node> list, TileBorder door) {
		for (Node n : list) {
			if (n.nav == door)
				return true;
		}
		return false;
	}
}
