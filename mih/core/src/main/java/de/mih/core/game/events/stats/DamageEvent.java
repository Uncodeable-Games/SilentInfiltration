package de.mih.core.game.events.stats;

import de.mih.core.engine.ecs.events.BaseEvent.EntityEvent;

/**
 * Created by Cataract on 15.04.2016.
 */
public class DamageEvent extends EntityEvent
{
	private int damage;
//	private int source;
	private int target;

	public DamageEvent(int damage, int source, int target)
	{
		super(source);
		this.onlyServerSends = true;
		this.damage = damage;
		//this.source = source;
		this.target = target;
	}

	public int getDamage()
	{
		return damage;
	}

	public int getSource()
	{
		return entityId;
	}

	public int getTarget()
	{
		return target;
	}
}
