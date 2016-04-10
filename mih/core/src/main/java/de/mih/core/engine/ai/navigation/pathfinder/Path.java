package de.mih.core.engine.ai.navigation.pathfinder;

import de.mih.core.engine.ai.navigation.NavPoint;

import java.util.ArrayList;

public class Path extends ArrayList<NavPoint>
{
	
	static Path NOPATH = new Path();
	
	public static Path getNoPath()
	{
		return NOPATH;
	}
}
