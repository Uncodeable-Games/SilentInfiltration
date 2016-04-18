package de.mih.core.engine.tilemap;

import com.badlogic.gdx.math.Vector2;
import de.mih.core.engine.tilemap.Tile.Direction;
import de.mih.core.game.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TileBorder
{
	public enum Facing
	{
		NS, WE
	}

	public float angle;

	public Facing facing;

	Vector2 center;

	HashMap<Direction, TileCorner> corners = new HashMap<>();
	public HashMap<Direction, Tile> adjacentTiles = new HashMap<Direction, Tile>();

	public TileBorder(float x, float y)
	{
		this(new Vector2(x, y));
	}

	public TileBorder(Vector2 center)
	{
		this.center = center;
	}

	public Vector2 getCenter()
	{
		return center;
	}

	public void setAdjacent(Tile tile, Direction dir)
	{
		adjacentTiles.put(dir, tile);
	}

	public Tile getAdjacentTile(Tile tile)
	{
		for (Direction dir : Direction.values())
		{
			if (adjacentTiles.get(dir) == tile && adjacentTiles.get(dir.getOppositeDirection()) != null)
			{
				return adjacentTiles.get(dir.getOppositeDirection());
			}
		}
		return null;
	}

	public Tile getAdjacentTile(Direction dir)
	{
		return adjacentTiles.get(dir);
	}

	public boolean isDoor()
	{
		return Door.doors.containsKey(this);
	}

	public Door getDoor()
	{
		if (isDoor())
			return Door.doors.get(this);
		return null;
	}

	public void setToDoor(int colliderentity, String blueprint)
	{
		Door.doors.put(this, new Door(this,colliderentity,blueprint));
	}

	public boolean isWall()
	{
		return Wall.walls.containsKey(this);
	}

	public Wall getWall()
	{
		if (isWall())
			return Wall.walls.get(this);
		return null;
	}

	public void setToWall(int colliderentity, String blueprint)
	{
		Wall.walls.put(this, new Wall(this,colliderentity,blueprint));
	}

	public boolean hasCollider()
	{
		return isWall() || isDoor();
	}

	public int getColliderEntity()
	{
		if (!hasCollider())
			return -1;
		if (isDoor())
			return getDoor().getColliderEntity();
		return getWall().getColliderEntity();
	}

	public String getBlueprint(){
		if (!hasCollider()) return null;
		if (isDoor())
			return getDoor().getBlueprint();
		return getWall().getBlueprint();
	}
	
	public void setColliderEntity(int entity)
	{
		if (!hasCollider())
			return;
		if (isWall())
		{
			getWall().setColliderEntity(entity);
		}
		else
		{
			getDoor().setColliderEntity(entity);
		}
	}

	public void removeCollider()
	{
		Game.getCurrentGame().getEntityManager().removeEntity(getColliderEntity());
		if (isDoor())
		{
			getDoor().setColliderEntity(-1);
			Door.doors.remove(this);
		}
		if (isWall())
		{
			getWall().setColliderEntity(-1);
			Wall.walls.remove(this);
		}
	}

	public boolean isHorizontal()
	{
		return corners.containsKey(Direction.E);
	}

	public boolean isVertical()
	{
		return !isHorizontal();
	}

	public Vector2 getPos()
	{
		Tile      tile = null;
		Direction dir  = null;
		if (adjacentTiles.containsKey(Direction.N))
		{
			dir = Direction.N;
			tile = adjacentTiles.get(Direction.N);
		}
		if (adjacentTiles.containsKey(Direction.S))
		{
			dir = Direction.S;
			tile = adjacentTiles.get(Direction.S);
		}
		if (adjacentTiles.containsKey(Direction.E))
		{
			dir = Direction.E;
			tile = adjacentTiles.get(Direction.E);
		}
		if (adjacentTiles.containsKey(Direction.W))
		{
			dir = Direction.W;
			tile = adjacentTiles.get(Direction.W);
		}
		Vector2 pos = new Vector2();
		switch (dir)
		{
			case N:
			{
				pos.x = tile.center.x;
				pos.y = tile.center.y + tile.getTilemap().getTILESIZE() / 2f;
				break;
			}
			case E:
			{
				pos.x = tile.center.x - tile.getTilemap().getTILESIZE() / 2f;
				pos.y = tile.center.y;
				break;
			}
			case S:
			{
				pos.x = tile.center.x;
				pos.y = tile.center.y - tile.getTilemap().getTILESIZE() / 2f;
				break;
			}
			case W:
			{
				pos.x = tile.center.x + tile.getTilemap().getTILESIZE() / 2f;
				pos.y = tile.center.y;
				break;
			}
		}
		return center;
	}

	public List<Tile> getTiles()
	{
		return new ArrayList<Tile>(adjacentTiles.values());
	}

	// TODO besser machen!
	public TileBorder getAdjacentBorder(Direction direction)
	{
		TileCorner corner = getCorner(direction);
		if (null == corner)
		{
			return null;
		}
		return corner.adjacentBorders.get(direction);
	}

	public TileCorner getCorner(Direction direction)
	{
		return this.corners.get(direction);
	}
}