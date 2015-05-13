package de.silentinfiltration.engine.ecs;

import java.util.HashMap;

import de.silentinfiltration.engine.exceptions.ComponentNotFoundEx;


public class EntityManager {
	//list of all entities, which are only represented as an integer, size is max entities!
	//Integer[] entityMasks = new Integer[100000];
	public int entityCount = 0;
	
	HashMap<Class<? extends Component>, HashMap<Integer, Component>> componentStore = new HashMap<Class<? extends Component>, HashMap<Integer,Component>>();
	
	
	public int createEntity(){
		return entityCount++;
	}
	
	
	//TODO: test the following code :D
//	public <T extends Component> void addComponent(int entity, T c){
//		Class<? extends Component> componentType = c.getClass();
//		HashMap<Integer, Component> sub;
//		if(!componentStore.containsKey(componentType)){
//			componentStore.put(componentType, new HashMap<Integer, Component>());
//		}
//		sub = componentStore.get(componentType);
//		sub.put(entity,c);
//	}
	
	public <T extends Component> void addComponent(int entity, T... comps){
		for(T c : comps){
			Class<? extends Component> componentType = c.getClass();
			HashMap<Integer, Component> sub;
			if(!componentStore.containsKey(componentType)){
				componentStore.put(componentType, new HashMap<Integer, Component>());
			}
			sub = componentStore.get(componentType);
			sub.put(entity,c);
		}
	}
	
	public boolean hasComponent(int entity, Class<? extends Component> componentType){
		if(componentStore.containsKey(componentType)){
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
	public <T> T getComponent(int entity, Class<T> componentType) throws ComponentNotFoundEx{
		
		   @SuppressWarnings("unchecked")
		 //  HashMap<Integer, Component> tmp = componentStore.get(componentType);
		   T result = (T) componentStore.get( componentType).get( entity );
		   if(result == null){
			   throw new ComponentNotFoundEx(entity, componentType);
		   }
			
		   return result;
      }
	
	
	public void removeComponent(int entity, Component c){
		if(hasComponent(entity,c.getClass())){
			componentStore.get(c.getClass()).remove(entity);
		}
	}
	
}


