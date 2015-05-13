package de.silentinfiltration.engine.ecs;

import java.util.ArrayList;
import java.util.List;

import de.silentinfiltration.engine.exceptions.ComponentNotFoundEx;


public class SystemManager {
	List<BaseSystem> registeredSystems;
	/**
	 * linked entityManger for iteration over entities
	 */
	EntityManager entityManager;
	
	public SystemManager(EntityManager entityManager){
		this.entityManager = entityManager;
		this.registeredSystems = new ArrayList<BaseSystem>();
	}
	
	public void register(BaseSystem s){
		if(!registeredSystems.contains(s))
			registeredSystems.add(s);
	}

	public void update(long dt) throws ComponentNotFoundEx{
		for(int entity = 0; entity < entityManager.entityCount; entity++){
			for(BaseSystem s : registeredSystems){				
				if(s.matchesSystem(entity)){
					s.update(dt,entity);
				}
			}
		}
	}
	public void render(long dt) throws ComponentNotFoundEx{
		for(int entity = 0; entity < entityManager.entityCount; entity++){
			for(BaseSystem s : registeredSystems){				
				if(s.matchesSystem(entity)){
					s.render(entity);
				}
			}
		}
	}
}
