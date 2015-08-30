package com.MiH.engine.tilemap;

import com.MiH.game.systems.RenderSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;

public class Tilemap {
	public Tile[][] map;
	public int width, length;
	public int tile_width, tile_height;
	public ModelInstance floor;
	
	
	public RenderSystem rs;
	
	public Tilemap(int width, int length, RenderSystem rs)
	{
		this.map = new Tile[length][width];
		this.width = width;
		this.length = length;
		this.rs = rs;
		floor = new ModelInstance(rs.modelBuilder.createBox(width, .01f, length+1, new Material(ColorAttribute.createDiffuse(Color.GREEN)),
                VertexAttributes.Usage.Position|VertexAttributes.Usage.Normal));
		floor.transform.translate(0f, -.5f, 0f);
		rs.allmodels.add(floor);
	}
	
	public void setTileAt(int x, int y, Tile tile)
	{
		map[y][x] = tile;
	}
	
	public Tile getTileAt(int x, int y)
	{
		return map[y][x];
	}
}
