package de.mih.core.engine.ecs;

import java.util.ArrayList;
import java.util.HashMap;

import de.mih.core.engine.ecs.events.BaseEvent;

public class EventManager {
	
	static EventManager eventM;
	
	HashMap<Class<? extends BaseEvent>,ArrayList<BaseSystem>> registeredSystems = new HashMap<Class<? extends BaseEvent>,ArrayList<BaseSystem>>();
	
	public static EventManager getInstance(){
		if (eventM == null){
			return eventM = new EventManager();
		}
		return eventM;
	}

	public void register(BaseSystem system, Class<? extends BaseEvent> event){
		if (!registeredSystems.containsKey(event)){
			registeredSystems.put(event, new ArrayList<BaseSystem>());
		}
		registeredSystems.get(event).add(system);
	}
	
	public void unregister(BaseSystem system, Class<? extends BaseEvent> event){
		if (registeredSystems.containsKey(event)){
			if (registeredSystems.get(event).contains(system)){
				registeredSystems.get(event).remove(system);
			}
		}
	}
	
	public void fire(BaseEvent event){
		System.out.println("firing: " + event.toString());
		if (!registeredSystems.containsKey(event.getClass())) return;
		for (BaseSystem system : registeredSystems.get(event.getClass())){
			system.onEventRecieve(event);
		}
	}
	
}
