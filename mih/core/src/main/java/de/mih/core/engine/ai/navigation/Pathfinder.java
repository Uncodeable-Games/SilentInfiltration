package de.mih.core.engine.ai.navigation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.badlogic.gdx.math.Vector3;
import de.mih.core.engine.tilemap.Room;
import de.mih.core.engine.tilemap.TileBorder;
import de.mih.core.engine.tilemap.Tilemap;
import de.mih.core.game.Game;
import de.mih.core.game.components.BorderC;
import de.mih.core.game.components.ColliderC;

public class Pathfinder
{

	public class Path extends ArrayList<NavPoint>
	{
		private static final long serialVersionUID = 2998231369299443032L;
	}

	class DoorPath extends ArrayList<TileBorder>
	{
		private static final long serialVersionUID = -2929333627020171326L;
		
		float dist = Float.MAX_VALUE;
		NavPoint start;
		NavPoint end;
	}

	class Node
	{
		TileBorder nav;
		Node pre;
		float value;
	}

	private NavPoint first;
	private NavPoint last;
	private NavigationManager navigationManager;
	private Tilemap tilemap;
	private Room startroom;
	private Room endroom;

	private ArrayList<TileBorder> possibleDoors;
	private ArrayList<DoorPath> allDoorPaths;
	private ArrayList<DoorPath> shortestpaths;

