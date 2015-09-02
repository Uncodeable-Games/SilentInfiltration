package de.mih.core.game.components;

import java.util.ArrayList;
import java.util.List;

import de.mih.core.engine.ecs.Component;

public class NodeC extends Component{
	public boolean blocked;
	public List<Integer> neighbours = new ArrayList<Integer>();
	public TilemapC map;
}
