package de.mih.core.engine.ecs;

import java.util.ArrayList;

/**
 * Component for the entity-component-system, 
 * has to be empty.
 * Components are only data holders and include NO functionality.
 * @author Tobias
 *
 */
@SuppressWarnings("rawtypes")
public abstract class Component{
	
	public static ArrayList<Class> allcomponentclasses = new ArrayList<Class>();
	
	/**
	 * the parent entity
	 */
	int entityID;
	
	public Component(){
		if (!allcomponentclasses.contains(getClass())) allcomponentclasses.add(getClass());
	}
	
	public void onRemove(){}
}