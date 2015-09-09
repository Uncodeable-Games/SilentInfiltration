package de.mih.core.engine.ecs.events.orderevents;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;

import de.mih.core.engine.ecs.BaseSystem;
import de.mih.core.engine.ecs.events.BaseEvent;
import de.mih.core.game.player.Player;

public class OrderToPoint_Event extends BaseEvent {
static ArrayList<BaseSystem> registeredSystems = new ArrayList<BaseSystem>();
	
	static public void register(BaseSystem system) {
		registeredSystems.add(system);
	}
	
	static public void unregister(BaseSystem system){
		if (registeredSystems.contains(system)){
			registeredSystems.remove(system);
		}
	}
	
	public static void fire( int e, Vector3 target_point, Object order){	
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(e);
		params.add(target_point);
		params.add(order);
		for (BaseSystem system: registeredSystems){
			system.onEventRecieve(OrderToPoint_Event.class,params);
		}
	}
}
