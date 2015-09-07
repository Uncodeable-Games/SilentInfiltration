package de.mih.core.engine.ecs.events;

import java.util.ArrayList;

import de.mih.core.engine.ecs.BaseSystem;
import de.mih.core.game.MiH;
import de.mih.core.game.player.Player;

public class SelectEntity_Event extends BaseEvent {
	static String name = "selectEntity";

	static ArrayList<BaseSystem> registeredSystems = new ArrayList<BaseSystem>();
	
	static Player selectingPlayer;
	static Integer selectedEntity = -1;
	
	static public void register(BaseSystem system) {
		registeredSystems.add(system);
	}
	
	static public void unregister(BaseSystem system){
		if (registeredSystems.contains(system)){
			registeredSystems.remove(system);
		}
	}
	
	public static void fire(Player p, int e){
		p.clearSelection();
		p.selectUnit(e);
		
		selectedEntity = (Integer) e;
		selectingPlayer = p;
		for (BaseSystem system: registeredSystems){
			system.onEventRecieve(SelectEntity_Event.class);
		}
	}
	
	public static Player getSelectingPlayer(){
		return selectingPlayer;
	}
	
	public static int getSelectedEntity(){
		return selectedEntity;
	}
}
