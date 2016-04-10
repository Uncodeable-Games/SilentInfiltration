package de.mih.core.game.events.order;

import de.mih.core.engine.ecs.events.BaseEvent;
import de.mih.core.game.player.Player;

public class SelectEvent extends BaseEvent
{

	public Player selectingplayer;
	public int    selectedentity;
	
	public SelectEvent(Player p, int e)
	{
		selectingplayer = p;
		selectedentity = e;
	}
}
