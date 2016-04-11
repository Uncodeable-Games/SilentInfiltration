package de.mih.core.engine.ecs.component;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Component implements Serializable
{
	public static ArrayList<Class> allcomponentclasses = new ArrayList<Class>();

	/**
	 * the parent entity
	 */
	public int entityID;

	public Component(){}

	public <T extends Component> Component(T comp){
		try
		{
			this.getClass().getConstructor(this.getClass());
		} catch (NoSuchMethodException e)
		{
			e.printStackTrace();
		}
	}
}