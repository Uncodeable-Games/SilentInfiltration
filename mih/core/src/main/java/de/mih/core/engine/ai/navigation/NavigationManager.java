package de.mih.core.engine.ai.navigation;

import com.badlogic.gdx.math.Vector2;
import de.mih.core.engine.ai.navigation.pathfinder.Pathfinder;
import de.mih.core.engine.tilemap.*;
import de.mih.core.engine.tilemap.Tile.Direction;
import de.mih.core.game.Game;
import de.mih.core.game.components.ColliderC;
import de.mih.core.game.components.PositionC;

import java.util.ArrayList;
import java.util.HashMap;

public class NavigationManager
{

	private static final float TOLERANCE_RANGE = 0.05f;
	
	private Pathfinder pathfinder = new Pathfinder();

	private HashMap<Room, ArrayList<NavPoint>>                roomNavPoints       = new HashMap<>();
	private HashMap<ColliderC, ArrayList<NavPoint>>           colliderNavPoints   = new HashMap<>();
	private HashMap<TileCorner, HashMap<Direction, NavPoint>> tileCornerNavPoints = new HashMap<>();
	private HashMap<Door, HashMap<Direction, NavPoint>>       doorNavPoints       = new HashMap<>();
	private HashMap<Door, HashMap<Door, Float>>               doorneighbours      = new HashMap<>();

	public void calculateNavigation()
	{
		roomNavPoints.clear();
		colliderNavPoints.clear();
		tileCornerNavPoints.clear();
		doorNavPoints.clear();

		for (Room r : Game.getCurrentGame().getTilemap().getRooms())
		{
			calculateNavigationForRoom(r);
		}
		for (Room r : Game.getCurrentGame().getTilemap().getRooms())
		{
			calcDoorNavPoints(r);
		}
		for (Room r : Game.getCurrentGame().getTilemap().getRooms())
		{
			calcDoorNeigbours(r);
		}
	}

	public void calculateNavigationForRoom(Room r)
	{
		getNavPoints(r).clear();
		addEntityNavPointsForRoom(r);
		addBorderNavPoints(r);
		calculateVisibilityRoom(r);
		//routeNavPointsRoom(r);
	}

	private void addEntityNavPointsForRoom(Room room)
	{
		for (int i : room.entitiesInRoom)
		{
			ColliderC col = Game.getCurrentGame().getEntityManager().getComponent(i, ColliderC.class);
			PositionC pos = Game.getCurrentGame().getEntityManager().getComponent(i, PositionC.class);
			getNavPoints(col).clear();

			getNavPoints(col).add(
					(new NavPoint(
							pos.getX() - (((col.getWidth() + (2 * ColliderC.COLLIDER_RADIUS / 0.8509f)) / 2f)
									+ TOLERANCE_RANGE),
							pos.getZ() - (((col.getWidth() + (2 * ColliderC.COLLIDER_RADIUS / 0.8509f)) / 2f)
									+ TOLERANCE_RANGE))));
			getNavPoints(col).add(
					(new NavPoint(
							pos.getX() - (((col.getWidth() + (2 * ColliderC.COLLIDER_RADIUS / 0.8509f)) / 2f)
									+ TOLERANCE_RANGE),
							pos.getZ() + (((col.getWidth() + (2 * ColliderC.COLLIDER_RADIUS / 0.8509f)) / 2f)
									+ TOLERANCE_RANGE))));
			getNavPoints(col).add(
					(new NavPoint(
							pos.getX() + (((col.getWidth() + (2 * ColliderC.COLLIDER_RADIUS / 0.8509f)) / 2f)
									+ TOLERANCE_RANGE),
							pos.getZ() + (((col.getWidth() + (2 * ColliderC.COLLIDER_RADIUS / 0.8509f)) / 2f)
									+ TOLERANCE_RANGE))));
			getNavPoints(col).add(
					(new NavPoint(
							pos.getX() + (((col.getWidth() + (2 * ColliderC.COLLIDER_RADIUS / 0.8509f)) / 2f)
									+ TOLERANCE_RANGE),
							pos.getZ() - (((col.getWidth() + (2 * ColliderC.COLLIDER_RADIUS / 0.8509f)) / 2f)
									+ TOLERANCE_RANGE))));

			for (NavPoint nav : getNavPoints(col))
			{
				nav.setRoom(room);
				getNavPoints(room).add(nav);
			}
		}
	}

