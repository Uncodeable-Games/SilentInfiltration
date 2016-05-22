package de.mih.core.game.events.order;

import de.mih.core.engine.ecs.events.BaseEvent.EntityEvent;
import de.mih.core.game.player.Player;

public class SelectEvent extends EntityEvent
{

	//public Player selectingplayer;
	public int playerID;
	//public int selectedentity;

	public SelectEvent(int playerID, int entityId)
	{
		super(entityId);
		this.playerID = playerID;
		//this.selectedentity = e;
	}

	public int getSelectingplayer()
	{
		return playerID;
	}

	public int getSelectedentity()
	{
		return entityId;
	}
}
