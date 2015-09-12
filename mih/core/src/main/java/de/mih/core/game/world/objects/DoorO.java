package de.mih.core.game.world.objects;

import de.mih.core.engine.world.object.BaseObject;
import de.mih.core.game.components.InteractableC;

public class DoorO extends BaseObject {

	public DoorO(InteractableC interactable) {
		super(interactable);
	}

	public boolean open = true;
	public boolean locked = false;
	public boolean smashed = false;
	
	@Override
	public void onInteraction(int enitity, String interaction) {
		switch (interaction) {
		case "open": {
			open = false;
			break;
		}
		case "close": {
			open = true;
			break;
		}
		case "lock": {
			locked = true;
			break;
		}
		case "unlock": {
			locked = false;
			break;
		}
		case "smash": {
			open = true;
			locked = false;
			smashed = true;
			break;
		}
		case "lookkeyhole": {
			break;
		}
		}
	}

}
