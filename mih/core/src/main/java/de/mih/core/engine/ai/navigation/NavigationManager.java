package de.mih.core.engine.ai.navigation;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import de.mih.core.engine.ai.navigation.NavPoint.Tuple;
import de.mih.core.engine.tilemap.Room;
import de.mih.core.engine.tilemap.TileBorder;
import de.mih.core.engine.tilemap.TileCorner;
import de.mih.core.engine.tilemap.Tilemap;
import de.mih.core.engine.tilemap.Tile.Direction;
import de.mih.core.game.Game;
import de.mih.core.game.components.ColliderC;
import de.mih.core.game.components.PositionC;

public class NavigationManager {

	public static final float TOLERANCE_RANGE = 0.05f;

	private HashMap<Room, ArrayList<NavPoint>> roomNavPoints = new HashMap<Room, ArrayList<NavPoint>>();
	private HashMap<ColliderC, ArrayList<NavPoint>> colliderNavPoints = new HashMap<ColliderC, ArrayList<NavPoint>>();
	private HashMap<TileCorner, HashMap<Direction, NavPoint>> tileCornerNavPoints = new HashMap<TileCorner, HashMap<Direction, NavPoint>>();
	private HashMap<TileBorder, HashMap<Direction, NavPoint>> doorNavPoints = new HashMap<TileBorder, HashMap<Direction, NavPoint>>();
	private HashMap<TileBorder, HashMap<TileBorder, Float>> doorneighbours = new HashMap<TileBorder, HashMap<TileBorder, Float>>();

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

