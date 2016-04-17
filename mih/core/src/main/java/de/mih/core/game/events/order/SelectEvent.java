package de.mih.core.game.events.order;

import de.mih.core.engine.ecs.events.BaseEvent;
import de.mih.core.game.player.Player;

public class SelectEvent extends BaseEvent
{

	private Player selectingplayer;
	private int    selectedentity;
	
	public SelectEvent(Player p, int e)
	{
		selectingplayer = p;
		selectedentity = e;
	}

	public Player getSelectingplayer()
	{
		return selectingplayer;
	}

	public int getSelectedentity()
	{
		return selectedentity;
	}
}