	private ArrayList<TileBorder> borders = new ArrayList<>();

	private void addBorderNavPoints(Room room)
	{

		for (Wall wall : room.allWalls)
		{
			if (wall.getTileBorder().isHorizontal())
			{
				wall.getTileBorder().getCorner(Direction.E).checked = false;
				wall.getTileBorder().getCorner(Direction.W).checked = false;
			}
			else
			{
				wall.getTileBorder().getCorner(Direction.N).checked = false;
				wall.getTileBorder().getCorner(Direction.S).checked = false;
			}
		}
		
		for (Door door : room.allDoors)
		{
			if (door.getTileBorder().isHorizontal())
			{
				door.getTileBorder().getCorner(Direction.E).checked = false;
				door.getTileBorder().getCorner(Direction.W).checked = false;
			}
			else
			{
				door.getTileBorder().getCorner(Direction.N).checked = false;
				door.getTileBorder().getCorner(Direction.S).checked = false;
			}
		}
		
		borders.clear();
		for (Wall wall : room.allWalls) borders.add(wall.getTileBorder());
		for (Door door : room.allDoors) borders.add(door.getTileBorder());
		
		// Iterate over all Borders in this room
		for (TileBorder border : borders)
		{
			// If the border is horizontal...
			if (border.isHorizontal())
			{

				// Check east corner...
				TileCorner east = border.getCorner(Direction.E);
				// if the east corner is NOT at the border of the tilemap...
				// (you can ignore the corners at the borders of the tilemap)
				if (!east.checked && east.adjacentBorders.containsKey(Direction.N)
						&& east.adjacentBorders.containsKey(Direction.E)
						&& east.adjacentBorders.containsKey(Direction.S)
						&& east.adjacentBorders.containsKey(Direction.W))
				{
					// if the east corner doesn't have another border to the
					// east OR has a border to the north AND to the south...
					// (If the east corner has another east border the wall
					// still goes on. If there's a north and a south corner it's
					// just a T-corner)
					east.checked = true;
					if (!(east.adjacentBorders.get(Direction.E).hasCollider()
							|| (east.adjacentBorders.get(Direction.N).hasCollider()
							&& east.adjacentBorders.get(Direction.S).hasCollider())))
					{

						// If there's no north border set a NavPoint
						if (!east.adjacentBorders.get(Direction.N).hasCollider()
								&& east.adjacentTiles.get(Direction.N).getRoom() == room)
						{

							setBorderNavPointByDirection(east, Direction.W);
							getNavPoints(room).add(getNavPoints(east).get(Direction.W));
							getNavPoints(east).get(Direction.W).setRoom(room);
						}
						// If there's no south border set a NavPoint
						if (!east.adjacentBorders.get(Direction.S).hasCollider()
								&& east.adjacentTiles.get(Direction.W).getRoom() == room)
						{

							setBorderNavPointByDirection(east, Direction.S);
							getNavPoints(room).add(getNavPoints(east).get(Direction.S));
							getNavPoints(east).get(Direction.S).setRoom(room);
						}
					}
				}

				// West

				TileCorner west = border.getCorner(Direction.W);

				if (!west.checked && west.adjacentBorders.containsKey(Direction.N)
						&& west.adjacentBorders.containsKey(Direction.E)
						&& west.adjacentBorders.containsKey(Direction.S)
						&& west.adjacentBorders.containsKey(Direction.W))
				{
					west.checked = true;
					if (!(west.adjacentBorders.get(Direction.W).hasCollider()
							|| (west.adjacentBorders.get(Direction.N).hasCollider()
							&& west.adjacentBorders.get(Direction.S).hasCollider())))
					{

						if (!west.adjacentBorders.get(Direction.N).hasCollider()
								&& west.adjacentTiles.get(Direction.E).getRoom() == room)
						{

							setBorderNavPointByDirection(west, Direction.N);
							getNavPoints(room).add(getNavPoints(west).get(Direction.N));
							getNavPoints(west).get(Direction.N).setRoom(room);
						}
						if (!west.adjacentBorders.get(Direction.S).hasCollider()
								&& west.adjacentTiles.get(Direction.S).getRoom() == room)
						{
							setBorderNavPointByDirection(west, Direction.E);
							getNavPoints(room).add(getNavPoints(west).get(Direction.E));
							getNavPoints(west).get(Direction.E).setRoom(room);
						}
					}
				}
				// If the border is vertical
			}
			else
			{

				// North
				TileCorner north = border.getCorner(Direction.N);

				if (!north.checked && north.adjacentBorders.containsKey(Direction.N)
						&& north.adjacentBorders.containsKey(Direction.E)
						&& north.adjacentBorders.containsKey(Direction.S)
						&& north.adjacentBorders.containsKey(Direction.W))
				{
					north.checked = true;
					if (!(north.adjacentBorders.get(Direction.N).hasCollider()
							|| (north.adjacentBorders.get(Direction.E).hasCollider()
							&& north.adjacentBorders.get(Direction.W).hasCollider())))
					{

						if (!north.adjacentBorders.get(Direction.E).hasCollider()
								&& north.adjacentTiles.get(Direction.E).getRoom() == room)
						{
							setBorderNavPointByDirection(north, Direction.W);
							getNavPoints(room).add(getNavPoints(north).get(Direction.W));
							getNavPoints(north).get(Direction.W).setRoom(room);
						}
						if (!north.adjacentBorders.get(Direction.W).hasCollider()
								&& north.adjacentTiles.get(Direction.N).getRoom() == room)
						{
							setBorderNavPointByDirection(north, Direction.N);
							getNavPoints(room).add(getNavPoints(north).get(Direction.N));
							getNavPoints(north).get(Direction.N).setRoom(room);
						}
					}
				}

				// South
				TileCorner south = border.getCorner(Direction.S);
				if (!south.checked && south.adjacentBorders.containsKey(Direction.N)
						&& south.adjacentBorders.containsKey(Direction.E)
						&& south.adjacentBorders.containsKey(Direction.S)
						&& south.adjacentBorders.containsKey(Direction.W))
				{
					south.checked = true;
					if (!(south.adjacentBorders.get(Direction.S).hasCollider()
							|| (south.adjacentBorders.get(Direction.E).hasCollider()
							&& south.adjacentBorders.get(Direction.W).hasCollider())))
					{

						if (!south.adjacentBorders.get(Direction.E).hasCollider()
								&& south.adjacentTiles.get(Direction.S).getRoom() == room)
						{
							setBorderNavPointByDirection(south, Direction.S);
							getNavPoints(room).add(getNavPoints(south).get(Direction.S));
							getNavPoints(south).get(Direction.S).setRoom(room);
						}
						if (!south.adjacentBorders.get(Direction.W).hasCollider()
								&& south.adjacentTiles.get(Direction.W).getRoom() == room)
						{
							setBorderNavPointByDirection(south, Direction.E);
							getNavPoints(room).add(getNavPoints(south).get(Direction.E));
							getNavPoints(south).get(Direction.E).setRoom(room);
						}
					}
				}
			}
		}

		for (Door door : room.allDoors)
		{

			if (door.getTileBorder().isHorizontal())
			{
				if (door.getTileBorder().getAdjacentTile(Direction.S) != null)
				{
					if (door.getTileBorder().getAdjacentTile(Direction.S).getRoom() == room)
					{
						if (!getNavPoints(door).containsKey(Direction.S))
						{
							getNavPoints(door).put(Direction.S,
									new NavPoint(door.getTileBorder().getPos().x, door.getTileBorder().getPos().y + 2 * ColliderC.COLLIDER_RADIUS));
						}
						getNavPoints(room).add(getNavPoints(door).get(Direction.S));
						getNavPoints(door).get(Direction.S).setRoom(room);
					}
				}
				if (door.getTileBorder().getAdjacentTile(Direction.N) != null)
				{
					if (door.getTileBorder().getAdjacentTile(Direction.N).getRoom() == room)
					{
						if (!getNavPoints(door).containsKey(Direction.N))
						{
							getNavPoints(door).put(Direction.N,
									new NavPoint(door.getTileBorder().getPos().x, door.getTileBorder().getPos().y - 2 * ColliderC.COLLIDER_RADIUS));
						}
						getNavPoints(room).add(getNavPoints(door).get(Direction.N));
						getNavPoints(door).get(Direction.N).setRoom(room);
					}
				}
			}
			else
			{
				if (door.getTileBorder().getAdjacentTile(Direction.E) != null)
				{
					if (door.getTileBorder().getAdjacentTile(Direction.E).getRoom() == room)
					{
						if (!getNavPoints(door).containsKey(Direction.E))
						{
							getNavPoints(door).put(Direction.E,
									new NavPoint(door.getTileBorder().getPos().x + 2 * ColliderC.COLLIDER_RADIUS, door.getTileBorder().getPos().y));
						}
						getNavPoints(room).add(getNavPoints(door).get(Direction.E));
						getNavPoints(door).get(Direction.E).setRoom(room);
					}
				}
				if (door.getTileBorder().getAdjacentTile(Direction.W) != null)
				{
					if (door.getTileBorder().getAdjacentTile(Direction.W).getRoom() == room)
					{
						if (!getNavPoints(door).containsKey(Direction.W))
						{
							getNavPoints(door).put(Direction.W,
									new NavPoint(door.getTileBorder().getPos().x - 2 * ColliderC.COLLIDER_RADIUS, door.getTileBorder().getPos().y));
						}
						getNavPoints(room).add(getNavPoints(door).get(Direction.W));
						getNavPoints(door).get(Direction.W).setRoom(room);
					}
				}
			}
		}
	}

