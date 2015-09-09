package de.mih.core.engine.tilemap;

import com.badlogic.gdx.math.Vector2;

import de.mih.core.engine.tilemap.Tile.Direction;
import de.mih.core.engine.tilemap.borders.TileBorder;

public class Tilemap {
	
	float TILESIZE = 2f;
	
	//Vector2[][] tileCorners;
	Tile[][] tilemap;
	//TileBorder[][] borders;
	int length, width;
	
	public Tilemap(int length, int width, float tilesize)
	{
		this.length = length;
		this.width = width;
		this.TILESIZE = tilesize;
		
		this.tilemap = new Tile[width][length];
		this.createTilemap();
	}
	
	public Tile getTileAt(int x, int y)
	{
		return tilemap[x][y];
	}
	
	public int coordToIndex_x(float x)
	{
		return Math.round(x/TILESIZE + (length-1)/2f);
	}
	
	public int coordToIndex_z(float z)
	{
		return Math.round(z/TILESIZE + (width-1)/2f);
	}
	
	
	private void createTilemap()
	{
		for(int x = 0; x < width; x++)
		{
			for(int y = 0; y < length; y++)
			{
				Tile tmp = new Tile(TILESIZE/2f * (float)x, TILESIZE/2f * (float)y);
				for(Direction direction : new Direction[]{ Direction.E, Direction.N})
				{
					Tile neighbour = null;
					Vector2 borderCenterOffset = Vector2.Zero;
					if (direction == Direction.E)
					{
						borderCenterOffset.x -= TILESIZE/2f;
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
						borderCenterOffset.y -= TILESIZE/2f;
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
						Vector2 borderCenter = tmp.center.cpy();
						borderCenter.sub(borderCenterOffset);
						TileBorder border = new TileBorder(borderCenter);
						if(neighbour != null)
						{
							neighbour.setBorder(direction.getOppositeDirection(),border);
						}
						tmp.setBorder(direction, border);
//					}
				}
				tilemap[x][y] = tmp;
			}
		}
	}
	
}
