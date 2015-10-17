package de.mih.core.engine.ecs.events;

public interface EventListener<T extends BaseEvent>
{
	//public default Class<?> getEventType() { return T; }
	public void handleEvent(T event);
}
