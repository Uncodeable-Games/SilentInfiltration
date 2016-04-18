package de.mih.core.engine.tilemap;

import de.mih.core.engine.tilemap.Tile.Direction;

import java.util.ArrayList;
import java.util.List;

public class Room
{

	public  List<Integer> entitiesInRoom = new ArrayList<>();
	public  List<Wall>    allWalls       = new ArrayList<>();
	public  List<Door>    allDoors       = new ArrayList<>();
	private List<Tile>    tiles          = new ArrayList<>();

	public void addBordersfromTile(Tile tile)
	{
		for (Direction dir : new Direction[]{Direction.N, Direction.S, Direction.W, Direction.E})
		{
			TileBorder b = tile.getBorder(dir);
			if (b.hasCollider())
			{
				if (b.isDoor() && !allDoors.contains(b.getDoor()))
				{
					allDoors.add(b.getDoor());
				}
				if (b.isWall() && !allWalls.contains(b.getWall()))
				{
					allWalls.add(b.getWall());
				}
			}
		}
	}

	public void addTile(Tile tile)
	{
		this.tiles.add(tile);
	}

	public List<Tile> getTiles()
	{
		return this.tiles;
	}

	public void addEntity(int entityId)
	{
		this.entitiesInRoom.add(entityId);
	}

	public void removeEntity(int entityId)
	{
		if (this.entitiesInRoom.contains(entityId))
		{
			this.entitiesInRoom.remove(entityId);
		}
	}
}
