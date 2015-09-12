package de.mih.core.game.player;

import com.badlogic.gdx.graphics.Texture;

import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.game.components.PositionC;

public class Interaction {

	public String command;
	public Texture icon;

	int actor;
	int target;

	public InteractionListener listener;

	public Interaction(String c, Texture i) {
		command = c;
		icon = i;
	}

	public void setTarget(int t) {
		target = t;
	}

	public void setActor(int a) {
		actor = a;
	}

	public void interact() {
		listener.onInteraction(actor, target);
	}

	public static interface InteractionListener {
		public void onInteraction(int actor, int target);
	}

	// Interactions!
	public static InteractionListener SIT = (int actor, int target) -> {
		EntityManager entityM = EntityManager.getInstance();
		entityM.getComponent(actor, PositionC.class).position = entityM.getComponent(target, PositionC.class).position
				.cpy();
	};

	public static InteractionListener JUMP = (int actor, int target) -> {
		EntityManager entityM = EntityManager.getInstance();
		entityM.getComponent(actor, PositionC.class).position.y += 1;
	};

	public static InteractionListener PETER = (int actor, int target) -> {
		System.out.println("peter");
	};
	//
}
