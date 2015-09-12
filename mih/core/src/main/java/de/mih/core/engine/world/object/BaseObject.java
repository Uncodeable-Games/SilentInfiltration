package de.mih.core.engine.world.object;

import java.util.ArrayList;

import de.mih.core.game.components.InteractableC;

public abstract class BaseObject {
	
	public InteractableC interactable;
	public ArrayList<String> interactions = new ArrayList<String>();
	
	public BaseObject(InteractableC interactable) {
		this.interactable = interactable;
	}
	
	public abstract void onInteraction(int entity, String interaction);
}
