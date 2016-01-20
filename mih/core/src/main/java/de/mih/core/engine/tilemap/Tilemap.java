package de.mih.core.engine.tilemap;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.physic.Line;
import de.mih.core.engine.tilemap.Tile.Direction;
import de.mih.core.engine.tilemap.TileBorder.Facing;
import de.mih.core.game.components.BorderC;
import de.mih.core.game.components.ColliderC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.VelocityC;

public class Tilemap
{

	float TILESIZE;

	String name;
	public List<Room> rooms = new ArrayList<>();
	Tile[][] tilemap;
	private List<TileBorder> borders = new ArrayList<>();
	private int length;

	private int width;

	private EntityManager entityManager;

	public List<Line> colLines = new ArrayList<>();

	public Tilemap(int length, int width, float tilesize, EntityManager entityManager)
	{
		this.setLength(length);
		this.setWidth(width);
		this.TILESIZE = tilesize;

		this.tilemap = new Tile[width][length];
		this.createTilemap();
		this.calculatePhysicBody();
		this.entityManager = entityManager;
		// this.calculateRooms();
	}

	public Tile getTileAt(float x, float y)
	{
		if (x >= 0 && x < width && y >= 0 && y < length)
		{
			return tilemap[(int) x][(int) y];
		}
		return null;
	}

	public Tile getTileAt(int x, int y)
	{
		if (x >= 0 && x < width && y >= 0 && y < length)
		{
			return tilemap[x][y];
		}
		return null;
	}

	public Room getRoomAt(int x, int y)
	{
		return getTileAt(x, y).getRoom();
	}

	public int coordToIndex_x(float x)
	{
		return Math.round(x / TILESIZE);
	}

	public int coordToIndex_z(float z)
	{
		return Math.round(z / TILESIZE);
	}

	private void createTilemap()
	{
		int border_count = 0;
		for (int x = 0; x < getWidth(); x++)
		{
			for (int y = 0; y < getLength(); y++)
			{
				Tile tmp = new Tile(TILESIZE * (float) x + TILESIZE / 2f, 0, TILESIZE * (float) y + TILESIZE / 2f,
						this);
				tmp.setX(x);
				tmp.setY(y);
				for (Direction direction : Direction.values())
				{
					Tile neighbour = null;
					TileBorder.Facing facing = null;
					Vector3 borderCenterOffset = new Vector3();
					float angle = 0f;
					if (direction == Direction.E)
					{
						borderCenterOffset.x -= TILESIZE / 2f;
						angle = 90f;
						if (x > 0)
							neighbour = tilemap[x - 1][y];
						facing = Facing.NS;
					}

					else if (direction == Direction.W)
					{
						borderCenterOffset.x += TILESIZE / 2f;
						if (x < width - 1)
							neighbour = tilemap[x + 1][y];
						facing = Facing.NS;

					}

					else if (direction == Direction.N)
					{
						borderCenterOffset.z -= TILESIZE / 2f;
						if (y > 0)
							neighbour = tilemap[x][y - 1];
						facing = Facing.WE;

					}
					else if (direction == Direction.S)
					{
						borderCenterOffset.z += TILESIZE / 2f;
						if (y < length - 1)
							neighbour = tilemap[x][y + 1];
						facing = Facing.WE;

					}
					if (!tmp.hasBorder(direction))
					{
						/*
						 * 
						 */

						// if(!tmp.hasBorder(direction))
						// {
						Vector3 borderCenter = tmp.center.cpy();
						borderCenter.add(borderCenterOffset);

						TileBorder border;// = new TileBorder(borderCenter);

						if (neighbour != null && neighbour.hasBorder(direction.getOppositeDirection()))
						{
							border = neighbour.getBorder(direction.getOppositeDirection());
						}
						else
						{
							border = new TileBorder(borderCenter);
							if (neighbour != null)
							{
								border.setAdjacent(neighbour);
								neighbour.setBorder(direction.getOppositeDirection(), border);
							}
						}

						border_count++;
						border.angle = angle;
						border.setAdjacent(tmp);

						border.facing = facing;
						this.borders.add(border);
						tmp.setBorder(direction, border);
					}
					// }
				}
				tilemap[x][y] = tmp;
			}
		}
		System.out.println("border count: " + border_count);
		System.out.println("border size: " + borders.size());
		List<TileBorder> check = new ArrayList<>();
		for (TileBorder border : borders)
		{
			if (!check.contains(border))
				check.add(border);
		}
		System.out.println("check size: " + check.size());
		connectTileBorders();
	}

	private void connectTileBorders()
	{
		for (int x = 0; x < getWidth(); x++)
		{
			for (int y = 0; y < getLength(); y++)
			{
				if (x > 0 && y < getLength())
				{
					tilemap[x - 1][y].getBorder(Direction.N).west = tilemap[x][y].getBorder(Direction.N);
					tilemap[x][y].getBorder(Direction.N).east = tilemap[x - 1][y].getBorder(Direction.N);
					tilemap[x - 1][y].getBorder(Direction.S).west = tilemap[x][y].getBorder(Direction.S);
					tilemap[x][y].getBorder(Direction.S).east = tilemap[x - 1][y].getBorder(Direction.S);
				}
				if (y > 0 && x < getWidth())
				{
					tilemap[x][y - 1].getBorder(Direction.W).south = tilemap[x][y].getBorder(Direction.W);
					tilemap[x][y].getBorder(Direction.W).north = tilemap[x][y - 1].getBorder(Direction.W);
					tilemap[x][y - 1].getBorder(Direction.E).south = tilemap[x][y].getBorder(Direction.E);
					tilemap[x][y].getBorder(Direction.E).north = tilemap[x][y - 1].getBorder(Direction.E);
				}
			}

		}
	}

