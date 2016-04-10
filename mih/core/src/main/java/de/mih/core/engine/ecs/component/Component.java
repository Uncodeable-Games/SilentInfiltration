package de.mih.core.engine.ecs.component;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Component for the entity-component-system, has to be empty. Components are
 * only data holders and include NO functionality.
 *
 * @author Tobias
 */
@SuppressWarnings("rawtypes")
public abstract class Component implements Serializable
{

	public Component()
	{
	}

	public static ArrayList<Class> allcomponentclasses = new ArrayList<Class>();

	/**
	 * the parent entity
	 */
	public int entityID;

	public void Init()
	{

	}
}