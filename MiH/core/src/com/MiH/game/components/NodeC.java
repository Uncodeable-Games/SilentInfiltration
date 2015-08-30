package com.MiH.game.components;

import java.util.ArrayList;

import com.MiH.engine.ecs.Component;

public class NodeC extends Component{
	public boolean blocked = false;
	public TilemapC map;

	public ArrayList<NodeC> neighbours = new ArrayList<NodeC>();
}