	private void addBorderNavPoints(Room room) {

		for (TileCorner corner : room.allCorners) {
			corner.checked = false;
		}

		// Iterate over all Borders in this room
		for (TileBorder border : room.allBorders) {

			// skip Border if it doesn't have a Collider
			if (!border.hasColliderEntity())
				continue;

			// If the border is horizontal...
			if (border.isHorizontal()) {

				// Check east corner...
				TileCorner east = border.corners.get(Direction.E);
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
					if (!(east.adjacentBorders.get(Direction.E).hasColliderEntity()
							|| (east.adjacentBorders.get(Direction.N).hasColliderEntity()
									&& east.adjacentBorders.get(Direction.S).hasColliderEntity()))) {

						// If there's no north border set a NavPoint
						if (!east.adjacentBorders.get(Direction.N).hasColliderEntity()
								&& east.adjacentTiles.get(Direction.N).getRoom() == room) {

							setBorderNavPointByDirection(east, Direction.W);
							get(room).add(get(east).get(Direction.W));
							get(east).get(Direction.W).setRoom(room);
						}
						// If there's no south border set a NavPoint
						if (!east.adjacentBorders.get(Direction.S).hasColliderEntity()
								&& east.adjacentTiles.get(Direction.W).getRoom() == room) {

							setBorderNavPointByDirection(east, Direction.S);
							get(room).add(get(east).get(Direction.S));
							get(east).get(Direction.S).setRoom(room);
						}
					}
				}

				// West

				TileCorner west = border.corners.get(Direction.W);

				if (!west.checked && west.adjacentBorders.containsKey(Direction.N)
						&& west.adjacentBorders.containsKey(Direction.E)
						&& west.adjacentBorders.containsKey(Direction.S)
						&& west.adjacentBorders.containsKey(Direction.W)) {
					west.checked = true;
					if (!(west.adjacentBorders.get(Direction.W).hasColliderEntity()
							|| (west.adjacentBorders.get(Direction.N).hasColliderEntity()
									&& west.adjacentBorders.get(Direction.S).hasColliderEntity()))) {

						if (!west.adjacentBorders.get(Direction.N).hasColliderEntity()
								&& west.adjacentTiles.get(Direction.E).getRoom() == room) {

							setBorderNavPointByDirection(west, Direction.N);
							get(room).add(get(west).get(Direction.N));
							get(west).get(Direction.N).setRoom(room);
						}
						if (!west.adjacentBorders.get(Direction.S).hasColliderEntity()
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
				TileCorner north = border.corners.get(Direction.N);

				if (!north.checked && north.adjacentBorders.containsKey(Direction.N)
						&& north.adjacentBorders.containsKey(Direction.E)
						&& north.adjacentBorders.containsKey(Direction.S)
						&& north.adjacentBorders.containsKey(Direction.W)) {
					north.checked = true;
					if (!(north.adjacentBorders.get(Direction.N).hasColliderEntity()
							|| (north.adjacentBorders.get(Direction.E).hasColliderEntity()
									&& north.adjacentBorders.get(Direction.W).hasColliderEntity()))) {

						if (!north.adjacentBorders.get(Direction.E).hasColliderEntity()
								&& north.adjacentTiles.get(Direction.E).getRoom() == room) {
							setBorderNavPointByDirection(north, Direction.W);
							get(room).add(get(north).get(Direction.W));
							get(north).get(Direction.W).setRoom(room);
						}
						if (!north.adjacentBorders.get(Direction.W).hasColliderEntity()
								&& north.adjacentTiles.get(Direction.N).getRoom() == room) {
							setBorderNavPointByDirection(north, Direction.N);
							get(room).add(get(north).get(Direction.N));
							get(north).get(Direction.N).setRoom(room);
						}
					}
				}

				// South
				TileCorner south = border.corners.get(Direction.S);
				if (!south.checked && south.adjacentBorders.containsKey(Direction.N)
						&& south.adjacentBorders.containsKey(Direction.E)
						&& south.adjacentBorders.containsKey(Direction.S)
						&& south.adjacentBorders.containsKey(Direction.W)) {
					south.checked = true;
					if (!(south.adjacentBorders.get(Direction.S).hasColliderEntity()
							|| (south.adjacentBorders.get(Direction.E).hasColliderEntity()
									&& south.adjacentBorders.get(Direction.W).hasColliderEntity()))) {

						if (!south.adjacentBorders.get(Direction.E).hasColliderEntity()
								&& south.adjacentTiles.get(Direction.S).getRoom() == room) {
							setBorderNavPointByDirection(south, Direction.S);
							get(room).add(get(south).get(Direction.S));
							get(south).get(Direction.S).setRoom(room);
						}
						if (!south.adjacentBorders.get(Direction.W).hasColliderEntity()
								&& south.adjacentTiles.get(Direction.W).getRoom() == room) {
							setBorderNavPointByDirection(south, Direction.E);
							get(room).add(get(south).get(Direction.E));
							get(south).get(Direction.E).setRoom(room);
						}
					}
				}
			}
		}

		for (TileBorder door : room.allDoors) {

			if (door.isHorizontal()) {
				if (door.getAdjacentTile(Direction.S) != null) {
					if (door.getAdjacentTile(Direction.S).getRoom() == room) {
						if (!get(door).containsKey(Direction.S)) {
							get(door).put(Direction.S,
									new NavPoint(door.getPos().x, door.getPos().y + 2 * ColliderC.COLLIDER_RADIUS));
						}
						get(room).add(get(door).get(Direction.S));
						get(door).get(Direction.S).setRoom(room);
					}
				}
				if (door.getAdjacentTile(Direction.N) != null) {
					if (door.getAdjacentTile(Direction.N).getRoom() == room) {
						if (!get(door).containsKey(Direction.N)) {
							get(door).put(Direction.N,
									new NavPoint(door.getPos().x, door.getPos().y - 2 * ColliderC.COLLIDER_RADIUS));
						}
						get(room).add(get(door).get(Direction.N));
						get(door).get(Direction.N).setRoom(room);
					}
				}
			} else {
				if (door.getAdjacentTile(Direction.E) != null) {
					if (door.getAdjacentTile(Direction.E).getRoom() == room) {
						if (!get(door).containsKey(Direction.E)) {
							get(door).put(Direction.E,
									new NavPoint(door.getPos().x + 2 * ColliderC.COLLIDER_RADIUS, door.getPos().y));
						}
						get(room).add(get(door).get(Direction.E));
						get(door).get(Direction.E).setRoom(room);
					}
				}
				if (door.getAdjacentTile(Direction.W) != null) {
					if (door.getAdjacentTile(Direction.W).getRoom() == room) {
						if (!get(door).containsKey(Direction.W)) {
							get(door).put(Direction.W,
									new NavPoint(door.getPos().x - 2 * ColliderC.COLLIDER_RADIUS, door.getPos().y));
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
			nav.calculateVisibility(room);
		}
	}

	private void routeNavPointsRoom(Room room) {
		for (NavPoint nav : get(room)) {
			nav.router.clear();
		}
		for (NavPoint nav : get(room)) {
			nav.route();
		}
	}

	private void calcDoorNavPoints(Room room) {
		for (TileBorder door : room.allDoors) {
			NavPoint nav1 = (NavPoint) get(door).values().toArray()[0];
			NavPoint nav2 = (NavPoint) get(door).values().toArray()[1];

			nav1.visibleNavPoints.put(nav2, (float) Math.sqrt(((nav1.pos.x - nav2.pos.x) * (nav1.pos.x - nav2.pos.x)
					+ (nav1.pos.y - nav2.pos.y) * (nav1.pos.y - nav2.pos.y))));
			nav2.visibleNavPoints.put(nav1, (float) Math.sqrt(((nav1.pos.x - nav2.pos.x) * (nav1.pos.x - nav2.pos.x)
					+ (nav1.pos.y - nav2.pos.y) * (nav1.pos.y - nav2.pos.y))));

			nav1.router.put(nav2, new Tuple(nav2, nav1.visibleNavPoints.get(nav2)));
			nav2.router.put(nav1, new Tuple(nav1, nav2.visibleNavPoints.get(nav1)));
		}
	}

	private void calcDoorNeigbours(Room room) {
		for (TileBorder door : room.allDoors) {
			if (!doorneighbours.containsKey(door)) {
				doorneighbours.put(door, new HashMap<TileBorder, Float>());
			}
		}

		for (TileBorder door : room.allDoors) {
			for (TileBorder door2 : room.allDoors) {
				if (door == door2)
					continue;
				NavPoint nav1 = getDoorNavPointByRoom(door, room);
				NavPoint nav2 = getDoorNavPointByRoom(door2, room);
				
				doorneighbours.get(door).put(door2, nav1.router.get(nav2).dist);
				doorneighbours.get(door2).put(door, nav1.router.get(nav2).dist);
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

	public NavPoint getDoorNavPointByRoom(TileBorder door, Room room) {
		if (get(room).contains((NavPoint) (get(door).values().toArray()[0]))) {
			return (NavPoint) (get(door).values().toArray()[0]);
		}
		if (get(room).contains((NavPoint) (get(door).values().toArray()[1]))) {
			return (NavPoint) (get(door).values().toArray()[1]);
		}
		return null;
	}

	public NavPoint getDoorNavPointbyPartner(TileBorder door, NavPoint nav) {
		if (!get(door).containsValue(nav))
			return null;
		NavPoint tmp = get(door).values().toArray()[0] == nav ? (NavPoint) get(door).values().toArray()[1]
				: (NavPoint) get(door).values().toArray()[0];
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

	public HashMap<Direction, NavPoint> get(TileBorder border) {
		if (!doorNavPoints.containsKey(border))
			doorNavPoints.put(border, new HashMap<Direction, NavPoint>());
		return doorNavPoints.get(border);
	}

	public HashMap<TileBorder, Float> getDoorNeighbours(TileBorder door) {
		if (!doorneighbours.containsKey(door))
			return null;
		return doorneighbours.get(door);
	}
}
