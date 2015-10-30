package de.mih.core.game.player;

import java.util.ArrayList;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import de.mih.core.engine.ai.navigation.NavPoint;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.render.RenderManager;
import de.mih.core.engine.tilemap.Tile;
import de.mih.core.game.Game;
import de.mih.core.game.MiH;
import de.mih.core.game.ai.orders.MoveOrder;
import de.mih.core.game.components.OrderableC;
import de.mih.core.game.components.PositionC;

public class Interaction {
	
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
	};

	public static InteractionListener JUMP = (int actor, int target) -> {
		EntityManager entityM = Game.getCurrentGame().getEntityManager();
		entityM.getComponent(actor, PositionC.class).setPos(entityM.getComponent(actor, PositionC.class).getX()+1, entityM.getComponent(actor, PositionC.class).getY(), entityM.getComponent(actor, PositionC.class).getZ());
	};

	public static InteractionListener PETER = (int actor, int target) -> {
		System.out.println("peter");
	};
	
	public static InteractionListener MOVETO = (int actor, int target) -> {
		EntityManager entityM = Game.getCurrentGame().getEntityManager();
		PositionC actorpos = entityM.getComponent(actor, PositionC.class);
		PositionC targetpos = entityM.getComponent(target, PositionC.class);
		
		Tile start = Game.getCurrentGame().getTilemap().getTileAt(actorpos.getPos().x, actorpos.getPos().y);
		Tile end = Game.getCurrentGame().getTilemap().getTileAt(targetpos.getPos().x, targetpos.getPos().y);
		
		Map<Tile, Tile> path = Game.getCurrentGame().getPathfinder().findShortesPath(start, end);
		
//		NavPoint[] path = Game.getCurrentGame().getPathfinder().getPath(actorpos.getPos(), targetpos.getPos());
		OrderableC order = entityM.getComponent(actor,OrderableC.class);
		order.newOrder(new MoveOrder(targetpos.getPos(), start, end, path, Game.getCurrentGame().getTilemap()));
	};
}
