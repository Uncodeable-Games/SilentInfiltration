package de.mih.core.engine.world.object;

import java.util.ArrayList;

import de.mih.core.game.components.InteractableC;
import de.mih.core.game.player.Interaction;

public abstract class BaseObject {
	
	public InteractableC interactable;
	public ArrayList<Interaction> interactions = new ArrayList<Interaction>();
	
	public BaseObject(InteractableC interactable) {
		this.interactable = interactable;
	}
}
