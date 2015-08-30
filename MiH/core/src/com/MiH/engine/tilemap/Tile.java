package com.MiH.engine.tilemap;

import java.util.Map;

import com.MiH.engine.ai.Node;
import com.badlogic.gdx.math.Vector2;

public class Tile extends Node{

	public Tilemap map;
	
	public String toString() { return x + ", " + y; }
	
	public void render()
	{
		if (model != null) model.transform.setToTranslation(x-(map.width/2f)+0.5f, 0, y-(map.length/2f));
	}
}
