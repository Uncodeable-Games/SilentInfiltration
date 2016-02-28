package de.mih.core.engine.ai.navigation;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;
import de.mih.core.engine.ai.navigation.NavPoint.Tuple;
import de.mih.core.engine.ai.navigation.pathfinder.Pathfinder;
import de.mih.core.engine.ai.navigation.pathfinder.Debugger.PFDebugger;
import de.mih.core.engine.tilemap.Door;
import de.mih.core.engine.tilemap.Room;
import de.mih.core.engine.tilemap.Tile;
import de.mih.core.engine.tilemap.TileBorder;
import de.mih.core.engine.tilemap.TileCorner;
import de.mih.core.engine.tilemap.Wall;
import de.mih.core.engine.tilemap.Tile.Direction;
import de.mih.core.game.Game;
import de.mih.core.game.components.ColliderC;
import de.mih.core.game.components.PositionC;

public class NavigationManager {

	public static final float TOLERANCE_RANGE = 0.05f;
	
	public Pathfinder pathfinder = new Pathfinder();
	public PFDebugger debugger = new PFDebugger();

	private HashMap<Room, ArrayList<NavPoint>> roomNavPoints = new HashMap<Room, ArrayList<NavPoint>>();
	private HashMap<ColliderC, ArrayList<NavPoint>> colliderNavPoints = new HashMap<ColliderC, ArrayList<NavPoint>>();
	private HashMap<TileCorner, HashMap<Direction, NavPoint>> tileCornerNavPoints = new HashMap<TileCorner, HashMap<Direction, NavPoint>>();
	private HashMap<Door, HashMap<Direction, NavPoint>> doorNavPoints = new HashMap<Door, HashMap<Direction, NavPoint>>();
	private HashMap<Door, HashMap<Door, Float>> doorneighbours = new HashMap<Door, HashMap<Door, Float>>();

	public void calculateNavigation() {
		roomNavPoints.clear();
		colliderNavPoints.clear();
		tileCornerNavPoints.clear();
		doorNavPoints.clear();
		for (Room r : Game.getCurrentGame().getTilemap().getRooms()) {
			calculateNavigationForRoom(r);
		}
		for (Room r : Game.getCurrentGame().getTilemap().getRooms()) {
			calcDoorNavPoints(r);
		}
		for (Room r : Game.getCurrentGame().getTilemap().getRooms()) {
			calcDoorNeigbours(r);
		}
		for (Room r: Game.getCurrentGame().getTilemap().getRooms()){
			for (NavPoint nav : get(r)){
				for (NavPoint tmp : nav.getVisibleNavPoints()){
					debugger.addEdge(nav, tmp);
				}
			}
		}
	}

	public void calculateNavigationForRoom(Room r) {
		get(r).clear();
		addEntityNavPointsForRoom(r);
		addBorderNavPoints(r);
		calculateVisibilityRoom(r);
		routeNavPointsRoom(r);
	}

