package de.silentinfiltration.engine.ai;

import java.util.ArrayList;
import java.util.List;

import de.silentinfiltration.engine.tilemap.Tile;


public class Node {
	public boolean blocked;
	public int x,y;
//	public double f,g;
//	public Node previous;
//	
//	public boolean isPath;
	
	public List<Node> neighbours = new ArrayList<Node>();
	
	public String toString()
	{
		return "(" + x + ", " + y + ")";
	}
	
	@Override
	public boolean equals(Object other){
		if(other instanceof Node)
		{
			Node n = (Node) other;
			return this.x == n.x && this.y == n.y;
		}
		return false;
	}
}
