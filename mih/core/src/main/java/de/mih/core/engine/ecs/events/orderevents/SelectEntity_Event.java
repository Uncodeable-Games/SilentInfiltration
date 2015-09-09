package de.mih.core.engine.ecs.events.orderevents;

import java.util.ArrayList;

import de.mih.core.engine.ecs.BaseSystem;
import de.mih.core.engine.ecs.events.BaseEvent;
import de.mih.core.game.MiH;
import de.mih.core.game.player.Player;

public class SelectEntity_Event extends BaseEvent {

	static ArrayList<BaseSystem> registeredSystems = new ArrayList<BaseSystem>();
	
	static public void register(BaseSystem system) {
		registeredSystems.add(system);
	}
	
	static public void unregister(BaseSystem system){
		if (registeredSystems.contains(system)){
			registeredSystems.remove(system);
		}
	}
	
	public static void fire(Player p, int e){	
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(p);
		params.add(e);
		for (BaseSystem system: registeredSystems){
			system.onEventRecieve(SelectEntity_Event.class,params);
		}
	}
	
}
