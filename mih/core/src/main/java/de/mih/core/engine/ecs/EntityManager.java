package de.mih.core.engine.ecs;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import de.mih.core.engine.ecs.component.Component;

@SuppressWarnings("rawtypes")
public class EntityManager
{
	// list of all entities, which are only represented as an integer, size is
	// max entities!
	public int entityCount = 0;

	HashMap<Class<? extends Component>, HashMap<Integer, Component>> componentStore = new HashMap<Class<? extends Component>, HashMap<Integer, Component>>();

	public int createEntity()
	{
		// TODO create pool
		return entityCount++;
	}

	@SuppressWarnings("unchecked")
	public void addComponent(int entity, Component... comps)
	{
		for (Component c : comps)
		{
			Class<? extends Component> componentType = c.getClass();
			HashMap<Integer, Component> sub;
			if (!componentStore.containsKey(componentType))
			{
				componentStore.put(componentType, new HashMap<Integer, Component>());
			}
			sub = componentStore.get(componentType);
			c.entityID = entity;
			sub.put(entity, c);
		}
	}

	public boolean hasComponent(int entity, Class<? extends Component> componentType)
	{
		if (componentStore.containsKey(componentType))
		{
			return componentStore.get(componentType).containsKey(entity);
		}
		return false;
	}

	/**
	 * 
	 * @param entity
	 * @param componentType
	 * @return
	 */
	public <T> T getComponent(int entity, Class<T> componentType)
	{
		@SuppressWarnings("unchecked")
		T result = (T) componentStore.get(componentType).get(entity);
		return result;
	}

	public Set<Integer> getEntitiesForType(Class<?> componentType)
	{
		return componentStore.get(componentType).keySet();
	}

	public void removeComponent(int entity, Component c)
	{
		if (hasComponent(entity, c.getClass()))
		{
			// c.onRemove();
			componentStore.get(c.getClass()).remove(entity);
		}
	}

	// TODO: replace with pool for components
	public void removeEntity(int entity)
	{
		for (Class c : Component.allcomponentclasses)
		{
			if (componentStore.containsKey(c))
			{
				if (componentStore.get(c).containsKey(entity))
				{
					removeComponent(entity, componentStore.get(c).get(entity));
				}
			}
		}
	}
}