	private void calculateVisibilityRoom(Room room)
	{
		for (NavPoint nav : getNavPoints(room))
		{
			nav.calculateVisibility();
		}
	}

	private void routeNavPointsRoom(Room room)
	{
		for (NavPoint nav : getNavPoints(room))
		{
			nav.flushRouter();
		}
		for (NavPoint nav : getNavPoints(room))
		{
			nav.route();
		}
	}

	private void calcDoorNavPoints(Room room)
	{
		for (Door door : room.allDoors)
		{
			if (!door.isClosed())
				door.open();
		}
	}

	private void calcDoorNeigbours(Room room)
	{
		for (Door door : room.allDoors)
		{
			if (!doorneighbours.containsKey(door))
			{
				doorneighbours.put(door, new HashMap<>());
			}
		}

		for (Door door : room.allDoors)
		{
			for (Door door2 : room.allDoors)
			{
				if (door == door2)
					continue;
				NavPoint nav1 = getDoorNavPointByRoom(door, room);
				NavPoint nav2 = getDoorNavPointByRoom(door2, room);

				doorneighbours.get(door).put(door2, nav1.getDistance(nav2) + 4 * ColliderC.COLLIDER_RADIUS);
				doorneighbours.get(door2).put(door, nav2.getDistance(nav1) + 4 * ColliderC.COLLIDER_RADIUS);
			}
		}
	}

