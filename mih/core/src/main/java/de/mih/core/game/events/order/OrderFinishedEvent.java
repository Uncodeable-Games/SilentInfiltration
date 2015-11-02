package de.mih.core.game.events.order;

import de.mih.core.engine.ai.BaseOrder;
import de.mih.core.engine.ecs.events.BaseEvent;

public class OrderFinishedEvent extends BaseEvent
{
	public int entity;
	public BaseOrder order;
	
	public OrderFinishedEvent(int entity, BaseOrder order)
	{
		this.entity = entity;
		this.order = order;
	}
}
