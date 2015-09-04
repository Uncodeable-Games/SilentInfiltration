package de.mih.core.engine.ecs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EventManager {
	Map<String, List<BaseSystem>> registeredSystems;
	//Map<Class<? extends Event>, List<BaseSystem>> registeredSystems;
	//HashMap<Class<? extends Event>, HashMap<BaseSystem, Event>> registeredSystems = new HashMap<Class<? extends Event>, HashMap<BaseSystem,Event>>();

	public EventManager(){
		registeredSystems = new HashMap<String, List<BaseSystem>>();
		//registeredSystems = new HashMap<Integer, List<BaseSystem>>();
	}
	
	@SuppressWarnings("rawtypes")
	public void sendEvent(Event e, double dt){
		List<BaseSystem> systems;// = registeredSystems.get(e.eventType);
		if(!registeredSystems.containsKey(e.eventType))
			return;
		systems = registeredSystems.get(e.eventType);
		for(int i = 0; i < systems.size(); i++){
			systems.get(i).receiveEvent(e, dt);
		}
	}
	
	public void registerForEvent(BaseSystem system, String eventType){
		List<BaseSystem> list = registeredSystems.get(eventType);
		if(list == null){
			list = new ArrayList<BaseSystem>();
			registeredSystems.put(eventType,list);
		}
		list.add(system);
	}
//	public <T extends Event> void registerForEvent(BaseSystem system, Class<T> eventType){
//		List<BaseSystem> list = registeredSystems.get(eventType);
//		if(list == null){
//			list = new ArrayList<BaseSystem>();	
//			registeredSystems.put(eventType, list);
//		}
//		list.add(system);
//	}
}