	public Path getPath(Vector3 v_start, Vector3 v_end)
	{
		navigationManager = Game.getCurrentGame().getNavigationManager();
		tilemap = Game.getCurrentGame().getTilemap();

		startroom = tilemap.getRoomAt(tilemap.coordToIndex(v_start.x), tilemap.coordToIndex(v_start.z));
		endroom = tilemap.getRoomAt(tilemap.coordToIndex(v_end.x), tilemap.coordToIndex(v_end.z));

		// Room not found
		if (endroom == null || startroom == null)
		{
			// TODO: this should trigger an exception because it is not a really
			// result?
			return null;
		}

		first = new NavPoint(v_start.x, v_start.z);
		last = new NavPoint(v_end.x, v_end.z);
		navigationManager.get(startroom).add(first);
		navigationManager.get(endroom).add(last);
		first.calculateVisibility(startroom);
		last.calculateVisibility(endroom);
		navigationManager.get(startroom).remove(first);
		navigationManager.get(endroom).remove(last);

		// If target vector is in line of sight return;
		if (first.visibleNavPoints.containsKey(last))
		{
			Path tmp = new Path();
			tmp.add(first);
			tmp.add(last);
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
		Path path = new Path();

		// No Path found
		if (allDoorPaths.isEmpty())
		{
			if (startroom == endroom)
			{
				NavPoint tmp1 = (NavPoint) first.visibleNavPoints.keySet().toArray()[0];
				NavPoint tmp2 = (NavPoint) last.visibleNavPoints.keySet().toArray()[0];
				for (NavPoint nav1 : first.visibleNavPoints.keySet())
				{
					for (NavPoint nav2 : last.visibleNavPoints.keySet())
					{
						if (first.visibleNavPoints.get(nav1) + nav1.router.get(nav2).dist
								+ last.visibleNavPoints.get(nav2) < first.visibleNavPoints.get(tmp1)
										+ tmp1.router.get(tmp2).dist + last.visibleNavPoints.get(tmp2))
						{
							tmp1 = nav1;
							tmp2 = nav2;
						}
					}
				}
				path.add(tmp1);
				path.add(tmp2);
				path.add(last);
				return path;
			}
			return null;
		}
		if (navigationManager.getDoorNavPointByRoom(allDoorPaths.get(0).get(allDoorPaths.get(0).size() - 1),
				endroom) == null)
		{
			return null;
		}

		// For all visible NavPoints from the startpoint find the shortest way
		// via the DoorPaths to all visible NavPoints from the endpoint. If
		// there are 2 visible NavPoints from the startpoint and 3 visible
		// NavPoints from the endpoint there're 6 possible paths.
		shortestpaths = getShortestDoorPaths();

		// Last Navpoint ist not reachable
		if (shortestpaths.isEmpty())
		{
			return null;
		}
		// Find the actual shortest way
		DoorPath min = shortestpaths.get(0);
		for (DoorPath tmp : shortestpaths)
		{
			if (tmp.dist + first.visibleNavPoints.get(tmp.start) + last.visibleNavPoints.get(tmp.end) < min.dist
					+ first.visibleNavPoints.get(min.start) + last.visibleNavPoints.get(min.end))
			{
				min = tmp;
			}
		}

		// Path path = new Path();
		if (startroom == endroom)
		{
			NavPoint tmp1 = (NavPoint) first.visibleNavPoints.keySet().toArray()[0];
			NavPoint tmp2 = (NavPoint) last.visibleNavPoints.keySet().toArray()[0];
			for (NavPoint nav1 : first.visibleNavPoints.keySet())
			{
				for (NavPoint nav2 : last.visibleNavPoints.keySet())
				{
					if (first.visibleNavPoints.get(nav1) + nav1.router.get(nav2).dist
							+ last.visibleNavPoints.get(nav2) < first.visibleNavPoints.get(tmp1)
									+ tmp1.router.get(tmp2).dist + last.visibleNavPoints.get(tmp2))
					{
						tmp1 = nav1;
						tmp2 = nav2;
					}
				}
			}

			if (first.visibleNavPoints.get(tmp1) + tmp1.router.get(tmp2).dist
					+ last.visibleNavPoints.get(tmp2) < first.visibleNavPoints.get(min.start)
							+ last.visibleNavPoints.get(min.end)
							+ min.start.router.get(navigationManager.getDoorNavPointByRoom(min.get(0), startroom)).dist
							+ min.end.router
									.get(navigationManager.getDoorNavPointByRoom(min.get(min.size() - 1), endroom)).dist
							+ min.dist)
			{
				path.add(tmp1);
				path.add(tmp2);
				path.add(last);
				return path;
			}
		}
		path.add(min.start);
		Room curroom = startroom;
		for (TileBorder door : min)
		{
			NavPoint nav1 = navigationManager.getDoorNavPointByRoom(door, curroom);
			NavPoint nav2 = navigationManager.getDoorNavPointbyPartner(door, nav1);
			path.add(nav1);
			path.add(nav2);
			curroom = tilemap.getRoomAt(tilemap.coordToIndex(nav2.pos.x), tilemap.coordToIndex(nav2.pos.y));
		}
		path.add(min.end);
		path.add(last);
		return path;
	}

	private ArrayList<TileBorder> getPossibleDoors()
	{
		HashMap<Room, Integer> countimpossibleDoors = new HashMap<Room, Integer>();
		ArrayList<TileBorder> possDoors = new ArrayList<TileBorder>();
		ArrayList<Room> uncheckedrooms = new ArrayList<Room>();
		for (Room r : tilemap.getRooms())
		{
			int count = 0;
			for (TileBorder door : r.allDoors)
			{
				if (!Game.getCurrentGame().getEntityManager().getComponent(door.getColliderEntity(),
						BorderC.class).isclosed)
				{
					count++;
				}
			}
			countimpossibleDoors.put(r, count);
		}
		for (Room r : tilemap.getRooms())
		{
			uncheckedrooms.add(r);
			for (TileBorder door : r.allDoors)
			{
				if (!possDoors.contains(door) && !Game.getCurrentGame().getEntityManager()
						.getComponent(door.getColliderEntity(), BorderC.class).isclosed)
				{
					possDoors.add(door);
				}
			}
		}

		while (!uncheckedrooms.isEmpty())
		{
			Room room = uncheckedrooms.get(0);
			if (room != startroom && room != endroom && countimpossibleDoors.get(room) <= 1)
			{
				uncheckedrooms.remove(room);
				if (countimpossibleDoors.get(room) <= 0)
					continue;
				possDoors.remove(room.allDoors.get(0));
				Room neigb = navigationManager.getRoomNeigbourByDoor(room, room.allDoors.get(0));
				countimpossibleDoors.put(neigb, countimpossibleDoors.get(neigb) - 1);
				uncheckedrooms.add(neigb);
			}
			else
			{
				uncheckedrooms.remove(room);
			}
		}
		return possDoors;
	}

	private ArrayList<DoorPath> getAllDoorPaths()
	{
		ArrayList<DoorPath> doorpaths = new ArrayList<DoorPath>();
		for (TileBorder startdoor : startroom.allDoors)
		{
			if (!possibleDoors.contains(startdoor))
				continue;
			for (TileBorder enddoor : endroom.allDoors)
			{
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
				while (!openlist.isEmpty())
				{
					current = getMin(openlist);
					if (current.nav == enddoor)
						break;
					openlist.remove(current);
					closedlist.add(current);
					for (TileBorder door : navigationManager.getDoorNeighbours(current.nav).keySet())
					{
						if (!possibleDoors.contains(door))
							continue;
						if (!contains(closedlist, door) && !contains(openlist, door) && current.nav != door)
						{
							Node tmp = new Node();
							tmp.nav = door;
							tmp.pre = current;
							tmp.value = navigationManager.getDoorNeighbours(current.nav).get(door) + current.value
									+ door.getPos().dst(enddoor.getPos());
							openlist.add(tmp);
						}
					}
				}

				DoorPath doorpath = new DoorPath();
				Node tmp = current;
				while (tmp != start)
				{
					doorpath.add(tmp.nav);
					tmp = tmp.pre;
				}
				doorpath.add(start.nav);
				doorpath.dist = current.value;
				Collections.reverse(doorpath);
				doorpaths.add(doorpath);
			}
		}
		return doorpaths;
	}

	private ArrayList<DoorPath> getShortestDoorPaths()
	{
		ArrayList<DoorPath> shortpaths = new ArrayList<DoorPath>();
		for (NavPoint nav1 : first.visibleNavPoints.keySet())
		{
			for (NavPoint nav2 : last.visibleNavPoints.keySet())
			{
				DoorPath min = (DoorPath) allDoorPaths.toArray()[0];
				for (DoorPath path : allDoorPaths)
				{
					if ((path.dist + 4 * ColliderC.COLLIDER_RADIUS
							+ nav1.router.get(navigationManager.getDoorNavPointByRoom(path.get(0), startroom)).dist
							+ nav2.router.get(navigationManager.getDoorNavPointByRoom(path.get(path.size() - 1),
									endroom)).dist) < (min.dist
											+ 4 * ColliderC.COLLIDER_RADIUS
											+ nav1.router.get(
													navigationManager.getDoorNavPointByRoom(min.get(0), startroom)).dist
											+ nav2.router.get(navigationManager
													.getDoorNavPointByRoom(min.get(min.size() - 1), endroom)).dist))
					{
						min = path;
					}
				}
				min.dist = min.dist + 4 * ColliderC.COLLIDER_RADIUS
						+ nav1.router.get(navigationManager.getDoorNavPointByRoom(min.get(0), startroom)).dist
						+ nav2.router
								.get(navigationManager.getDoorNavPointByRoom(min.get(min.size() - 1), endroom)).dist;
				min.start = nav1;
				min.end = nav2;
				shortpaths.add(min);
			}
		}
		return shortpaths;
	}

	private Node getMin(ArrayList<Node> list)
	{
		Node min = (Node) list.toArray()[0];
		for (Node node : list)
		{
			if (node.value < min.value)
				min = node;
		}
		return min;
	}

	private boolean contains(ArrayList<Node> list, TileBorder door)
	{
		for (Node n : list)
		{
			if (n.nav == door)
				return true;
		}
		return false;
	}

}