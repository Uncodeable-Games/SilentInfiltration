package de.mih.core.engine.tilemap;

public class Tilemap {
	
	Tile[][] tilemap;
	int length, width;
	
	public Tilemap(int length, int width)
	{
		this.length = length;
		this.width = width;
		
		this.tilemap = new Tile[length][width];
	}
	
	public Tile getTileAt(int x, int y)
	{
		return tilemap[x][y];
	}
	
}
