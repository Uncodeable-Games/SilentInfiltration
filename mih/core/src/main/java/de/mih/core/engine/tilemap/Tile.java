package de.mih.core.engine.tilemap;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import de.mih.core.engine.ecs.RenderManager;
import de.mih.core.engine.render.Visual;
import de.mih.core.engine.tilemap.Tile.Direction;
import de.mih.core.engine.tilemap.borders.BorderCollider;
import de.mih.core.engine.tilemap.borders.TileBorder;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.systems.RenderSystem;

public class Tile {
	
	public Visual visual;
	
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
	
	Vector3 center;
	
	public Tile(Vector3 center)
	{
		this.center = center;
		
		visual = new Visual(RenderSystem.getInstance().storedmodels.get("floor"));
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
	
	public void addBorderCollider(BorderCollider collider, Direction direction)
	{
		if(borders.containsKey(direction))
		{
			borders.get(direction).setBorderCollider(collider);
		}
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
}