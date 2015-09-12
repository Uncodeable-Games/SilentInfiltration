package de.mih.core.engine.tilemap.borders;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import de.mih.core.engine.tilemap.Tile;

public class TileBorder {
	
	Tile adjacentTile1, adjacentTile2;
	BorderCollider collider = null;
	public float angle;
	
	//Vector2 beginn, end;
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
		if(adjacentTile1 == null)
			adjacentTile1 = tile;
		else if(adjacentTile2 == null)
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
	public void setBorderCollider(BorderCollider collider)
	{
		this.collider = collider;
	}
	
	public boolean hasBorderCollider()
	{
		return this.collider != null;
	}
	
	public BorderCollider getBorderCollider()
	{
		return this.collider;
	}
}