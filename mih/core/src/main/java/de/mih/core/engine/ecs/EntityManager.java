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

	private HashMap<Class<? extends Component>, HashMap<Integer, Component>> componentStore = new HashMap<>();

	public int createEntity()
	{
		// TODO create pool
		return entityCount++;
	}
	
	/**
	 * Needed for remote entity setting, maybe we need a better solution
	 * @param entity
	 */
	public void setMaxEntity(int entity)
	{
		if(entity > entityCount)
		{
			entityCount = entity;
		}
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
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends Component> T getComponent(int entity, Class<T> componentType)
	{
		return (T) componentStore.get(componentType).get(entity);
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
			componentStore.get(c.getClass()).remove(entity);
		}
	}

	// TODO: replace with pool for components
	public void removeEntity(int entity)
	{
		for (HashMap<Integer, Component> list : componentStore.values())
		{
			if (list.containsKey(entity))
			{
				removeComponent(entity, list.get(entity));
			}
		}
	}
}
