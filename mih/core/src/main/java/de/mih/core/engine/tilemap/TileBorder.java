package de.mih.core.engine.tilemap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.math.Vector3;

import de.mih.core.engine.render.Visual;
import de.mih.core.engine.tilemap.Tile.Direction;
import de.mih.core.game.Game;
import de.mih.core.game.GameLogic;
import de.mih.core.game.components.VisualC;

import java.util.ArrayList;
import java.util.HashMap;

import static de.mih.core.engine.tilemap.Tile.Direction.E;

public class TileBorder
{
	public enum Facing
	{
		NS, WE
	}

	public float angle;

	public Facing facing;

	Vector3 center;

	private String[] textures = new String[2];

	HashMap<Direction, TileCorner> corners = new HashMap<>();
	public HashMap<Direction, Tile> adjacentTiles = new HashMap<>();

	public TileBorder(float x, float y)
	{
		this(new Vector3(x, 0, y));
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

	public ArrayList<Tile> getAdjacentTiles(){
		return new ArrayList<>(adjacentTiles.values());
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
		return corners.containsKey(E);
	}

	public boolean isVertical()
	{
		return !isHorizontal();
	}

	public Vector3 getPos()
	{
		return center;
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

	public void setTexture(int index, String texture){
		this.textures[index] = texture;
		if(GameLogic.getCurrentGame().noGraphic)
			return;
		Visual visual = GameLogic.getCurrentGame().getEntityManager().getComponent(getColliderEntity(), VisualC.class).getVisual();
		if(visual != null)
			visual.getModel().materials.get(index).set(TextureAttribute.createDiffuse(Game.getCurrentGame().getAssetManager().assetManager.get(textures[index], Texture.class)));
	}

	public String[] getTextures()
	{
		return textures;
	}

	public int getTextureIndexByAdjacentTile(Tile tile){
		Direction direction = tile.getDirection(this);
		if (direction == null) return -1;
		switch (direction){
			case E:{
				return 0;
			}
			case W:{
				return 1;
			}
			default:
				return -1;
		}
	}
}