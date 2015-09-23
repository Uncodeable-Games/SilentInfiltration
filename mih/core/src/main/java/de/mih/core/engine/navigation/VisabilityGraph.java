package de.mih.core.engine.navigation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

public class VisabilityGraph
{
	public List<Vertex> verts = new ArrayList<>();
	public List<Edge> edges = new ArrayList<>();
	HashMap<Vertex, List<Edge>> connections = new HashMap<>();

	public void addConnection(Vertex v, Vertex w)
	{
		Edge e = new Edge(v, w);
		if(!connections.containsKey(v))
		{
			connections.put(v, new ArrayList<Edge>());
		}
		List<Edge> a = connections.get(v);
		a.add(e);
		if(!connections.containsKey(w))
		{
			connections.put(w, new ArrayList<Edge>());
		}
		List<Edge> b = connections.get(w);
		b.add(e);
		if(!verts.contains(v))
			verts.add(v);
		if(!verts.contains(w))
			verts.add(w);
		edges.add(e);
	}
	
//	public List<Edge> getConnections(Vertex fromNode)
//	{
//		return connections.get(fromNode);
//	}
}