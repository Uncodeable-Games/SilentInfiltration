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
}