package de.mih.core.engine.tilemap;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import de.mih.core.engine.tilemap.Tile.Direction;
import de.mih.core.engine.tilemap.borders.TileBorder;

public class Tilemap {
	
	float TILESIZE;
	

	//Vector2[][] tileCorners;
	Tile[][] tilemap;
	//TileBorder[][] borders;
	private List<TileBorder> borders = new ArrayList<>();
	private int length;

	private int width;
	
	public Tilemap(int length, int width, float tilesize)
	{
		this.setLength(length);
		this.setWidth(width);
		this.TILESIZE = tilesize;
		
		this.tilemap = new Tile[width][length];
		this.createTilemap();
	}
	
	public Tile getTileAt(int x, int y)
	{
		if(x >= 0 && x < width && y >= 0 && y < length)
		{
			return tilemap[x][y];
		}
		return null;
	}
	
	public int coordToIndex_x(float x)
	{
		return Math.round(x/TILESIZE );
	}
	
	public int coordToIndex_z(float z)
	{
		return Math.round(z/TILESIZE );
	}
	
	
	private void createTilemap()
	{
		for(int x = 0; x < getWidth(); x++)
		{
			for(int y = 0; y < getLength(); y++)
			{
				Tile tmp = new Tile(TILESIZE * (float)x + TILESIZE/2f, 0, TILESIZE * (float)y + TILESIZE/2f)  ;
				for(Direction direction : new Direction[]{ Direction.E, Direction.N})
				{
					Tile neighbour = null;
					Vector3 borderCenterOffset = new Vector3();
					float angle = 0f;
					if (direction == Direction.E)
					{
						borderCenterOffset.x += TILESIZE/2f;
						angle = 90f;
						if(x > 0)
							neighbour = tilemap[x-1][y];
					}
					/*else if (direction == Direction.W)
					{
						borderCenterOffset.x += TILESIZE/2f;
						if(x < width)
							neighbour = tilemap[x+1][y];
					}*/
					else if (direction == Direction.N)
					{
						borderCenterOffset.z += TILESIZE/2f;
						if(y > 0)
							neighbour = tilemap[x][y-1];
					}
					/*else if (direction == Direction.S)
					{
						borderCenterOffset.y += TILESIZE/2f;
						if(y < length)
							neighbour = tilemap[x][y+1];
					}*/
					
//					if(!tmp.hasBorder(direction))
//					{
						Vector3 borderCenter = tmp.center.cpy();
						borderCenter.sub(borderCenterOffset);
						
						TileBorder border = new TileBorder(borderCenter);
						border.angle = angle;
						border.setAdjacent(tmp);
						if(neighbour != null)
						{
							border.setAdjacent(neighbour);
							neighbour.setBorder(direction.getOppositeDirection(),border);
						}
						this.borders.add(border);
						tmp.setBorder(direction, border);
//					}
				}
				tilemap[x][y] = tmp;
			}
		}
	}
	
	public float getTILESIZE() {
		return TILESIZE;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public List<TileBorder> getBorders() {
		return borders;
	}

//	public void setBorders(List<TileBorder> borders) {
//		this.borders = borders;
//	}
	
}
