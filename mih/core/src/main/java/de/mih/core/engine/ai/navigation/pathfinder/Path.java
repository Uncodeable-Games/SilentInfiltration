package de.mih.core.engine.ai.navigation.pathfinder;

import java.util.ArrayList;

import de.mih.core.engine.ai.navigation.NavPoint;

public class Path extends ArrayList<NavPoint> {
	
	static Path NOPATH = new Path();
	
	public static Path getNoPath(){
		return NOPATH;
	}

}
