package de.mih.core.game.events.order;

import com.badlogic.gdx.math.Vector3;
import de.mih.core.engine.ecs.events.BaseEvent;

public class OrderToPointEvent extends BaseEvent
{

	public int actor;
	public Vector3 target;

	public OrderToPointEvent(int actor, Vector3 target)
	{
		this.actor = actor;
		this.target = target;
	}
}
