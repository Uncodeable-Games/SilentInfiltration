package de.silentinfiltration.engine.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Pathfinder {
	//A-*
	List<Node> closed, open;
	Node start, goal;

	public Pathfinder(Node start, Node goal) {

		this.start = start;
		this.goal = goal;

		start.f = 0;

		open = new ArrayList<Node>();
		closed = new ArrayList<Node>();
	}

	public boolean findShortesPath() {
		open.add(start);
		Node current;
		do {
			current = getMin();
			open.remove(current);

			if (current == goal) {
				return true;
			}

			closed.add(current);
			expandNode(current);

		} while (!open.isEmpty());
		return false;
	}

	public Node getMin() {
		Node min = open.get(0);
		for (Node n : open) {
			if (n.f < min.f) {
				min = n;
			}
		}
		return min;
	}

	public void expandNode(Node current) {
		for(Node neighbour : current.neighbours){
			if(neighbour.blocked)
				continue;
			if(closed.contains(neighbour)){
				continue;
			}
			double newG = current.g + 1; //Kantenkosten sind immer 1
			if(open.contains(neighbour) && newG >= neighbour.g){
				continue;
			}
			neighbour.previous = current;
			neighbour.g = newG;
			double f = newG + dist(neighbour,goal);
			neighbour.f = f;
			if(!open.contains(neighbour)){
				open.add(neighbour);
			}
				
		}
	}

	private double dist(Node n1, Node n2) {
		double distance = 0;
		double dx = n1.x >= n2.x ? n1.x - n2.x : n2.x - n1.x;
		double dy = n1.y >= n2.y ? n1.y - n2.y : n2.y - n1.y;
		distance = Math.sqrt(dx * dx + dy * dy);
		return distance;
	}
	
	public void printPath(){
		Node tmp = goal;
		while(tmp.previous != null){
		//	System.out.println("(" + tmp.x + ", " + tmp.y + ")");
			tmp.isPath = true;
			tmp = tmp.previous;
		}
		tmp.isPath = true;
	}
}
