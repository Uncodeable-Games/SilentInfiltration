package de.mih.core.game.world.objects;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.world.object.BaseObject;
import de.mih.core.game.components.InteractableC;
import de.mih.core.game.components.PositionC;

public class ChairO extends BaseObject {

	public ChairO(InteractableC inter) {
		super(inter);
		interactions.add("sit");
		interactions.add("jump");
		interactions.add("peter");
		// interactions.add("sit");
		// interactions.add("jump");
		// interactions.add("peter");
	}

	@Override
	public void onInteraction(int entity, String interaction) {
		EntityManager entityM = EntityManager.getInstance();
		switch (interaction) {
		case "sit": {
			entityM.getComponent(entity, PositionC.class).position = entityM
					.getComponent(entityM.getEntityByComponent(interactable), PositionC.class).position.cpy();
			break;
		}
		case "jump": {
			entityM.getComponent(entity, PositionC.class).position.y += 1f;
			break;
		}
		case "peter": {
			System.out.println("peter");
		}
		}

	}

}
