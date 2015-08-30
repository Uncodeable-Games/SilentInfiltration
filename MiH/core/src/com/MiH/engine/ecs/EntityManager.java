package com.MiH.engine.ecs;

import java.util.HashMap;

import com.MiH.engine.exceptions.ComponentNotFoundEx;
import com.MiH.game.components.RecycleC;

public class EntityManager {
	// list of all entities, which are only represented as an integer, size is
	// max entities!
	// Integer[] entityMasks = new Integer[100000];
	public int entityCount = 0;

	HashMap<Class<? extends Component>, HashMap<Integer, Component>> componentStore = new HashMap<Class<? extends Component>, HashMap<Integer, Component>>();

	public int createEntity() {
		for (int i=0;i<entityCount;i++){
			if (hasComponent(i, RecycleC.class)){
				try {
					removeComponent(i, getComponent(i, RecycleC.class));
				} catch (ComponentNotFoundEx e) {e.printStackTrace();}
				return i;
			}
		}
		return entityCount++;
	}

	public <T extends Component> void addComponent(int entity, T... comps) {
		for (T c : comps) {
			Class<? extends Component> componentType = c.getClass();
			HashMap<Integer, Component> sub;
			if (!componentStore.containsKey(componentType)) {
				componentStore.put(componentType, new HashMap<Integer, Component>());
			}
			sub = componentStore.get(componentType);
			sub.put(entity, c);
		}
	}

	public boolean hasComponent(int entity, Class<? extends Component> componentType) {
		if (componentStore.containsKey(componentType)) {
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
	public <T> T getComponent(int entity, Class<T> componentType) throws ComponentNotFoundEx {

		@SuppressWarnings("unchecked")
		// HashMap<Integer, Component> tmp = componentStore.get(componentType);
		T result = (T) componentStore.get(componentType).get(entity);
		if (result == null) {
			throw new ComponentNotFoundEx(entity, componentType);
		}

		return result;
	}

	public void removeComponent(int entity, Component c) {
		if (hasComponent(entity, c.getClass())) {
			c.onRemove();
			componentStore.get(c.getClass()).remove(entity);
		}
	}

	public void removeEntity(int entity) {
		for (Class c : Component.allcomponents) {
			if (componentStore.containsKey(c)) {
				if (componentStore.get(c).containsKey(entity)) {
					removeComponent(entity, componentStore.get(c).get(entity));
				}
			}
		}
		addComponent(entity, new RecycleC());
	}

}
