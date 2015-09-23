package de.mih.core.engine.navigation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

public class PolygonGraph
{
	public PolygonGraph()
	{

	}
	
	public boolean intersectsLine(Vector2 p1, Vertex vert)
	{
		for(Vertex v : vertices)
		{
//			if(Intersector.pointLineSide(p1, vert.position, v.position) == 0 && v != vert)
//			{
//				return true;
//			}
		}
		int intersections = 0;
		for(Edge e : edges)
		{
//			if(e.from == vert || e.to == vert || p1.equals(e.from.position) || p1.equals(e.to.position))
//				continue;
			Vector2 intersection = new Vector2();
			if(Intersector.intersectLines(e.from.position, e.to.position, p1, vert.position, intersection) && Intersector.pointLineSide(p1, vert.position, intersection) != 0)
			{
				System.out.println(p1 + " " + vert.position + ": intersection at: " +intersection);
				return true;
			}
		}
//		if(intersections > 3)
//		{
//			return true;
//		}
//		if(Intersector.intersectLinePolygon(p1, vert, polygon))
//		{
//			
//			return true;
//		}
		return false;
	}

	public Polygon polygon;
	HashMap<Vertex, List<Edge>> edgesToVert = new HashMap<>();
	List<Edge> edges = new ArrayList<>();
	List<Vertex> vertices = new ArrayList<>();

	public void addEdge(Vertex from, Vertex to)
	{
		if (!vertices.contains(from))
			vertices.add(from);
		if (!vertices.contains(to))
			vertices.add(to);
		Edge edge = new Edge(from, to);
		// edges.get(from).add(edge);
		// edges.get(to).add(edge);
		if (!edgesToVert.containsKey(from))
			edgesToVert.put(from, new ArrayList<>());
		if (!edgesToVert.containsKey(to))
			edgesToVert.put(to, new ArrayList<>());
		edgesToVert.get(from).add(edge);
		edgesToVert.get(to).add(edge);

		edges.add(edge);

	}

	public List<Edge> getIncidentEdges(Vertex fromNode)
	{
		return edgesToVert.get(fromNode);
	}

}