package de.mih.core.engine.ai.navigation.pathfinder.PathGenerator;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;

import de.mih.core.engine.ai.navigation.NavPoint;

public interface Node {
	
	public HashMap<Node,Node> prev = new HashMap<Node,Node>();
	public HashMap<Node,Float> value = new HashMap<Node,Float>();

	public abstract float getDistance(Node target);

	public abstract Vector2 getPos();
	
	public abstract ArrayList<Node> getNeighbours(NavPoint last);
	
	public default void setValue(float v){
		value.put(this, v);
	}
	
	public default float getValue(){
		return value.get(this);
	}	
}
