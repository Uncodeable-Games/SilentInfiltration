package de.mih.core.engine.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.tilemap.Tile;
import de.mih.core.engine.tilemap.Tile.Direction;
import de.mih.core.game.Game;
import de.mih.core.game.components.ColliderC;
import de.mih.core.game.components.PositionC;

public class Pathfinder {
	//A-*

	
	List<Tile> closed, open;
	Tile start, goal;
	private Game game;

	public Pathfinder(Game game)
	{
		this.game = game;
	}

	public Map<Tile,Tile> findShortesPath(Tile start, Tile goal) {
		Map<Tile,Double> f = new HashMap<>();
		Map<Tile,Double> g = new HashMap<>();
		Map<Tile,Tile> prev = new HashMap<>();
		
		this.start = start;
		this.goal = goal;

		f.put(start, 0.0d);
		g.put(start, 0.0d);
		open = new ArrayList<Tile>();
		closed = new ArrayList<Tile>();
		
		open.add(start);
		
		Tile current;
		 while (!open.isEmpty()) 
		 {
			current = getMin(f);
			open.remove(current);

			if (current == goal) {
				Map<Tile,Tile> path = new HashMap<>();
				Tile tmp = goal;
				while(prev.containsKey(tmp)){
					path.put(prev.get(tmp),tmp);
					tmp = prev.get(tmp);
				}
				return path;
			}
			closed.add(current);
			expandNode(current,f,g,prev);

		}
		return new HashMap<>();
	}

	private Tile getMin(Map<Tile,Double> f) {
		Tile min = open.get(0);
		for (Tile n : open) {
			if (f.get(n) < f.get(min)) {
				min = n;
			}
		}
		return min;
	}

	
	private void expandNode(Tile current, Map<Tile,Double> f, Map<Tile, Double> g, Map<Tile,Tile> prev){
		List<Tile> neighbours = new ArrayList<>();
		for(Direction d : Direction.values())
		{
			boolean hasCollider = false;
		//	System.out.println("current: " + current);
			if(current.hasBorder(d) && current.getBorder(d).hasColliderEntity())
			{
				hasCollider = game.getEntityManager().hasComponent(current.getBorder(d).getColliderEntity(),ColliderC.class);
			}
			//TODO: add door check or something?
			if(current.hasNeighbour(d) && !hasCollider)
			{
				neighbours.add(current.getNeighour(d));
			}
			else
			{
				//System.out.println("collider found!");
			}
		}
		for (Tile neighbour : neighbours)
		{
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
	

	private double dist(Tile n1, Tile n2) {
		Vector3 t1 = n1.getCenter();
		Vector3 t2 = n2.getCenter();
		
		double distance = 0;
		double dx = Math.abs(t1.x - t2.x);
		double dy = Math.abs(t1.y - t1.y);
		distance = Math.sqrt(dx * dx + dy * dy);
		return distance;
	}
	
}