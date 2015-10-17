package de.mih.core.game.events.orderevents;

import java.util.ArrayList;

import de.mih.core.engine.ecs.BaseSystem;
import de.mih.core.engine.ecs.events.BaseEvent;
import de.mih.core.game.MiH;
import de.mih.core.game.player.Player;

public class SelectEntity_Event extends BaseEvent {

	public Player selectingplayer;
	public int selectedentity;
	
	public SelectEntity_Event(Player p, int e) {
		selectingplayer = p;
		selectedentity = e;
	}
	
}
