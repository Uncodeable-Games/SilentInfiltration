package de.mih.core.engine.navigation;

public class Edge
{
	Vertex from, to;
	float costs;
	public float key;

	public Edge(Vertex from, Vertex to)
	{
		this.from = from;
		this.to = to;
		costs = from.position.dst(to.position);
	}

	public float getCost()
	{
		return costs;
	}

	public Vertex getFromNode()
	{
		return from;
	}

	public Vertex getToNode()
	{
		return to;
	}

}