package de.mih.core.engine.tilemap;

import com.badlogic.gdx.math.Vector2;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.physic.Line;
import de.mih.core.engine.tilemap.Tile.Direction;
import de.mih.core.engine.tilemap.TileBorder.Facing;
import de.mih.core.game.components.BorderC;
import de.mih.core.game.components.ColliderC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.VelocityC;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

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
		this.entityManager = entityManager;
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
		if (getTileAt(x, y) == null)
			return null;
		return getTileAt(x, y).getRoom();
	}
	
	public Room getRoomAt(float x, float y)
	{
		return getRoomAt(coordToIndex(x), coordToIndex(y));
	}

	private int coordToIndex(float x)
	{
		return (int) (x / TILESIZE);
	}

	// TODO: Check Directions!
	private void createTilemap()
	{
		for (int x = 0; x < getWidth(); x++)
		{
			for (int y = 0; y < getLength(); y++)
			{
				Tile tmp = new Tile(TILESIZE * (float) x + TILESIZE / 2f, TILESIZE * (float) y + TILESIZE / 2f,
						this);
				tmp.setX(x);
				tmp.setY(y);
				tilemap[x][y] = tmp;
			}
		}
		for (int x = 0; x < getWidth(); x++)
		{
			for (int y = 0; y < getLength(); y++)
			{

				TileBorder newtb;
				Tile       temp;

				// North Border
				if (y == 0)
				{
					newtb = new TileBorder(new Vector2(tilemap[x][y].center).add(0, -TILESIZE / 2f));
					tilemap[x][y].setBorder(Direction.N, newtb);
					borders.add(newtb);
					newtb.facing = Facing.WE;
				}
				else
				{
					temp = tilemap[x][y - 1];
					if (temp.borders.containsKey(Direction.S))
					{
						tilemap[x][y].setBorder(Direction.N, temp.getBorder(Direction.S));
					}
					else
					{
						newtb = new TileBorder(new Vector2(tilemap[x][y].center).add(0, -TILESIZE / 2f));
						tilemap[x][y].setBorder(Direction.N, newtb);
						borders.add(newtb);
						temp.setBorder(Direction.S, newtb);
						newtb.facing = Facing.WE;
					}
				}

				// West Border
				if (x == 0)
				{
					newtb = new TileBorder(new Vector2(tilemap[x][y].center).add(-TILESIZE / 2f, 0));
					newtb.angle = 90f;
					tilemap[x][y].setBorder(Direction.W, newtb);
					borders.add(newtb);
					newtb.facing = Facing.NS;
				}
				else
				{
					temp = tilemap[x - 1][y];
					if (temp.borders.containsKey(Direction.E))
					{
						tilemap[x][y].setBorder(Direction.W, temp.getBorder(Direction.E));
					}
					else
					{
						newtb = new TileBorder(new Vector2(tilemap[x][y].center).add(-TILESIZE / 2f, 0));
						newtb.angle = 90f;
						tilemap[x][y].setBorder(Direction.W, newtb);
						borders.add(newtb);
						temp.setBorder(Direction.E, newtb);
						newtb.facing = Facing.NS;
					}
				}

				// South Border
				if (y == tilemap[0].length - 1)
				{
					newtb = new TileBorder(new Vector2(tilemap[x][y].center).add(0, TILESIZE / 2f));
					tilemap[x][y].setBorder(Direction.S, newtb);
					borders.add(newtb);
					newtb.facing = Facing.WE;
				}
				else
				{
					temp = tilemap[x][y + 1];
					if (temp.borders.containsKey(Direction.N))
					{
						tilemap[x][y].setBorder(Direction.S, temp.getBorder(Direction.N));
					}
					else
					{
						newtb = new TileBorder(new Vector2(tilemap[x][y].center).add(0, TILESIZE / 2f));
						tilemap[x][y].setBorder(Direction.S, newtb);
						borders.add(newtb);
						temp.setBorder(Direction.N, newtb);
						newtb.facing = Facing.WE;
					}
				}

				// East Border
				if (x == tilemap.length - 1)
				{
					newtb = new TileBorder(new Vector2(tilemap[x][y].center).add(TILESIZE / 2f, 0));
					newtb.angle = 90f;
					tilemap[x][y].setBorder(Direction.E, newtb);
					borders.add(newtb);
					newtb.facing = Facing.NS;
				}
				else
				{
					temp = tilemap[x + 1][y];
					if (temp.borders.containsKey(Direction.W))
					{
						tilemap[x][y].setBorder(Direction.E, temp.getBorder(Direction.W));
					}
					else
					{
						newtb = new TileBorder(new Vector2(tilemap[x][y].center).add(TILESIZE / 2f, 0));
						newtb.angle = 90f;
						tilemap[x][y].setBorder(Direction.E, newtb);
						borders.add(newtb);
						temp.setBorder(Direction.W, newtb);
						newtb.facing = Facing.NS;
					}
				}
			}
		}

		for (int x = 0; x < getWidth(); x++)
		{
			for (int y = 0; y < getLength(); y++)
			{

				TileCorner tmp = new TileCorner();

				tilemap[x][y].setCorner(Direction.N, tmp);
				tmp.adjacentTiles.put(Direction.S, tilemap[x][y]);

				tmp.adjacentBorders.put(Direction.E, tilemap[x][y].getBorder(Direction.N));
				tilemap[x][y].getBorder(Direction.N).corners.put(Direction.W, tmp);
				tmp.adjacentBorders.put(Direction.S, tilemap[x][y].getBorder(Direction.W));
				tilemap[x][y].getBorder(Direction.W).corners.put(Direction.N, tmp);

				if (y != 0)
				{
					tilemap[x][y - 1].setCorner(Direction.W, tmp);
					tmp.adjacentTiles.put(Direction.E, tilemap[x][y - 1]);

					tmp.adjacentBorders.put(Direction.N, tilemap[x][y - 1].getBorder(Direction.W));
					tilemap[x][y - 1].getBorder(Direction.W).corners.put(Direction.S, tmp);
				}

				if (x != 0)
				{
					tilemap[x - 1][y].setCorner(Direction.E, tmp);
					tmp.adjacentTiles.put(Direction.W, tilemap[x - 1][y]);

					tmp.adjacentBorders.put(Direction.W, tilemap[x - 1][y].getBorder(Direction.N));
					tilemap[x - 1][y].getBorder(Direction.N).corners.put(Direction.E, tmp);
				}
				if (x != 0 && y != 0)
				{
					tilemap[x - 1][y - 1].setCorner(Direction.S, tmp);
					tmp.adjacentTiles.put(Direction.N, tilemap[x - 1][y - 1]);
				}
			}
		}

		for (int x = 0; x < getWidth(); x++)
		{
			TileCorner tmp = new TileCorner();

			tilemap[x][getLength() - 1].setCorner(Direction.W, tmp);
			tmp.adjacentTiles.put(Direction.E, tilemap[x][getLength() - 1]);

			tmp.adjacentBorders.put(Direction.E, tilemap[x][getLength() - 1].getBorder(Direction.S));
			tilemap[x][getLength() - 1].getBorder(Direction.S).corners.put(Direction.W, tmp);
			tmp.adjacentBorders.put(Direction.N, tilemap[x][getLength() - 1].getBorder(Direction.W));
			tilemap[x][getLength() - 1].getBorder(Direction.W).corners.put(Direction.S, tmp);

			if (x != 0)
			{
				tilemap[x - 1][getLength() - 1].setCorner(Direction.S, tmp);
				tmp.adjacentTiles.put(Direction.N, tilemap[x - 1][getLength() - 1]);
				tmp.adjacentBorders.put(Direction.W, tilemap[x - 1][getLength() - 1].getBorder(Direction.S));
				tilemap[x - 1][getLength() - 1].getBorder(Direction.S).corners.put(Direction.E, tmp);
			}
		}

		for (int y = 0; y < getLength(); y++)
		{
			TileCorner tmp = new TileCorner();

			tilemap[getWidth() - 1][y].setCorner(Direction.E, tmp);
			tmp.adjacentTiles.put(Direction.W, tilemap[getWidth() - 1][y]);

			tmp.adjacentBorders.put(Direction.W, tilemap[getWidth() - 1][y].getBorder(Direction.N));
			tilemap[getWidth() - 1][y].getBorder(Direction.N).corners.put(Direction.E, tmp);
			tmp.adjacentBorders.put(Direction.S, tilemap[getWidth() - 1][y].getBorder(Direction.E));
			tilemap[getWidth() - 1][y].getBorder(Direction.E).corners.put(Direction.N, tmp);

			if (y != 0)
			{
				tilemap[getWidth() - 1][y - 1].setCorner(Direction.S, tmp);
				tmp.adjacentTiles.put(Direction.N, tilemap[getWidth() - 1][y - 1]);

				tmp.adjacentBorders.put(Direction.N, tilemap[getWidth() - 1][y - 1].getBorder(Direction.E));
				tilemap[getWidth() - 1][y - 1].getBorder(Direction.E).corners.put(Direction.S, tmp);
			}
		}

		TileCorner tmp = new TileCorner();

		tilemap[getWidth() - 1][getLength() - 1].setCorner(Direction.S, tmp);
		tmp.adjacentTiles.put(Direction.N, tilemap[getWidth() - 1][getLength() - 1]);

		tmp.adjacentBorders.put(Direction.N, tilemap[getWidth() - 1][getLength() - 1].getBorder(Direction.E));
		tilemap[getWidth() - 1][getLength() - 1].getBorder(Direction.E).corners.put(Direction.S, tmp);
		tmp.adjacentBorders.put(Direction.W, tilemap[getWidth() - 1][getLength() - 1].getBorder(Direction.S));
		tilemap[getWidth() - 1][getLength() - 1].getBorder(Direction.S).corners.put(Direction.E, tmp);
	}

	public void calculatePhysicBody()
	{
		//System.out.println("THIS BORDERS: " + borders.size());
		List<TileBorder> allBorders = new ArrayList<>();
		allBorders.addAll(this.borders);
		TileBorder current = allBorders.get(0);
		while (!allBorders.isEmpty() && current != null)
		{
			allBorders.remove(current);
			if (!current.hasCollider())
			{
				if (!allBorders.isEmpty())
					current = allBorders.get(0);
				continue;
			}
			TileBorder east, west, north, south, tmp;
			east = current;
			west = current;
			while (true)
			{
				allBorders.remove(east);
				tmp = east.getAdjacentBorder(Direction.E);
				if (tmp != null && tmp.hasCollider())
					east = tmp;
				else
					break;
			}
			while (true)
			{
				allBorders.remove(west);
				tmp = west.getAdjacentBorder(Direction.W);
				if (tmp != null && tmp.hasCollider())
					west = tmp;
				else
					break;
			}
			if ((current.getAdjacentBorder(Direction.W) != null
					&& current.getAdjacentBorder(Direction.W).hasCollider())
					|| (current.getAdjacentBorder(Direction.E) != null
					&& current.getAdjacentBorder(Direction.E).hasCollider())
					|| current.facing == Facing.WE)
			{
				Vector2 vEast = new Vector2(east.center.x + 1.0f, east.center.y);
				Vector2 vWest = new Vector2(west.center.x - 1.0f, west.center.y);
				colLines.add(new Line(vEast, vWest));
			}

			north = current;
			south = current;
			while (true)
			{
				allBorders.remove(north);
				tmp = north.getAdjacentBorder(Direction.N);
				if (tmp != null && tmp.hasCollider())
					north = tmp;
				break;
			}
			while (true)
			{
				allBorders.remove(south);
				tmp = south.getAdjacentBorder(Direction.S);
				if (tmp != null && tmp.hasCollider())
					south = tmp;
				else
					break;
			}
			if ((current.getAdjacentBorder(Direction.N) != null
					&& current.getAdjacentBorder(Direction.N).hasCollider())
					|| (current.getAdjacentBorder(Direction.S) != null
					&& current.getAdjacentBorder(Direction.S).hasCollider())
					|| current.facing == Facing.NS)
			{
				Vector2 vNorth = new Vector2(north.center.x, north.center.y - 1.0f);
				Vector2 vSouth = new Vector2(south.center.x, south.center.y + 1.0f);
				colLines.add(new Line(vNorth, vSouth));
			}
			//
			if (!allBorders.isEmpty())
				current = allBorders.get(0);
		}
	}

	public void calculateRooms()
	{
		this.rooms.clear();

		LinkedList<Tile> tileList  = new LinkedList<>();
		LinkedList<Tile> doneTiles = new LinkedList<>();

		for (int x = 0; x < getWidth(); x++)
			for (int y = 0; y < getLength(); y++)
				tileList.add(getTileAt(x, y));

		Stack<Tile> tileStack = new Stack<>();

		for (Tile tile : tileList)
		{
			if (tileStack.isEmpty() && !tile.hasRoom())
			{
				tileStack.add(tile);
			}
			while (!tileStack.isEmpty())
			{
				Tile t = tileStack.pop();

				Room r = new Room();

				for (Direction dir : new Direction[]{Direction.E, Direction.N, Direction.S, Direction.W}){
					if (t.hasNeighbour(dir) && !t.getBorder(dir).hasCollider() && t.getNeighour(dir).hasRoom())
					{
						r = t.getNeighour(dir).getRoom();
						break;
					}
				}

				for (Direction dir : new Direction[]{Direction.E, Direction.N, Direction.S, Direction.W})
				{
					if (t.hasNeighbour(dir) && !t.getBorder(dir).hasCollider() && !t.getNeighour(dir).hasRoom())
					{
						tileStack.add(t.getNeighour(dir));
					}
				}
				t.setRoom(r);
				r.addTile(t);
				r.addBordersfromTile(t);

				if (!rooms.contains(r)) rooms.add(r);
			}
		}

		for (int i = 0; i < entityManager.entityCount; i++)
		{
			if (entityManager.hasComponent(i, PositionC.class) && entityManager.hasComponent(i, ColliderC.class)
					&& !entityManager.hasComponent(i, VelocityC.class) && !entityManager.hasComponent(i, BorderC.class))
			{
				Room r = getTileAt(entityManager.getComponent(i, PositionC.class).getX(),
						entityManager.getComponent(i, PositionC.class).getZ()).getRoom();
				r.addEntity(i);
			}
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

	public List<Room> getRooms()
	{
		return rooms;
	}
}
