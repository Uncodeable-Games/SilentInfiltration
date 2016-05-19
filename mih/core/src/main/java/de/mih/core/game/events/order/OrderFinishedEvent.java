package de.mih.core.game.events.order;

import de.mih.core.engine.ai.BaseOrder;
import de.mih.core.engine.ecs.events.BaseEvent;

public class OrderFinishedEvent extends BaseEvent
{

	public int entity;
	public int orderID;
	//TODO: maybe order to order id, with manager or something, makes it easier for networking

	public OrderFinishedEvent(int entity, int orderID)
	{
		this.entity = entity;
		this.orderID = orderID;
	}
}
