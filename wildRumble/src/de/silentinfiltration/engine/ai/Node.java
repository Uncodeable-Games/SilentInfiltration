package de.silentinfiltration.engine.ai;

import java.util.ArrayList;
import java.util.List;


public class Node {
	public boolean blocked;
	public int x,y;
	public double f,g;
	public Node previous;
	
	public boolean isPath;
	
	public List<Node> neighbours = new ArrayList<Node>();
}