	private void setBorderNavPointByDirection(TileCorner corner, Direction dir)
	{
		if (!getNavPoints(corner).containsKey(dir))
		{
			switch (dir)
			{
				case N:
				{
					getNavPoints(corner).put(dir,
							new NavPoint(corner.getPos().x - TOLERANCE_RANGE - 2 * ColliderC.COLLIDER_RADIUS / 0.8509f,
									corner.getPos().y - TOLERANCE_RANGE - 2 * ColliderC.COLLIDER_RADIUS / 0.8509f));
					break;
				}
				case W:
				{
					getNavPoints(corner).put(dir,
							new NavPoint(corner.getPos().x + TOLERANCE_RANGE + 2 * ColliderC.COLLIDER_RADIUS / 0.8509f,
									corner.getPos().y - TOLERANCE_RANGE - 2 * ColliderC.COLLIDER_RADIUS / 0.8509f));
					break;
				}
				case S:
				{
					getNavPoints(corner).put(dir,
							new NavPoint(corner.getPos().x + TOLERANCE_RANGE + 2 * ColliderC.COLLIDER_RADIUS / 0.8509f,
									corner.getPos().y + TOLERANCE_RANGE + 2 * ColliderC.COLLIDER_RADIUS / 0.8509f));
					break;
				}
				case E:
				{
					getNavPoints(corner).put(dir,
							new NavPoint(corner.getPos().x - TOLERANCE_RANGE - 2 * ColliderC.COLLIDER_RADIUS / 0.8509f,
									corner.getPos().y + TOLERANCE_RANGE + 2 * ColliderC.COLLIDER_RADIUS / 0.8509f));
					break;
				}
			}
		}
	}

