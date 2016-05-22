package de.mih.core.game.events.order;

import com.badlogic.gdx.math.Vector3;

import de.mih.core.engine.ecs.events.BaseEvent.EntityEvent;

public class AbilityCastOnTargetEvent extends EntityEvent
{
	public int abilityID;
//	public int casterID;
	public int targetID;
	public Vector3 intersection;
	
	public AbilityCastOnTargetEvent(int casterID, int targetID, Vector3 intersection, int abilityID)
	{
		super(casterID);
		this.abilityID = abilityID;
		this.targetID = targetID;
		//this.casterID = casterID;
		this.intersection = intersection;
	}
	
	public int getCasterId()
	{
		return entityId;
	}
}
