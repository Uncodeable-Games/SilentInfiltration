package com.MiH.engine.ecs;

import java.util.ArrayList;

/**
 * Component for the entity-component-system, 
 * has to be empty.
 * Components are only data holders and include NO functionality.
 * @author Tobias
 *
 */

public abstract class Component{
	
	public static ArrayList<Class> allcomponents = new ArrayList<Class>();
	
	/**
	 * the parent entity
	 */
	int entityID;
	
	public Component(){
		if (!allcomponents.contains(getClass())) allcomponents.add(getClass());
	}
	
	public void onRemove(){}
}