	private void addEntityNavPointsForRoom(Room room) {
		for (int i : room.entitiesInRoom) {
			ColliderC col = Game.getCurrentGame().getEntityManager().getComponent(i, ColliderC.class);
			PositionC pos = Game.getCurrentGame().getEntityManager().getComponent(i, PositionC.class);
			get(col).clear();

			get(col).add(
					(new NavPoint(
							pos.getX() - (((col.getWidth() + (2 * ColliderC.COLLIDER_RADIUS / 0.8509f)) / 2f)
									+ TOLERANCE_RANGE),
					pos.getZ() - (((col.getWidth() + (2 * ColliderC.COLLIDER_RADIUS / 0.8509f)) / 2f)
							+ TOLERANCE_RANGE))));
			get(col).add(
					(new NavPoint(
							pos.getX() - (((col.getWidth() + (2 * ColliderC.COLLIDER_RADIUS / 0.8509f)) / 2f)
									+ TOLERANCE_RANGE),
					pos.getZ() + (((col.getWidth() + (2 * ColliderC.COLLIDER_RADIUS / 0.8509f)) / 2f)
							+ TOLERANCE_RANGE))));
			get(col).add(
					(new NavPoint(
							pos.getX() + (((col.getWidth() + (2 * ColliderC.COLLIDER_RADIUS / 0.8509f)) / 2f)
									+ TOLERANCE_RANGE),
					pos.getZ() + (((col.getWidth() + (2 * ColliderC.COLLIDER_RADIUS / 0.8509f)) / 2f)
							+ TOLERANCE_RANGE))));
			get(col).add(
					(new NavPoint(
							pos.getX() + (((col.getWidth() + (2 * ColliderC.COLLIDER_RADIUS / 0.8509f)) / 2f)
									+ TOLERANCE_RANGE),
					pos.getZ() - (((col.getWidth() + (2 * ColliderC.COLLIDER_RADIUS / 0.8509f)) / 2f)
							+ TOLERANCE_RANGE))));

			for (NavPoint nav : get(col)) {
				nav.setRoom(room);
				get(room).add(nav);
			}
		}
	}

	
	ArrayList<TileBorder> borders = new ArrayList<TileBorder>();
	private void addBorderNavPoints(Room room) {

		for (Wall wall : room.allWalls) {
			if (wall.getTileBorder().isHorizontal()){
				wall.getTileBorder().getCorner(Direction.E).checked = false;
				wall.getTileBorder().getCorner(Direction.W).checked = false;
			} else {
				wall.getTileBorder().getCorner(Direction.N).checked = false;
				wall.getTileBorder().getCorner(Direction.S).checked = false;
			}
		}
		
		for (Door door : room.allDoors) {
			if (door.getTileBorder().isHorizontal()){
				door.getTileBorder().getCorner(Direction.E).checked = false;
				door.getTileBorder().getCorner(Direction.W).checked = false;
			} else {
				door.getTileBorder().getCorner(Direction.N).checked = false;
				door.getTileBorder().getCorner(Direction.S).checked = false;
			}
		}
		
		

		borders.clear();
		for (Wall wall : room.allWalls) borders.add(wall.getTileBorder());
		for (Door door : room.allDoors) borders.add(door.getTileBorder());
		
		// Iterate over all Borders in this room
		for (TileBorder border : borders) {			

			// If the border is horizontal...
			if (border.isHorizontal()) {

				// Check east corner...
				TileCorner east = border.getCorner(Direction.E);
				// if the east corner is NOT at the border of the tilemap...
				// (you can ignore the corners at the borders of the tilemap)
				if (!east.checked && east.adjacentBorders.containsKey(Direction.N)
						&& east.adjacentBorders.containsKey(Direction.E)
						&& east.adjacentBorders.containsKey(Direction.S)
						&& east.adjacentBorders.containsKey(Direction.W)) {
					// if the east corner doesn't have another border to the
					// east OR has a border to the north AND to the south...
					// (If the east corner has another east border the wall
					// still goes on. If there's a north and a south corner it's
					// just a T-corner)
					east.checked = true;
					if (!(east.adjacentBorders.get(Direction.E).hasCollider()
							|| (east.adjacentBorders.get(Direction.N).hasCollider()
									&& east.adjacentBorders.get(Direction.S).hasCollider()))) {

						// If there's no north border set a NavPoint
						if (!east.adjacentBorders.get(Direction.N).hasCollider()
								&& east.adjacentTiles.get(Direction.N).getRoom() == room) {

							setBorderNavPointByDirection(east, Direction.W);
							get(room).add(get(east).get(Direction.W));
							get(east).get(Direction.W).setRoom(room);
						}
						// If there's no south border set a NavPoint
						if (!east.adjacentBorders.get(Direction.S).hasCollider()
								&& east.adjacentTiles.get(Direction.W).getRoom() == room) {

							setBorderNavPointByDirection(east, Direction.S);
							get(room).add(get(east).get(Direction.S));
							get(east).get(Direction.S).setRoom(room);
						}
					}
				}

				// West

				TileCorner west = border.getCorner(Direction.W);

				if (!west.checked && west.adjacentBorders.containsKey(Direction.N)
						&& west.adjacentBorders.containsKey(Direction.E)
						&& west.adjacentBorders.containsKey(Direction.S)
						&& west.adjacentBorders.containsKey(Direction.W)) {
					west.checked = true;
					if (!(west.adjacentBorders.get(Direction.W).hasCollider()
							|| (west.adjacentBorders.get(Direction.N).hasCollider()
									&& west.adjacentBorders.get(Direction.S).hasCollider()))) {

						if (!west.adjacentBorders.get(Direction.N).hasCollider()
								&& west.adjacentTiles.get(Direction.E).getRoom() == room) {

							setBorderNavPointByDirection(west, Direction.N);
							get(room).add(get(west).get(Direction.N));
							get(west).get(Direction.N).setRoom(room);
						}
						if (!west.adjacentBorders.get(Direction.S).hasCollider()
								&& west.adjacentTiles.get(Direction.S).getRoom() == room) {
							setBorderNavPointByDirection(west, Direction.E);
							get(room).add(get(west).get(Direction.E));
							get(west).get(Direction.E).setRoom(room);
						}
					}
				}
				// If the border is vertical
			} else {

				// North
				TileCorner north = border.getCorner(Direction.N);

				if (!north.checked && north.adjacentBorders.containsKey(Direction.N)
						&& north.adjacentBorders.containsKey(Direction.E)
						&& north.adjacentBorders.containsKey(Direction.S)
						&& north.adjacentBorders.containsKey(Direction.W)) {
					north.checked = true;
					if (!(north.adjacentBorders.get(Direction.N).hasCollider()
							|| (north.adjacentBorders.get(Direction.E).hasCollider()
									&& north.adjacentBorders.get(Direction.W).hasCollider()))) {

						if (!north.adjacentBorders.get(Direction.E).hasCollider()
								&& north.adjacentTiles.get(Direction.E).getRoom() == room) {
							setBorderNavPointByDirection(north, Direction.W);
							get(room).add(get(north).get(Direction.W));
							get(north).get(Direction.W).setRoom(room);
						}
						if (!north.adjacentBorders.get(Direction.W).hasCollider()
								&& north.adjacentTiles.get(Direction.N).getRoom() == room) {
							setBorderNavPointByDirection(north, Direction.N);
							get(room).add(get(north).get(Direction.N));
							get(north).get(Direction.N).setRoom(room);
						}
					}
				}

				// South
				TileCorner south = border.getCorner(Direction.S);
				if (!south.checked && south.adjacentBorders.containsKey(Direction.N)
						&& south.adjacentBorders.containsKey(Direction.E)
						&& south.adjacentBorders.containsKey(Direction.S)
						&& south.adjacentBorders.containsKey(Direction.W)) {
					south.checked = true;
					if (!(south.adjacentBorders.get(Direction.S).hasCollider()
							|| (south.adjacentBorders.get(Direction.E).hasCollider()
									&& south.adjacentBorders.get(Direction.W).hasCollider()))) {

						if (!south.adjacentBorders.get(Direction.E).hasCollider()
								&& south.adjacentTiles.get(Direction.S).getRoom() == room) {
							setBorderNavPointByDirection(south, Direction.S);
							get(room).add(get(south).get(Direction.S));
							get(south).get(Direction.S).setRoom(room);
						}
						if (!south.adjacentBorders.get(Direction.W).hasCollider()
								&& south.adjacentTiles.get(Direction.W).getRoom() == room) {
							setBorderNavPointByDirection(south, Direction.E);
							get(room).add(get(south).get(Direction.E));
							get(south).get(Direction.E).setRoom(room);
						}
					}
				}
			}
		}

		
		for (Door door : room.allDoors) {

			if (door.getTileBorder().isHorizontal()) {
				if (door.getTileBorder().getAdjacentTile(Direction.S) != null) {
					if (door.getTileBorder().getAdjacentTile(Direction.S).getRoom() == room) {
						if (!get(door).containsKey(Direction.S)) {
							get(door).put(Direction.S,
									new NavPoint(door.getTileBorder().getPos().x, door.getTileBorder().getPos().y + 2 * ColliderC.COLLIDER_RADIUS));
						}
						get(room).add(get(door).get(Direction.S));
						get(door).get(Direction.S).setRoom(room);
					}
				}
				if (door.getTileBorder().getAdjacentTile(Direction.N) != null) {
					if (door.getTileBorder().getAdjacentTile(Direction.N).getRoom() == room) {
						if (!get(door).containsKey(Direction.N)) {
							get(door).put(Direction.N,
									new NavPoint(door.getTileBorder().getPos().x, door.getTileBorder().getPos().y - 2 * ColliderC.COLLIDER_RADIUS));
						}
						get(room).add(get(door).get(Direction.N));
						get(door).get(Direction.N).setRoom(room);
					}
				}
			} else {
				if (door.getTileBorder().getAdjacentTile(Direction.E) != null) {
					if (door.getTileBorder().getAdjacentTile(Direction.E).getRoom() == room) {
						if (!get(door).containsKey(Direction.E)) {
							get(door).put(Direction.E,
									new NavPoint(door.getTileBorder().getPos().x + 2 * ColliderC.COLLIDER_RADIUS, door.getTileBorder().getPos().y));
						}
						get(room).add(get(door).get(Direction.E));
						get(door).get(Direction.E).setRoom(room);
					}
				}
				if (door.getTileBorder().getAdjacentTile(Direction.W) != null) {
					if (door.getTileBorder().getAdjacentTile(Direction.W).getRoom() == room) {
						if (!get(door).containsKey(Direction.W)) {
							get(door).put(Direction.W,
									new NavPoint(door.getTileBorder().getPos().x - 2 * ColliderC.COLLIDER_RADIUS, door.getTileBorder().getPos().y));
						}
						get(room).add(get(door).get(Direction.W));
						get(door).get(Direction.W).setRoom(room);
					}
				}
			}
		}

	}

