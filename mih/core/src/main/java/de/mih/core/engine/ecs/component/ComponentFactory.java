package de.mih.core.engine.ecs.component;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by Cataract on 11.04.2016.
 */
public class ComponentFactory
{
	public <T extends Component> T generateComponent(T comp){
		try
		{
			return comp.getClass().getConstructor(comp.getClass()).newInstance(comp);
		} catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
