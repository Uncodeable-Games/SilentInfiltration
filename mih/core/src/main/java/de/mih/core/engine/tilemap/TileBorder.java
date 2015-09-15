package de.mih.core.engine.tilemap;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.math.Vector3;

import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.game.components.PositionC;

public class TileBorder {
	
	Tile adjacentTile1, adjacentTile2;
	public float angle;
	
	int colliderEntity = -1;
	Vector3 center;
	
	public TileBorder(float x, float y, float z)
	{
		this(new Vector3(x,y,z));
	}
	
	public TileBorder(Vector3 center)
	{
		this.center = center;
	}
	
	public Vector3 getCenter()
	{
		return center;
	}
	
	public void setAdjacent(Tile tile)
	{
		if(adjacentTile1 == null || tile == adjacentTile1)
			adjacentTile1 = tile;
		else if(adjacentTile2 == null || tile == adjacentTile2)
			adjacentTile2 = tile;
		else
		{
			System.out.println("ERROR");
		}
	}
	public Tile getAdjacentTile(Tile tile)
	{
		return tile == adjacentTile1 ? adjacentTile2 : adjacentTile1;
	}
	
	public void removeColliderEntity()
	{
		EntityManager.getInstance().removeEntity(this.colliderEntity);
		this.colliderEntity = -1;
	}
	public void setColliderEntity(int entityID)
	{
		this.colliderEntity = entityID;
		EntityManager.getInstance().getComponent(entityID, PositionC.class).position = this.center;
		EntityManager.getInstance().getComponent(entityID, PositionC.class).angle = this.angle;

	}
	
	public int getColliderEntity()
	{
		return this.colliderEntity;
	}
	
	public boolean hasColliderEntity()
	{
		return this.colliderEntity > -1;// && EntityManager.getInstance().hasComponent(colliderEntity, ColliderC.class);
	}

	public List<Tile> getTiles() {
		List<Tile> adjacentTiles = new ArrayList<>();
		if(adjacentTile1 != null)
		{
			adjacentTiles.add(adjacentTile1);
		}
		if(adjacentTile2 != null)
		{
			adjacentTiles.add(adjacentTile2);
		}
		return adjacentTiles;
	}
	
}