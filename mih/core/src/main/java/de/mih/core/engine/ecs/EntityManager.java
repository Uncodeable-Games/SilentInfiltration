package de.mih.core.engine.ecs;

import de.mih.core.engine.ecs.component.Component;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

	public void addComponent(int entity, Component... comps)
	{
		for (Component c : comps)
		{
			Class<? extends Component>  componentType = c.getClass();
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
	
	public List<Integer> getEntitiesOfType(Predicate<Integer> predicate, Class<?>... componentTypes)
	{
		return getEntitiesOfType(componentTypes).stream().filter(predicate).collect(Collectors.toList());
	}

	public List<Integer> getEntitiesOfType(Class<?>... componentTypes)
	{
		List<Integer> entities = new ArrayList<>();
		for (Class<?> componentType : componentTypes)
		{
			if (entities.isEmpty())
				entities.addAll(componentStore.get(componentType).keySet());
			else
				entities.retainAll(componentStore.get(componentType).keySet());
		}
		return entities;
	}

	public Set<Integer> getEntitiesOfType(Class<?> componentType)
	{
		if (!componentStore.containsKey(componentType))
			return Collections.emptySet();
		return componentStore.get(componentType).keySet();
	}
	
	public Set<Integer> getEntitiesOfType(Class<?> componentType, Predicate<Integer> predicate)
	{
		Set<Integer> entities = getEntitiesOfType(componentType);
		return entities.stream().filter(predicate).collect(Collectors.toSet());
	}

	public void removeComponent(int entity, Component c)
	{
		if (hasComponent(entity, c.getClass()))
		{
			// c.onRemove();
			//System.out.println("removing: " + c.getClass().getName());
			componentStore.get(c.getClass()).remove(entity);
		}
	}

	// TODO: replace with pool for components
	public void removeEntity(int entity)
	{
		//System.out.println("removing entity: " + entity);
		for (HashMap<Integer, Component> list : componentStore.values())
		{

			if (list.containsKey(entity))
			{
				removeComponent(entity, list.get(entity));
			}
		}
	}
}
