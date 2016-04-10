package de.mih.core.game.player;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

public class Interaction
{
	
	public String  command;
	public Texture icon;

	int actor;
	int target;

	public InteractionListener listener;
	
	public ArrayList<String> filter = new ArrayList<String>();

	public Interaction(String c, Texture i)
	{
		command = c;
		icon = i;
	}

	public void setTarget(int t)
	{
		target = t;
	}

	public void setActor(int a)
	{
		actor = a;
	}

	public void interact()
	{
		listener.onInteraction(actor, target);
	}

	public static interface InteractionListener
	{
		public void onInteraction(int actor, int target);
	}

	// Interactions!
//	public static InteractionListener SIT = (int actor, int target) -> {
//	};
//
//	public static InteractionListener JUMP = (int actor, int target) -> {
//		EntityManager entityM = Game.getCurrentGame().getEntityManager();
//		entityM.getComponent(actor, PositionC.class).setPos(entityM.getComponent(actor, PositionC.class).getX()+1, entityM.getComponent(actor, PositionC.class).getY(), entityM.getComponent(actor, PositionC.class).getZ());
//	};
//
//	public static InteractionListener PETER = (int actor, int target) -> {
//		System.out.println("peter");
//	};
//	
//	public static InteractionListener MOVETO = (int actor, int target) -> {
//		EntityManager entityM = Game.getCurrentGame().getEntityManager();
//		PositionC actorpos = entityM.getComponent(actor, PositionC.class);
//		PositionC targetpos = entityM.getComponent(target, PositionC.class);
//		
//		Path path = Game.getCurrentGame().getNavigationManager().getPathfinder().getPath(actorpos.getPos(), targetpos.getPos());
//		OrderableC order = entityM.getComponent(actor,OrderableC.class);
//		order.newOrder(new MoveOrder(path));
//	};
}
