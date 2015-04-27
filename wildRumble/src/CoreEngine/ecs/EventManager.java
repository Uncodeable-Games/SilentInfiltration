package CoreEngine.ecs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {
	
	Map<Integer, List<BaseSystem>> registeredSystems;
	
	public EventManager(){
		registeredSystems = new HashMap<Integer, List<BaseSystem>>();
	}
	
	public void sendEvent(Event e){
		List<BaseSystem> systems = registeredSystems.get(e.eventType);
		for(int i = 0; i < systems.size(); i++){
			systems.get(i).receiveEvent(e);
		}
	}
	
	
	public void registerForEvent(BaseSystem system, int eventType){
		List<BaseSystem> list = registeredSystems.get(eventType);
		if(list == null){
			list = new ArrayList<BaseSystem>();	
			registeredSystems.put(eventType, list);
		}
		list.add(system);
	}
}
