package de.mih.core.game.events.order;

import de.mih.core.engine.ecs.events.BaseEvent;
import de.mih.core.game.player.Player;

public class SelectEvent extends BaseEvent
{

	//public Player selectingplayer;
	public int playerID;
	public int selectedentity;

	public SelectEvent(int playerID, int e)
	{
		this.playerID = playerID;
		this.selectedentity = e;
	}

	public int getSelectingplayer()
	{
		return playerID;
	}

	public int getSelectedentity()
	{
		return selectedentity;
	}
}
