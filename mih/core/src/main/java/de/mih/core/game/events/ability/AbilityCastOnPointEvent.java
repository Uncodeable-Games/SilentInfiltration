package de.mih.core.game.events.ability;

import com.badlogic.gdx.math.Vector3;

import de.mih.core.engine.ecs.events.BaseEvent.EntityEvent;

public class AbilityCastOnPointEvent extends EntityEvent
{
	public int abilityID;
	//public int casterID;
	public Vector3 target;
	
	public AbilityCastOnPointEvent(int casterID, Vector3 target, int abilityID)
	{
		super(casterID);
		this.abilityID = abilityID;
		//this.casterID = casterID;
		this.target = target;
	}
	
	public int getCasterId()
	{
		return entityId;
	}
}
