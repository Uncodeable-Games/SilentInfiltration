package de.mih.core.engine.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.game.components.NodeC;
import de.mih.core.game.components.PositionC;

public class Pathfinder {
	//A-*
	EntityManager entityM;
	
	List<Integer> closed, open;
	int start, goal;

	public Pathfinder(EntityManager em) {
		entityM = em;
	}

	public Map<Integer,Integer> findShortesPath(int start, int goal) {
		Map<Integer,Double> f = new HashMap<>();
		Map<Integer,Double> g = new HashMap<>();
		Map<Integer,Integer> prev = new HashMap<>();
		
		this.start = start;
		this.goal = goal;

		f.put((Integer)start, 0.0d);
		g.put((Integer)start, 0.0d);
		open = new ArrayList<Integer>();
		closed = new ArrayList<Integer>();
		
		open.add((Integer)start);
		
		int current;
		do {
			current = getMin(f);
			open.remove((Integer) current);

			if (current == goal) {
				Map<Integer,Integer> path = new HashMap<>();
				int tmp = goal;
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

	private int getMin(Map<Integer,Double> f) {
		int min = open.get(0);
		for (Integer n : open) {
			if (f.get((Integer)n) < f.get((Integer)min)) {
				min = n;
			}
		}
		return min;
	}

	
	private void expandNode(int current, Map<Integer,Double> f, Map<Integer, Double> g, Map<Integer,Integer> prev){
		for (int neighbour : entityM.getComponent(current, NodeC.class).neighbours){
			if(entityM.getComponent(neighbour, NodeC.class).blocked)
				continue;
			if(closed.contains(neighbour)){
				continue;
			}
			//TODO: Kantenkosten?
			double newG = g.get(current) + 1;
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
	
	PositionC pos1;
	PositionC pos2;

	private double dist(int n1, int n2) {
		pos1 = entityM.getComponent(n1, PositionC.class);
		pos2 = entityM.getComponent(n2, PositionC.class);
		
		double distance = 0;
		double dx = pos1.position.x >= pos2.position.x ? pos1.position.x - pos2.position.x : pos2.position.x - pos1.position.x;
		double dz = pos1.position.z >= pos2.position.z ? pos1.position.z - pos2.position.z : pos2.position.z - pos1.position.z;
		distance = Math.sqrt(dx * dx + dz * dz);
		return distance;
	}
	
	public int getPath(){
		return goal;
	}
}