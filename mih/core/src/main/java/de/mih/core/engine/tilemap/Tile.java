package de.mih.core.engine.tilemap;

import java.util.Map;

import com.badlogic.gdx.math.Vector2;

import de.mih.core.engine.tilemap.borders.BorderCollider;
import de.mih.core.engine.tilemap.borders.TileBorder;

public class Tile {
	public enum Direction
	{
		N, S, W, E
	}
	
	Map<Direction,TileBorder> borders;
	Vector2 center;
	
	public Tile(Vector2 center)
	{
		this.center = center;
	}
	
	public Tile(float x, float y)
	{
		this.center = new Vector2(x, y);
	}
	
	public void setBorder(Direction direction, TileBorder border)
	{
		borders.put(direction, border);
	}
	
	public void addBorderCollider(BorderCollider collider, Direction direction)
	{
		if(borders.containsKey(direction))
		{
			collider.setFacing(direction);
			borders.get(direction).setBorderCollider(collider);
		}
	}
	
}