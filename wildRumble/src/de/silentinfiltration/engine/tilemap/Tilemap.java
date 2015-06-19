package de.silentinfiltration.engine.tilemap;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

public class Tilemap {
	public Tile[][] map;
	public int width, length;
	public int tile_width, tile_height;
	private int halftwidth, halftheight;

	public Tilemap(int width, int length, int tilewidth, int tileheight) {
		this.map = new Tile[length][width];
		this.width = width;
		this.length = length;
		this.tile_width = tilewidth;
		this.tile_height = tileheight;
		halftwidth = tile_width / 2;
		halftheight = tile_height / 2;
	}

	public void setTileAt(int x, int y, Tile tile) {
		map[y][x] = tile;
		// tile.map = this;
	}

	public Tile getTileAt(int x, int y) {
		if(x < 0 || y < 0 || x >= width || y >= length)
			return null;
		return map[y][x];
	}

	// public Tile VectorToTile(Vector2f pointOnMap)
	// {
	// float x, y;
	//
	// y = (pointOnMap.x / tile_width + pointOnMap.y / tile_height)/2;
	// x = pointOnMap.x / tile_width - y;
	// System.out.println(x + ", " + y);
	// return map[(int)y][(int)x];
	// }
	public Vector2f mapToScreen(Vector2f pointOnMap) {
		Vector2f tmp = new Vector2f();
		tmp.x = (pointOnMap.x * halftwidth)
				+ (pointOnMap.y * halftwidth);
		tmp.y = (pointOnMap.y * halftheight)
				- (pointOnMap.x * halftheight);
		// System.out.println(tmp);
		return tmp;
	}

	public Vector2f screenToMap(Vector2f pointOnScreen) {
		Vector2f result = new Vector2f();
		
		result.x = pointOnScreen.x / halftwidth+ pointOnScreen.y / halftheight;
		result.y = pointOnScreen.y / halftheight - pointOnScreen.x / halftwidth;
		return result;
//		Vector2f result = new Vector2f();
//		result.x = pointOnScreen.x / tile_width	- (pointOnScreen.y - (tile_height / 2)) / tile_height;
//		result.y = (pointOnScreen.x + (tile_width / 2)) / tile_width + pointOnScreen.y / tile_height
//		return result;
	}

}
