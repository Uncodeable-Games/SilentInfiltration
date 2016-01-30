package de.mih.core.engine.ai.navigation.pathfinder.PathGenerator.Paths;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

import de.mih.core.engine.ai.navigation.NavPoint;

public class Path extends BasePath<NavPoint>
{
	
	public static Path NOPATH = new Path();

	private static final long serialVersionUID = 2998231369299443032L;

	@Override
	public ArrayList<NavPoint> getNeighbours(NavPoint object) {
		return object.getVisibleNavPoints();
	}

	@Override
	public float getDistance(NavPoint start, NavPoint end) {
		return start.getDistance(end);
	}

	@Override
	public Vector2 getPos(NavPoint object) {
		return object.getPos();
	}

	@Override
	public BasePath<NavPoint> getNoPath() {
		return NOPATH;
	}

}
