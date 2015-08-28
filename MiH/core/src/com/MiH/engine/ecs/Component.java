package com.MiH.engine.ecs;

/**
 * Component for the entity-component-system, 
 * has to be empty.
 * Components are only data holders and include NO functionality.
 * @author Tobias
 *
 */

public abstract class Component{
	/**
	 * the parent entity
	 */
	int entityID;
}