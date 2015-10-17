package de.mih.core.engine.ecs;

import java.util.ArrayList;
import java.util.HashMap;

import de.mih.core.engine.ecs.events.BaseEvent;
import de.mih.core.engine.ecs.events.EventListener;

public class EventManager
{


	HashMap<Class<? extends BaseEvent>, ArrayList<BaseSystem>> registeredSystems = new HashMap<Class<? extends BaseEvent>, ArrayList<BaseSystem>>();
	HashMap<Class<? extends BaseEvent>, ArrayList<EventListener<? extends BaseEvent>>> registeredHandlers = new HashMap<>();



	public void register(Class<? extends BaseEvent> eventType, EventListener<? extends BaseEvent> eventListener)
	{
		if (!registeredHandlers.containsKey(eventType))
		{
			registeredHandlers.put(eventType, new ArrayList<EventListener<? extends BaseEvent>>());
		}
		registeredHandlers.get(eventType).add(eventListener);
	}

	public void register(BaseSystem system, Class<? extends BaseEvent> eventType)
	{
		if (!registeredSystems.containsKey(eventType))
		{
			registeredSystems.put(eventType, new ArrayList<BaseSystem>());
		}
		registeredSystems.get(eventType).add(system);
	}

	public void unregister(BaseSystem system, Class<? extends BaseEvent> eventType)
	{
		if (registeredSystems.containsKey(eventType))
		{
			if (registeredSystems.get(eventType).contains(system))
			{
				registeredSystems.get(eventType).remove(system);
			}
		}
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
		if (!registeredSystems.containsKey(event.getClass()))
			return;
		for (BaseSystem system : registeredSystems.get(event.getClass()))
		{
			system.onEventRecieve(event);
		}
	}

}
