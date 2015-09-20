package de.mih.core.game.player;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.ecs.RenderManager;
import de.mih.core.engine.tilemap.Tile;
import de.mih.core.game.Game;
import de.mih.core.game.MiH;
import de.mih.core.game.ai.orders.MoveOrder;
import de.mih.core.game.components.OrderableC;
import de.mih.core.game.components.PositionC;

public class Interaction {

	//TODO: CHANGE
	public static Game game;
	
	public String command;
	public Texture icon;

	int actor;
	int target;

	public InteractionListener listener;
	
	public ArrayList<String> filter = new ArrayList<String>();

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
	
	public static InteractionListener MOVETO = (int actor, int target) -> {
		EntityManager entityM = EntityManager.getInstance();
		PositionC actorpos = entityM.getComponent(actor, PositionC.class);
		PositionC targetpos = entityM.getComponent(target, PositionC.class);
		System.out.println(actorpos.position+" ; "+targetpos.position);
		//TODO: refactor
		Tile start = game.tilemap.getTileAt((int)actorpos.position.x, (int)actorpos.position.z);
		Tile end = game.tilemap.getTileAt(game.tilemap.coordToIndex_x((int)targetpos.position.x),game.tilemap.coordToIndex_z((int)targetpos.position.z));
		MoveOrder order = new MoveOrder(RenderManager.getInstance().getMouseTarget(0f, Gdx.input),
				start, end, game.pathfinder.findShortesPath(start, end),	game.tilemap);
		
		entityM.getComponent(actor, OrderableC.class).newOrder(order);

	};
}
