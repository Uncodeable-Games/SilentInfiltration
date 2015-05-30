package de.silentinfiltration.game.tilemap;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

public class Tilemap {
	public Tile[][] map;
	public int width, length;
	public int tile_width, tile_height;
	
	public Tilemap(int width, int length)
	{
		this.map = new Tile[length][width];
		this.width = width;
		this.length = length;
	}
	
	public void setTileAt(int x, int y, Tile tile)
	{
		map[y][x] = tile;
	}
	
	public Tile getTileAt(int x, int y)
	{
		return map[y][x];
	}
	
//	public Tile VectorToTile(Vector2f pointOnMap)
//	{
//		float x, y;
//		
//		y = (pointOnMap.x / tile_width + pointOnMap.y / tile_height)/2;
//		x = pointOnMap.x / tile_width - y;
//		System.out.println(x + ", " + y);
//		return map[(int)y][(int)x];
//	}
	public Vector2f mapToScreen(Vector2f pointOnMap)
	{
		Vector2f tmp = new Vector2f();
		tmp.x = (pointOnMap.x * tile_width   / 2) + (pointOnMap.y * tile_width  / 2);
	    tmp.y = (pointOnMap.y * tile_height / 2) - (pointOnMap.x * tile_height / 2);
	   // System.out.println(tmp);
		return tmp;
	}
	
	public Vector2f screenToMap(Vector2f pointOnScreen)
	{
		return new Vector2f(pointOnScreen.x/tile_width - (pointOnScreen.y-(tile_height/2))/tile_height, (pointOnScreen.x+(tile_width/2))/tile_width + pointOnScreen.y/tile_height);
	}
	
}
