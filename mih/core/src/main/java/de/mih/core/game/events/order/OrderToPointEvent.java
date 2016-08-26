package de.mih.core.game.events.order;

import com.badlogic.gdx.math.Vector3;
import de.mih.core.engine.ecs.events.BaseEvent.EntityEvent;

public class OrderToPointEvent extends EntityEvent
{

	//public int actor;
	public Vector3 target;

	public OrderToPointEvent(int entityId, Vector3 target)
	{
		//this.actor = actor;
		super(entityId);
		this.target = target;
	}
}
