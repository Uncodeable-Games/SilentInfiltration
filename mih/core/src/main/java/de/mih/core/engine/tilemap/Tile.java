package de.mih.core.engine.tilemap;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.math.Vector3;

import de.mih.core.engine.io.AdvancedAssetManager;
import de.mih.core.engine.render.Visual;


public class Tile {
	Room parent;
	
	public Visual visual;
	int x,y;
	
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
		boolean borderExists = borders.containsKey(direction);
		if(borderExists)
		{
			return borders.get(direction).getAdjacentTile(this) != null;
		}
		return false;
	}
	
	public Tile getNeighour(Direction direction)
	{
		return borders.get(direction).getAdjacentTile(this);
	}
	
	Vector3 center = new Vector3();
	
	public Tile(Vector3 center)
	{
		this.center = center;
		
		visual = new Visual(AdvancedAssetManager.getInstance().storedmodels.get("floor"));
	}
	
	public Tile(float x, float y, float z)
	{
		this(new Vector3(x, y, z));
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
	

	public Vector3 getCenter() {
		return center;
	}	
	
	public void render()
	{
		visual.model.transform.setToTranslation(center.x + visual.pos.x, center.y + visual.pos.y,
				center.z + visual.pos.z);
		//visual.model.transform.rotate(0f, 1f, 0f, visual.angle);
		visual.model.transform.scale(visual.getScale().x, visual.getScale().y, visual.getScale().z);
	}
	
	public String toString()
	{
		return "(" + center.x + ", " + center.z + ")";
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public Direction getDirection(TileBorder border)
	{
		if(this.borders.containsValue(border))
		{
			for(Entry<Direction, TileBorder> entry : this.borders.entrySet())
			{
				if(entry.getValue().equals(border))
				{
					return entry.getKey();
				}
			}
		}
		return null;
		
	}
	
	public boolean hasRoom()
	{
		return parent != null;
	}
	
	public void setRoom(Room room)
	{
		this.parent = room;
	}
	
	public Room getRoom()
	{
		return this.parent;
	}
}