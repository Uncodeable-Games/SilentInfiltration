package com.MiH.game.components;

import com.MiH.engine.ecs.Component;

public class TilemapC extends Component{
	public int[][] map;
	public int width, length;
	public int tile_width, tile_height;

	public TilemapC(int l, int w) {
		length = l;
		width = w;
		map = new int[length][width];
	}

	public void setTileAt(int x, int y, int tile_entity) {
		map[y][x] = tile_entity;
	}

	public int getTileAt(int x, int y)
	{
		return map[y][x];
	}
}