	private static Vector2 r1 = new Vector2();
	private static Vector2 r2 = new Vector2();
	private static Vector2 r3 = new Vector2();
	private static Vector2 r4 = new Vector2();

	private static Vector2 rp1 = new Vector2(), rp2 = new Vector2(), rpos = new Vector2(0, 0);

	static boolean LineIntersectsCollider(Vector2 p1, Vector2 p2, ColliderC col, PositionC pos)
	{
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

	private static boolean rectContainsPoint(Vector2 point, Vector2 min, Vector2 max)
	{
		return (min.x <= point.x && point.x <= max.x && min.y <= point.y && point.y <= max.y);
	}

	private static boolean LineIntersectsLine(Vector2 l1p1, Vector2 l1p2, Vector2 l2p1, Vector2 l2p2)
	{
		float q = (l1p1.y - l2p1.y) * (l2p2.x - l2p1.x) - (l1p1.x - l2p1.x) * (l2p2.y - l2p1.y);
		float d = (l1p2.x - l1p1.x) * (l2p2.y - l2p1.y) - (l1p2.y - l1p1.y) * (l2p2.x - l2p1.x);

		if (d == 0)
		{
			return false;
		}

		float r = q / d;

		q = (l1p1.y - l2p1.y) * (l1p2.x - l1p1.x) - (l1p1.x - l2p1.x) * (l1p2.y - l1p1.y);
		float s = q / d;

		return !(r < 0 || r > 1 || s < 0 || s > 1);
	}

	public NavPoint getDoorNavPointByRoom(Door door, Room room)
	{
		if (getNavPoints(room).contains(getNavPoints(door).values().toArray()[0]))
		{
			return (NavPoint) (getNavPoints(door).values().toArray()[0]);
		}
		if (getNavPoints(room).contains(getNavPoints(door).values().toArray()[1]))
		{
			return (NavPoint) (getNavPoints(door).values().toArray()[1]);
		}
		return null;
	}

	public NavPoint getDoorNavPointbyPartner(Door door, NavPoint nav)
	{
		if (!getNavPoints(door).containsValue(nav))
		{
			return null;
		}
		return getNavPoints(door).values().toArray()[0] == nav ? (NavPoint) getNavPoints(door).values().toArray()[1]
				: (NavPoint) getNavPoints(door).values().toArray()[0];
	}

	public Room getRoomNeigbourByDoor(Room room, Door door)
	{
		if (!room.allDoors.contains(door))
			return null;
		return ((Tile) door.getTileBorder().adjacentTiles.values().toArray()[0]).getRoom() == room
				? ((Tile) door.getTileBorder().adjacentTiles.values().toArray()[1]).getRoom()
				: ((Tile) door.getTileBorder().adjacentTiles.values().toArray()[0]).getRoom();
	}

	public ArrayList<NavPoint> getNavPoints(ColliderC col)
	{
		if (!colliderNavPoints.containsKey(col))
			colliderNavPoints.put(col, new ArrayList<>());
		return colliderNavPoints.get(col);
	}

	public ArrayList<NavPoint> getNavPoints(Room room)
	{
		if (!roomNavPoints.containsKey(room))
			roomNavPoints.put(room, new ArrayList<>());
		return roomNavPoints.get(room);
	}

	public HashMap<Direction, NavPoint> getNavPoints(TileCorner corner)
	{
		if (!tileCornerNavPoints.containsKey(corner))
			tileCornerNavPoints.put(corner, new HashMap<>());
		return tileCornerNavPoints.get(corner);
	}

	public HashMap<Direction, NavPoint> getNavPoints(Door door)
	{
		if (!doorNavPoints.containsKey(door))
			doorNavPoints.put(door, new HashMap<>());
		return doorNavPoints.get(door);
	}

	public HashMap<Door, Float> getDoorNeighbours(Door door)
	{
		if (!doorneighbours.containsKey(door))
			return null;
		return doorneighbours.get(door);
	}
	
	public Pathfinder getPathfinder()
	{
		return pathfinder;
	}
}
