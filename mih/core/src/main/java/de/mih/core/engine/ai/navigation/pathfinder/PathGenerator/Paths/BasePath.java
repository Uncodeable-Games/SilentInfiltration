package de.mih.core.engine.ai.navigation.pathfinder.PathGenerator.Paths;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public abstract class BasePath<T> extends ArrayList<T> {
	
	private static final long serialVersionUID = 1876613416828738393L;
	
	public float distance = Float.MAX_VALUE;

	public abstract ArrayList<T> getNeighbours(T object);
	
	public abstract float getDistance(T start, T end);
	
	public abstract Vector2 getPos(T object);
	
	public abstract BasePath<T> getNoPath();
}
