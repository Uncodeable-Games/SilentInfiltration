package de.mih.core.engine.tilemap;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;

import de.mih.core.engine.tilemap.Tile.Direction;
import de.mih.core.engine.tilemap.borders.BorderCollider;
import de.mih.core.engine.tilemap.borders.TileBorder;
import de.mih.core.game.components.PositionC;

public class Tile {
	public enum Direction
	{
		N, S, W, E;
		
		private Direction opposite;

	    static {
	        N.opposite = S;
	        S.opposite = N;
	        E.opposite = W;
	        W.opposite = E;
	    }

	    public Direction getOppositeDirection() {
	        return opposite;
	    }

		public static Direction parseDirection(String parse) throws IllegalArgumentException {
			String tmp = parse.toUpperCase();
			if(tmp.equals("N"))
				return N;
			if(tmp.equals("S"))
				return S;
			if(tmp.equals("E"))
				return E;
			if(tmp.equals("W"))
				return W;
			throw new IllegalArgumentException("Cannot parse \"" + parse + " to a direction!");
			
		}
	}
	
	Map<Direction,TileBorder> borders = new HashMap<>();
	
	public boolean hasNeighbour(Direction direction)
	{
		return borders.containsKey(direction);
	}
	
	public Tile getNeighour(Direction direction)
	{
		return borders.get(direction).getAdjacentTile(this);
	}
	
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
		border.setAdjacent(this);
		borders.put(direction, border);
	}
	
	public boolean hasBorder(Direction direction)
	{
		return borders.containsKey(direction);
	}
	
	public TileBorder getBorder(Direction direction)
	{
		return borders.get(direction);
	}
	
	public void addBorderCollider(BorderCollider collider, Direction direction)
	{
		if(borders.containsKey(direction))
		{
			borders.get(direction).setBorderCollider(collider);
		}
	}

	public Vector2 getCenter() {
		return center;
	}
	
}