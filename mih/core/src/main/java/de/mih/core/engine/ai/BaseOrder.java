package de.mih.core.engine.ai;

import de.mih.core.engine.ecs.events.BaseEvent;

public abstract class BaseOrder {
	
	abstract public void handle(int entity);
	
}
