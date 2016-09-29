package de.mih.core.engine.ecs.component;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Component implements Serializable
{
	public static ArrayList<? extends Component> allcomponentclasses = new ArrayList<>();

	/**
	 * the parent entity
	 */
	public int entityID;

	public Component()
	{
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}