	private void calculateVisibilityRoom(Room room) {
		for (NavPoint nav : get(room)) {
			nav.calculateVisibility();
		}
	}

	private void routeNavPointsRoom(Room room) {
		for (NavPoint nav : get(room)) {
			nav.flushRouter();
		}
		for (NavPoint nav : get(room)) {
			nav.route();
		}
	}

	private void calcDoorNavPoints(Room room) {
		for (Door door : room.allDoors) {
			NavPoint nav1 = (NavPoint) get(door).values().toArray()[0];
			NavPoint nav2 = (NavPoint) get(door).values().toArray()[1];

			nav1.addVisibleNavPoint(nav2, 4 * ColliderC.COLLIDER_RADIUS);
			nav2.addVisibleNavPoint(nav1, 4 * ColliderC.COLLIDER_RADIUS);

			nav1.addToRouter(nav2, new Tuple(nav2, nav1.getDistance(nav2)));
			nav2.addToRouter(nav1, new Tuple(nav1, nav2.getDistance(nav1)));
		}
	}

	private void calcDoorNeigbours(Room room) {
		for (Door door : room.allDoors) {
			if (!doorneighbours.containsKey(door)) {
				doorneighbours.put(door, new HashMap<Door, Float>());
			}
		}

		for (Door door : room.allDoors) {
			for (Door door2 : room.allDoors) {
				if (door == door2)
					continue;
				NavPoint nav1 = getDoorNavPointByRoom(door, room);
				NavPoint nav2 = getDoorNavPointByRoom(door2, room);

				doorneighbours.get(door).put(door2, nav1.getDistance(nav2) + 4 * ColliderC.COLLIDER_RADIUS);
				doorneighbours.get(door2).put(door, nav2.getDistance(nav1) + 4 * ColliderC.COLLIDER_RADIUS);
			}
		}
	}

