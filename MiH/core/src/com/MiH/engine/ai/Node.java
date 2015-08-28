package com.MiH.engine.ai;

import java.util.ArrayList;
import java.util.List;

import com.MiH.engine.tilemap.Tilemap;
import com.badlogic.gdx.graphics.g3d.ModelInstance;


public class Node {
	
	public boolean blocked;
	public int x,y;
	public ModelInstance model;
	
	public List<Node> neighbours = new ArrayList<Node>();
}
