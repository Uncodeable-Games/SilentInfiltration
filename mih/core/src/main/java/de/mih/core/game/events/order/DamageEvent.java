package de.mih.core.game.events.order;

import de.mih.core.engine.ecs.events.BaseEvent;

/**
 * Created by Cataract on 15.04.2016.
 */
public class DamageEvent extends BaseEvent
{
	private int damage;
	private int source;
	private int target;

	public DamageEvent(int damage, int source, int target)
	{
		this.damage = damage;
		this.source = source;
		this.target = target;
	}

	public int getDamage()
	{
		return damage;
	}

	public int getSource()
	{
		return source;
	}

	public int getTarget()
	{
		return target;
	}
}