	private void setBorderNavPointByDirection(TileCorner corner, Direction dir) {
		if (!get(corner).containsKey(dir)) {
			switch (dir) {
			case N: {
				get(corner).put(dir,
						new NavPoint(corner.getPos().x - TOLERANCE_RANGE - 2 * ColliderC.COLLIDER_RADIUS / 0.8509f,
								corner.getPos().y - TOLERANCE_RANGE - 2 * ColliderC.COLLIDER_RADIUS / 0.8509f));
				break;
			}
			case W: {
				get(corner).put(dir,
						new NavPoint(corner.getPos().x + TOLERANCE_RANGE + 2 * ColliderC.COLLIDER_RADIUS / 0.8509f,
								corner.getPos().y - TOLERANCE_RANGE - 2 * ColliderC.COLLIDER_RADIUS / 0.8509f));
				break;
			}
			case S: {
				get(corner).put(dir,
						new NavPoint(corner.getPos().x + TOLERANCE_RANGE + 2 * ColliderC.COLLIDER_RADIUS / 0.8509f,
								corner.getPos().y + TOLERANCE_RANGE + 2 * ColliderC.COLLIDER_RADIUS / 0.8509f));
				break;
			}
			case E: {
				get(corner).put(dir,
						new NavPoint(corner.getPos().x - TOLERANCE_RANGE - 2 * ColliderC.COLLIDER_RADIUS / 0.8509f,
								corner.getPos().y + TOLERANCE_RANGE + 2 * ColliderC.COLLIDER_RADIUS / 0.8509f));
				break;
			}
			}
		}
	}

	static Vector2 r1 = new Vector2();
	static Vector2 r2 = new Vector2();
	static Vector2 r3 = new Vector2();
	static Vector2 r4 = new Vector2();

	static Vector2 rp1 = new Vector2(), rp2 = new Vector2(), rpos = new Vector2(0, 0);

