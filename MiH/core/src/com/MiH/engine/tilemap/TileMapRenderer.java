package com.MiH.engine.tilemap;


import java.util.Map;

import com.MiH.engine.ai.Node;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class TileMapRenderer {
	public Rectangle viewport;
	public Vector2 cam;
	
	public Tilemap tilemap;
	
	public void render()  {
		if(tilemap == null)
			return;

		for(int i = tilemap.length -1; i >= 0; i--){
			for(int j = 0; j < tilemap.width; j++)
			{
			    tilemap.map[i][j].render();
			}
		}
	}
}
