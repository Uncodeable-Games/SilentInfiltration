package de.mih.core.engine.ecs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

import de.mih.core.engine.render.BaseRenderer;

public class SystemManager extends BaseRenderer {
	
	static SystemManager systemM;
	
	List<BaseSystem> registeredSystems;
	PriorityQueue<BaseSystem> rS;
	/**
	 * linked entityManger for iteration over entities
	 */
	EntityManager entityManager;
	
	
	public static SystemManager getInstance(){
		if (systemM == null){
			return systemM = new SystemManager(EntityManager.getInstance(), 5);
		}
		return systemM;
	}

	public SystemManager(EntityManager entityManager, int initialCapacity) {
		super(true);
		this.entityManager = entityManager;
		this.registeredSystems = new ArrayList<BaseSystem>();
	}

	public void register(BaseSystem s) {
		if (!registeredSystems.contains(s)) {
			registeredSystems.add(s);
			Collections.sort(registeredSystems);
		}
	}

	public void update(double dt){
		for (BaseSystem s : registeredSystems) {
			for (int entity = 0; entity < entityManager.entityCount; entity++) {
				if (s.matchesSystem(entity)) {
					s.update(dt, entity);
				}
			}
			s.update(dt);
		}
		
	}

	public void render(){
		for (BaseSystem s : registeredSystems) {
			for (int entity = 0; entity < entityManager.entityCount; entity++) {
				if (s.matchesSystem(entity)) {
					s.render(entity);
				}
			}
			s.render();
		}
	}
}