	public static boolean LineIntersectsCollider(Vector2 p1, Vector2 p2, ColliderC col, PositionC pos) {
		rpos.set(pos.getX(), pos.getZ());
		rp1.set(p1).sub(rpos).rotate(pos.getAngle());
		rp2.set(p2).sub(rpos).rotate(pos.getAngle());
		r1.set(-(col.getWidth() + (2 * ColliderC.COLLIDER_RADIUS / 0.8509f)) / 2f,
				-(col.getLength() + (2 * ColliderC.COLLIDER_RADIUS / 0.8509f)) / 2f);
		r2.set(-(col.getWidth() + (2 * ColliderC.COLLIDER_RADIUS / 0.8509f)) / 2f,
				(col.getLength() + (2 * ColliderC.COLLIDER_RADIUS / 0.8509f)) / 2f);
		r3.set((col.getWidth() + (2 * ColliderC.COLLIDER_RADIUS / 0.8509f)) / 2f,
				(col.getLength() + (2 * ColliderC.COLLIDER_RADIUS / 0.8509f)) / 2f);
		r4.set((col.getWidth() + (2 * ColliderC.COLLIDER_RADIUS / 0.8509f)) / 2f,
				-(col.getLength() + (2 * ColliderC.COLLIDER_RADIUS / 0.8509f)) / 2f);

		return LineIntersectsLine(rp1, rp2, r1, r2) || LineIntersectsLine(rp1, rp2, r1, r4)
				|| LineIntersectsLine(rp1, rp2, r3, r2) || LineIntersectsLine(rp1, rp2, r3, r4)
				|| (rectContainsPoint(rp1, r1, r3) && rectContainsPoint(rp2, r1, r3));
	}

	private static boolean rectContainsPoint(Vector2 point, Vector2 min, Vector2 max) {
		return (min.x <= point.x && point.x <= max.x && min.y <= point.y && point.y <= max.y);
	}

	private static boolean LineIntersectsLine(Vector2 l1p1, Vector2 l1p2, Vector2 l2p1, Vector2 l2p2) {
		float q = (l1p1.y - l2p1.y) * (l2p2.x - l2p1.x) - (l1p1.x - l2p1.x) * (l2p2.y - l2p1.y);
		float d = (l1p2.x - l1p1.x) * (l2p2.y - l2p1.y) - (l1p2.y - l1p1.y) * (l2p2.x - l2p1.x);

		if (d == 0) {
			return false;
		}

		float r = q / d;

		q = (l1p1.y - l2p1.y) * (l1p2.x - l1p1.x) - (l1p1.x - l2p1.x) * (l1p2.y - l1p1.y);
		float s = q / d;

		if (r < 0 || r > 1 || s < 0 || s > 1) {
			return false;
		}

		return true;
	}

	public NavPoint getDoorNavPointByRoom(Door door, Room room) {
		if (get(room).contains((NavPoint) (get(door).values().toArray()[0]))) {
			return (NavPoint) (get(door).values().toArray()[0]);
		}
		if (get(room).contains((NavPoint) (get(door).values().toArray()[1]))) {
			return (NavPoint) (get(door).values().toArray()[1]);
		}
		return null;
	}

	public NavPoint getDoorNavPointbyPartner(Door door, NavPoint nav) {
		if (!get(door).containsValue(nav)){
			return null;
		}
		NavPoint tmp = get(door).values().toArray()[0] == nav ? (NavPoint) get(door).values().toArray()[1]
				: (NavPoint) get(door).values().toArray()[0];
		return tmp;
	}

	public Room getRoomNeigbourByDoor(Room room, TileBorder door) {
		if (!room.allDoors.contains(door))
			return null;
		Room tmp = ((Tile) door.adjacentTiles.values().toArray()[0]).getRoom() == room
				? ((Tile) door.adjacentTiles.values().toArray()[1]).getRoom()
				: ((Tile) door.adjacentTiles.values().toArray()[0]).getRoom();
		return tmp;
	}

	public ArrayList<NavPoint> get(ColliderC col) {
		if (!colliderNavPoints.containsKey(col))
			colliderNavPoints.put(col, new ArrayList<NavPoint>());
		return colliderNavPoints.get(col);
	}

	public ArrayList<NavPoint> get(Room room) {
		if (!roomNavPoints.containsKey(room))
			roomNavPoints.put(room, new ArrayList<NavPoint>());
		return roomNavPoints.get(room);
	}

	public HashMap<Direction, NavPoint> get(TileCorner corner) {
		if (!tileCornerNavPoints.containsKey(corner))
			tileCornerNavPoints.put(corner, new HashMap<Direction, NavPoint>());
		return tileCornerNavPoints.get(corner);
	}

	public HashMap<Direction, NavPoint> get(Door door) {
		if (!doorNavPoints.containsKey(door))
			doorNavPoints.put(door, new HashMap<Direction, NavPoint>());
		return doorNavPoints.get(door);
	}

	public HashMap<Door, Float> getDoorNeighbours(Door door) {
		if (!doorneighbours.containsKey(door))
			return null;
		return doorneighbours.get(door);
	}
	
	public Pathfinder getPathfinder(){
		return pathfinder;
	}
}
