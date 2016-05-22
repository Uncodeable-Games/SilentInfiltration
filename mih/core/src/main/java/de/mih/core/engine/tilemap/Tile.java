package de.mih.core.engine.tilemap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.math.Vector3;

import de.mih.core.engine.render.Visual;
import de.mih.core.game.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Tile
{
	private Room parent = null;
	Vector3 center;

	// TODO: can probably be removed
	private Tilemap tilemap;
	private Visual visual;
	String modelType;
	private int x, y;
	private String texture;


	public enum Direction
	{
		N, S, W, E;

		private Direction opposite;

		static
		{
			N.opposite = S;
			S.opposite = N;
			E.opposite = W;
			W.opposite = E;
		}

		public Direction getOppositeDirection()
		{
			return opposite;
		}

		public static Direction parseDirection(String parse) throws IllegalArgumentException
		{
			String tmp = parse.toUpperCase();
			if (tmp.equals("N"))
				return N;
			if (tmp.equals("S"))
				return S;
			if (tmp.equals("E"))
				return E;
			if (tmp.equals("W"))
				return W;
			throw new IllegalArgumentException("Cannot parse \"" + parse + " to a direction!");
		}
	}

	Map<Direction, TileBorder> borders = new HashMap<>();
	Map<Direction, TileCorner> corners = new HashMap<>();

	public Tile(Vector3 center, Tilemap tilemap)
	{
		this.center = center;

		this.tilemap = tilemap;
		this.modelType = "floor.g3db";
		//visual = new Visual("floor.g3db");
		//visual.setScale(0.99f,0.99f,0.99f);
	}


	public boolean hasNeighbour(Direction direction)
	{
		if (borders.containsKey(direction))
		{
			return borders.get(direction).getAdjacentTile(this) != null;
		}
		return false;
	}

	public Tile getNeighour(Direction direction)
	{
		return borders.get(direction).getAdjacentTile(this);
	}

	//Just save the neighbours after the first lookup?
	public ArrayList<Tile> getAllNeighbours()
	{
		ArrayList<Tile> neighbours = new ArrayList<Tile>();
		if (hasNeighbour(Direction.N))
		{
			neighbours.add(getNeighour(Direction.N));
		}
		if (hasNeighbour(Direction.W))
		{
			neighbours.add(getNeighour(Direction.W));
		}
		if (hasNeighbour(Direction.S))
		{
			neighbours.add(getNeighour(Direction.S));
		}
		if (hasNeighbour(Direction.E))
		{
			neighbours.add(getNeighour(Direction.E));
		}
		return neighbours;
	}

	public Tile(float x, float y, Tilemap tilemap)
	{
		this(new Vector3(x, 0, y), tilemap);
	}

	public void setBorder(Direction direction, TileBorder border)
	{
		border.setAdjacent(this, direction.getOppositeDirection());
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

	public Direction getBorderDirection(TileBorder border)
	{
		for (Direction dir : borders.keySet())
		{
			if (borders.get(dir) == border)
				return dir;
		}
		return null;
	}
	
	public boolean hasCorner(Direction dir)
	{
		return (corners.containsKey(dir));
	}

	public void setCorner(Direction dir, TileCorner corner)
	{
		corners.put(dir, corner);
	}

	public TileCorner getCorner(Direction dir)
	{
		return corners.get(dir);
	}

	public Direction getCornerDirection(TileCorner corner)
	{
		for (Direction dir : corners.keySet())
		{
			if (corners.get(dir) == corner)
				return dir;
		}
		return null;
	}

	public Vector3 getCenter()
	{
		return center;
	}

	public void render()
	{
		visual.getModel().transform.setToTranslation(center.x + visual.getPos().x, visual.getPos().y,
				center.z + visual.getPos().z);
		// visual.model.transform.rotate(0f, 1f, 0f, visual.angle);
		visual.getModel().transform.scale(visual.getScale().x, visual.getScale().y, visual.getScale().z);
	}

	public String toString()
	{
		return "(" + center.x + ", " + center.z + ")";
	}

	public int getX()
	{
		return x;
	}

	public void setX(int x)
	{
		this.x = x;
	}

	public int getY()
	{
		return y;
	}

	public void setY(int y)
	{
		this.y = y;
	}

	public Direction getDirection(TileBorder border)
	{
		if (this.borders.containsValue(border))
		{
			for (Entry<Direction, TileBorder> entry : this.borders.entrySet())
			{
				if (entry.getValue().equals(border))
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
		if (room != null)
			room.addTile(this);
		this.parent = room;
	}
	
	public String getModelType()
	{
		return modelType;
	}

	public void setVisual(Visual visual)
	{
		this.visual = visual;
	}

	public Visual getVisual()
	{
		return visual;
	}

	public String getTexture()
	{
		return texture;
	}

	public void setTexture(String texture)
	{
		this.texture = texture;
		//visual.getModel().materials.get(0).set(TextureAttribute.createDiffuse(Game.getCurrentGame().getAssetManager().assetManager.get(texture,Texture.class)));
	}

	public Room getRoom()
	{
		return this.parent;
	}
	
	public Tilemap getTilemap()
	{
		return tilemap;
	}
}