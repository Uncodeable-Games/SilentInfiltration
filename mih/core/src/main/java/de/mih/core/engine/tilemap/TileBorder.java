package de.mih.core.engine.tilemap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import de.mih.core.engine.tilemap.Tile.Direction;
import de.mih.core.game.Game;
import de.mih.core.game.components.ColliderC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.VisualC;

public class TileBorder
{
	public enum Facing
	{
		NS, WE
	}

	public float angle;

	public Facing facing;

	int colliderEntity = -1;
	Vector3 center;

	HashMap<Direction, TileCorner> corners = new HashMap<>();
	public HashMap<Direction, Tile> adjacentTiles = new HashMap<Direction, Tile>();

	public TileBorder(float x, float y, float z)
	{
		this(new Vector3(x, y, z));
	}

	public TileBorder(Vector3 center)
	{
		this.center = center;
	}

	public Vector3 getCenter()
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

	public void removeColliderEntity()
	{
		// TODO: resolve to outside maybe? why should the tileborder be deleting
		// entities?
		Game.getCurrentGame().getEntityManager().removeEntity(this.colliderEntity);
		this.colliderEntity = -1;
	}

	public void setColliderEntity(int entityID)
	{
		this.colliderEntity = entityID;

		Game.getCurrentGame().getEntityManager().getComponent(entityID, PositionC.class).setPos(this.center);
		Game.getCurrentGame().getEntityManager().getComponent(entityID, PositionC.class).setAngle(this.angle);
	}

	public int getColliderEntity()
	{
		return this.colliderEntity;
	}

	public boolean hasColliderEntity()
	{
		return this.colliderEntity > -1;
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
		Tile tile = null;
		Direction dir = null;
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
				pos.y = tile.center.z + tile.getTilemap().getTILESIZE() / 2f;
				break;
			}
			case E:
			{
				pos.x = tile.center.x - tile.getTilemap().getTILESIZE() / 2f;
				pos.y = tile.center.z;
				break;
			}
			case S:
			{
				pos.x = tile.center.x;
				pos.y = tile.center.z - tile.getTilemap().getTILESIZE() / 2f;
				break;
			}
			case W:
			{
				pos.x = tile.center.x + tile.getTilemap().getTILESIZE() / 2f;
				pos.y = tile.center.z;
				break;
			}
		}
		return pos;
	}

	public boolean hasSameRoom(TileBorder door)
	{
		for (Room room : Game.getCurrentGame().getTilemap().getRooms())
		{
			if (room.allDoors.contains(this) && room.allDoors.contains(door))
				return true;
		}
		return false;
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