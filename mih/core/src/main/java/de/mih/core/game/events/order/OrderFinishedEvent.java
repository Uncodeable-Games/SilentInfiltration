package de.mih.core.game.events.order;

import de.mih.core.engine.ai.BaseOrder;
import de.mih.core.engine.ecs.events.BaseEvent.EntityEvent;

public class OrderFinishedEvent extends EntityEvent
{

	public int orderID;
	//TODO: maybe order to order id, with manager or something, makes it easier for networking

	public OrderFinishedEvent(int entity, int orderID)
	{
		super(entity);
		this.orderID = orderID;
	}
}
