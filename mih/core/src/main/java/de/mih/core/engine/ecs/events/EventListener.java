package de.mih.core.engine.ecs.events;

public interface EventListener//<T extends BaseEvent>
{
	public void handleEvent(BaseEvent event);
}