	public void calculatePhysicBody()
	{
		List<TileBorder> allBorders = new ArrayList<>();
		allBorders.addAll(this.borders);
		TileBorder current = allBorders.get(0);
		while (!allBorders.isEmpty() && current != null)
		{
			allBorders.remove(current);
			if (!current.hasColliderEntity())
			{
				if (!allBorders.isEmpty())
					current = allBorders.get(0);
				continue;
			}
			TileBorder east, west, north, south;
			east = current;
			west = current;
			while (true)
			{
				allBorders.remove(east);
				if (east.east != null && east.east.hasColliderEntity())
					east = east.east;
				else
					break;
			}
			while (true)
			{
				allBorders.remove(west);
				if (west.west != null && west.west.hasColliderEntity())
					west = west.west;
				else
					break;
			}
			if ((current.west != null && current.west.hasColliderEntity())
					|| (current.east != null && current.east.hasColliderEntity()) || current.facing == Facing.WE)
			{
				Vector2 vEast = new Vector2(east.center.x - 1.0f, east.center.z);
				Vector2 vWest = new Vector2(west.center.x + 1.0f, west.center.z);
				colLines.add(new Line(vEast, vWest));
			}

			north = current;
			south = current;
			while (true)
			{
				allBorders.remove(north);
				if (north.north != null && north.north.hasColliderEntity())
					north = north.north;
				else
					break;
			}
			while (true)
			{
				allBorders.remove(south);
				if (south.south != null && south.south.hasColliderEntity())
					south = south.south;
				else
					break;
			}
			if ((current.north != null && current.north.hasColliderEntity())
					|| (current.south != null && current.south.hasColliderEntity()) || current.facing == Facing.NS)
			{
				Vector2 vNorth = new Vector2(north.center.x, north.center.z - 1.0f);
				Vector2 vSouth = new Vector2(south.center.x, south.center.z + 1.0f);
				colLines.add(new Line(vNorth, vSouth));
			}

			if (!allBorders.isEmpty())
				current = allBorders.get(0);
		}

	}

	public void setRoomforTile(Room r, Tile t)
	{
		t.setRoom(r);
		if (t.hasNeighbour(Direction.E) && !t.getBorder(Direction.E).hasColliderEntity()
				&& !t.getNeighour(Direction.E).hasRoom())
		{
			setRoomforTile(r, t.getNeighour(Direction.E));
		}
		if (t.hasNeighbour(Direction.N) && !t.getBorder(Direction.N).hasColliderEntity()
				&& !t.getNeighour(Direction.N).hasRoom())
		{
			setRoomforTile(r, t.getNeighour(Direction.N));
		}
		if (t.hasNeighbour(Direction.W) && !t.getBorder(Direction.W).hasColliderEntity()
				&& !t.getNeighour(Direction.W).hasRoom())
		{
			setRoomforTile(r, t.getNeighour(Direction.W));
		}
		if (t.hasNeighbour(Direction.S) && !t.getBorder(Direction.S).hasColliderEntity()
				&& !t.getNeighour(Direction.S).hasRoom())
		{
			setRoomforTile(r, t.getNeighour(Direction.S));
		}
	}

	public void calculateRooms()
	{
		for (Room r : rooms)
		{
			for (Tile t : r.getTiles())
				t.setRoom(null);
			r.tiles.clear();
		}
		this.rooms.clear();

		for (int x = 0; x < getWidth(); x++)
		{
			for (int y = 0; y < getLength(); y++)
			{
				if (!getTileAt(x, y).hasRoom())
				{
					Room r = new Room();
					rooms.add(r);
					setRoomforTile(r, getTileAt(x, y));
				}
			}
		}

		for (Room room : this.rooms)
		{
			room.calculateCenter();
		}

		for (int i = 0; i < entityManager.entityCount; i++)
		{
			if (entityManager.hasComponent(i, PositionC.class) && entityManager.hasComponent(i, ColliderC.class)
					&& !entityManager.hasComponent(i, VelocityC.class) && !entityManager.hasComponent(i, BorderC.class))
			{
				Room r = getTileAt(coordToIndex_x(entityManager.getComponent(i, PositionC.class).getX()),
						coordToIndex_x(entityManager.getComponent(i, PositionC.class).getZ())).getRoom();
				r.entitiesInRoom.add(i);
			}
		}

		for (Room r : rooms)
		{
			r.calculateVisibility();
			r.routeNavPoints();
		}
	}

	public float getTILESIZE()
	{
		return TILESIZE;
	}

	public int getLength()
	{
		return length;
	}

	public void setLength(int length)
	{
		this.length = length;
	}

	public int getWidth()
	{
		return width;
	}

	public void setWidth(int width)
	{
		this.width = width;
	}

	public List<TileBorder> getBorders()
	{
		return borders;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}
