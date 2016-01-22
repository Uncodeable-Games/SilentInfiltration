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

	public class Path {
		public ArrayList<NavPoint> path = new ArrayList<NavPoint>();
		public NavPoint start;
	}

	class DoorPath {
		public ArrayList<TileBorder> path = new ArrayList<TileBorder>();
		float dist = Float.MAX_VALUE;
		NavPoint first;
		NavPoint last;
	}

	class Node {
		TileBorder nav;
		Node pre;
		float value;
	}

	private NavPoint first;
	private NavPoint last;
	private NavigationManager navM;
	private Tilemap tilemap;
	private Room startroom;
	private Room endroom;

	private ArrayList<TileBorder> possibleDoors;
	private ArrayList<DoorPath> allDoorPaths;
	private ArrayList<DoorPath> shortestpaths;

	private ArrayList<TileBorder> getPossibleDoors() {
		HashMap<Room, Integer> countimpossibleDoors = new HashMap<Room, Integer>();
		ArrayList<TileBorder> possDoors = new ArrayList<TileBorder>();
		ArrayList<Room> uncheckedrooms = new ArrayList<Room>();
		for (Room r : tilemap.getRooms()) {
			countimpossibleDoors.put(r, r.allDoors.size());
		}
		for (Room r : tilemap.getRooms()) {
			uncheckedrooms.add(r);
			for (TileBorder door : r.allDoors) {
				if (!possDoors.contains(door)) {
					possDoors.add(door);
				}
			}
		}

		while (!uncheckedrooms.isEmpty()) {
			Room room = uncheckedrooms.get(0);
			if (room != startroom && room != endroom && countimpossibleDoors.get(room) <= 1) {
				uncheckedrooms.remove(room);
				if (countimpossibleDoors.get(room) <= 0)
					continue;
				possDoors.remove(room.allDoors.get(0));
				Room neigb = navM.getRoomNeigbourByDoor(room, room.allDoors.get(0));
				countimpossibleDoors.put(neigb, countimpossibleDoors.get(neigb) - 1);
				uncheckedrooms.add(neigb);
			} else {
				uncheckedrooms.remove(room);
			}
		}
		return possDoors;
	}

	private ArrayList<DoorPath> getAllDoorPaths() {
		ArrayList<DoorPath> doorpaths = new ArrayList<DoorPath>();
		for (TileBorder startdoor : startroom.allDoors) {
			if (!possibleDoors.contains(startdoor))
				continue;
			for (TileBorder enddoor : endroom.allDoors) {
				if (!possibleDoors.contains(enddoor))
					continue;
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
						if (!possibleDoors.contains(door))
							continue;
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
				doorpaths.add(doorpath);
			}
		}
		return doorpaths;
	}

	private ArrayList<DoorPath> getShortestDoorPaths() {
		ArrayList<DoorPath> shortpaths = new ArrayList<DoorPath>();
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
				shortpaths.add(min);
			}
		}
		return shortpaths;
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

	public Path getPath(Vector3 v_start, Vector3 v_end) {
		navM = Game.getCurrentGame().getNavigationManager();
		tilemap = Game.getCurrentGame().getTilemap();

		startroom = tilemap.getRoomAt(tilemap.coordToIndex(v_start.x), tilemap.coordToIndex(v_start.z));
		endroom = tilemap.getRoomAt(tilemap.coordToIndex(v_end.x), tilemap.coordToIndex(v_end.z));

		first = new NavPoint(v_start.x, v_start.z);
		last = new NavPoint(v_end.x, v_end.z);
		navM.get(startroom).add(first);
		navM.get(endroom).add(last);
		first.calculateVisibility(startroom);
		last.calculateVisibility(endroom);
		navM.get(startroom).remove(first);
		navM.get(endroom).remove(last);

		// If target vector is in line of sight return;
		if (first.visibleNavPoints.containsKey(last)) {
			Path tmp = new Path();
			tmp.path.add(first);
			tmp.path.add(last);
			return tmp;
		}

		// If a Room (which is neither the startroom or the endroom) just has
		// one door (so the door and the room can/must be ignored finding a
		// path) remove the door from the possible doors. If the next room
		// sharing this door now just have one other possible door left: repeat.
		possibleDoors = getPossibleDoors();

		// For all Doors in the startroom find the shortest way to all Doors in
		// the endroom. For doing this just use an A*-algorithm over all
		// possible doors. So if there are 2 doors in the startroom and 3 doors
		// in the endroom there're 6 possible Paths.
		allDoorPaths = getAllDoorPaths();

		// For all visible NavPoints from the startpoint find the shortest way
		// via the DoorPaths to all visible NavPoints from the endpoint. If
		// there are 2 visible NavPoints from the startpoint and 3 visible
		// NavPoints from the endpoint there're 6 possible paths.
		shortestpaths = getShortestDoorPaths();

		
		// Find the actual shortest way 
		DoorPath min = shortestpaths.get(0);
		for (DoorPath tmp : shortestpaths) {
			if (tmp.dist + first.visibleNavPoints.get(tmp.first) + last.visibleNavPoints.get(tmp.last) < min.dist
					+ first.visibleNavPoints.get(min.first) + last.visibleNavPoints.get(min.last)) {
				min = tmp;
			}
		}

		Path path = new Path();
		path.path.add(min.first);
		if (startroom == endroom) {
			// TODO: Fix
			if (min.first.router.get(last).dist < (min.dist
					+ min.first.router.get(navM.getDoorNavPointByRoom(min.path.get(0), startroom)).dist
					+ min.last.router
							.get(navM.getDoorNavPointByRoom(min.path.get(min.path.size() - 1), endroom)).dist)) {
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
}
