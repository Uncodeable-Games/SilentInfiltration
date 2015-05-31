package de.silentinfiltration.engine.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pathfinder {
	//A-*
	List<Node> closed, open;
	Node start, goal;

	public Pathfinder() {

		
	}

	
	public Map<Node,Node> findShortesPath(Node start, Node goal) {
		Map<Node,Double> f = new HashMap<>();
		Map<Node,Double> g = new HashMap<>();
		Map<Node,Node> prev = new HashMap<>();
		
		this.start = start;
		this.goal = goal;

		f.put(start, 0.0d);
		g.put(start, 0.0d);
		open = new ArrayList<Node>();
		closed = new ArrayList<Node>();
		
		open.add(start);
		
		Node current;
		do {
			current = getMin(f);
			open.remove(current);

			if (current == goal) {
				Map<Node,Node> path = new HashMap<>();
				Node tmp = goal;
				while(prev.containsKey(tmp)){
					path.put(tmp, prev.get(tmp));
					tmp = prev.get(tmp);
				}
				return path;
			}

			closed.add(current);
			expandNode(current,f,g,prev);

		} while (!open.isEmpty());
		return null;
	}

	private Node getMin(Map<Node,Double> f) {
		Node min = open.get(0);
		for (Node n : open) {
			if (f.get(n) < f.get(min)) {
				min = n;
			}
		}
		return min;
	}

	private void expandNode(Node current, Map<Node,Double> f, Map<Node, Double> g, Map<Node,Node> prev) {
		for(Node neighbour : current.neighbours){
			if(neighbour.blocked)
				continue;
			if(closed.contains(neighbour)){
				continue;
			}
			double newG = g.get(current) + 1; //Kantenkosten sind immer 1
			if(open.contains(neighbour) && newG >= g.get(neighbour)){
				continue;
			}
			prev.put(neighbour, current);
			//neighbour.previous = current;
			//neighbour.g = newG;
			g.put(neighbour,newG);
			
			double fn = newG + dist(neighbour,goal);
		
			f.put(neighbour,fn);
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
	
//	public void printPath(){
//		Node tmp = goal;
//		while(tmp.previous != null){
//		//	System.out.println("(" + tmp.x + ", " + tmp.y + ")");
//			tmp.isPath = true;
//			tmp = tmp.previous;
//		}
//		tmp.isPath = true;
//	}
	
	public Node getPath(){
		return goal;
	}
}
