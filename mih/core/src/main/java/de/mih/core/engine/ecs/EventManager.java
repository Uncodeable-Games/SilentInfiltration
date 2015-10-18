package de.mih.core.engine.ecs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import de.mih.core.engine.ecs.events.BaseEvent;
import de.mih.core.engine.ecs.events.EventListener;

public class EventManager
{


//	HashMap<Class<? extends BaseEvent>, ArrayList<BaseSystem>> registeredSystems = new HashMap<Class<? extends BaseEvent>, ArrayList<BaseSystem>>();
	HashMap<Class<? extends BaseEvent>, ArrayList<EventListener<? extends BaseEvent>>> registeredHandlers = new HashMap<>();

	LinkedList<BaseEvent> eventQueue = new LinkedList<>();

	public void register(Class<? extends BaseEvent> eventType, EventListener<? extends BaseEvent> eventListener)
	{
		if (!registeredHandlers.containsKey(eventType))
		{
			registeredHandlers.put(eventType, new ArrayList<EventListener<? extends BaseEvent>>());
		}
		registeredHandlers.get(eventType).add(eventListener);
	}

	public void unregister(Class<? extends BaseEvent> eventType, EventListener<? extends BaseEvent> eventListener)
	{
		if (registeredHandlers.containsKey(eventType))
		{
			if (registeredHandlers.get(eventType).contains(eventListener))
			{
				registeredHandlers.get(eventType).remove(eventListener);
			}
		}
	}
	
//	public void register(BaseSystem system, Class<? extends BaseEvent> eventType)
//	{
//		if (!registeredSystems.containsKey(eventType))
//		{
//			registeredSystems.put(eventType, new ArrayList<BaseSystem>());
//		}
//		registeredSystems.get(eventType).add(system);
//	}
//
//	public void unregister(BaseSystem system, Class<? extends BaseEvent> eventType)
//	{
//		if (registeredSystems.containsKey(eventType))
//		{
//			if (registeredSystems.get(eventType).contains(system))
//			{
//				registeredSystems.get(eventType).remove(system);
//			}
//		}
//	}

	public void queueEvent(BaseEvent event)
	{
		this.eventQueue.add(event);
	}
	
	public void update()
	{
		if(eventQueue.isEmpty())
			return;
		BaseEvent event = this.eventQueue.poll();
		this.fire(event);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void fire(BaseEvent event)
	{
		if (registeredHandlers.containsKey(event.getClass()))
		{
			for(EventListener listener : registeredHandlers.get(event.getClass()))
			{
				listener.handleEvent(event);
			}
		}
//		if (!registeredSystems.containsKey(event.getClass()))
//			return;
//		for (BaseSystem system : registeredSystems.get(event.getClass()))
//		{
//			system.onEventRecieve(event);
//		}
	}

}
