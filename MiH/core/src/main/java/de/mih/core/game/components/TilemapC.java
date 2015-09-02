package de.mih.core.game.components;

import de.mih.core.engine.ecs.Component;

public class TilemapC extends Component {
	public int[][] map;
	public int width, length;
	public int tile_width, tile_height;
	
	public TilemapC(int l, int w){
		length = l;
		width = w;
		map = new int[length][width];
	}
	

	public void setTileAt(int x, int y, int e_tile)
	{
		map[x][y] = e_tile;
	}
	
	public int getTileAt(int x, int y)
	{
		return map[x][y];
	}
	
	public void setTileAt(float x, float z, int e_tile)
	{
		map[cordToIndex_x(x)][cordToIndex_z(z)] = e_tile;
	}
	
	public int getTileAt(float x, float z)
	{
		return map[cordToIndex_x(x)][cordToIndex_z(z)];
	}
	
	public int cordToIndex_x(float x){
		return Math.round(x/NodeC.TILE_SIZE + (length-1)/2f);
	}
	public int cordToIndex_z(float z){
		return Math.round(z/NodeC.TILE_SIZE + (width-1)/2f);
	}
	//
}
