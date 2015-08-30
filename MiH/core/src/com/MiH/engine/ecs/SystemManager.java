package com.MiH.engine.ecs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

import com.MiH.engine.exceptions.ComponentNotFoundEx;

public class SystemManager {
	List<BaseSystem> registeredSystems;
	PriorityQueue<BaseSystem> rS;
	/**
	 * linked entityManger for iteration over entities
	 */
	EntityManager entityManager;

	public SystemManager(EntityManager entityManager, int initialCapacity) {
		this.entityManager = entityManager;
		this.registeredSystems = new ArrayList<BaseSystem>();
	}

	public void register(BaseSystem s) {
		if (!registeredSystems.contains(s)) {
			registeredSystems.add(s);
			Collections.sort(registeredSystems);
		}
//		for (BaseSystem st : registeredSystems) {
//			System.out.println(st.toString());
//		}

	}

	public void update(double dt) throws ComponentNotFoundEx {
		for (BaseSystem s : registeredSystems) {
			s.update(dt);
			for (int entity = 0; entity < entityManager.entityCount; entity++) {

				if (s.matchesSystem(entity)) {
					s.update(dt, entity);
				}
			}
		}
	}

	public void render(double dt) throws ComponentNotFoundEx {
		for (BaseSystem s : registeredSystems) {
			s.render();
			for (int entity = 0; entity < entityManager.entityCount; entity++) {

				if (s.matchesSystem(entity)) {
					s.render(entity);
				}
			}
		}
	}
}
