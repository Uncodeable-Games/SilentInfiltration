package de.silentinfiltration.game.tilemap;

import org.lwjgl.util.vector.Vector2f;

public class Tilemap {
	public Tile[][] map;
	public int width, length;
	
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
	
	public Vector2f mapToScreen(Vector2f pointOnMap)
	{
		return new Vector2f();
	}
	
	public Vector2f screenToMap(Vector2f pointOnScreen)
	{
		return new Vector2f();
	}
	
}
