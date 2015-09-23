package de.mih.core.engine.navigation;

import com.badlogic.gdx.math.Vector2;

public class Vertex
{
	public Vector2 position;
	public PolygonGraph polygon;
	public Vertex prior;
	
	public String toString()
	{
		return position.toString();
	}
}