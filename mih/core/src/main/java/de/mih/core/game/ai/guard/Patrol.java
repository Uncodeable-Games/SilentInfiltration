package de.mih.core.game.ai.guard;

import java.util.Map;

import de.mih.core.engine.ai.BaseOrder;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.tilemap.Tile;
import de.mih.core.game.Game;
import de.mih.core.game.ai.orders.MoveOrder;
import de.mih.core.game.components.OrderableC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.StateMachineComponent;
import de.mih.core.game.components.StateMachineComponent.State;
import de.mih.core.game.events.order.OrderToPointEvent;

public class Patrol extends State{

	Game game;
	int currentWaypoint = -1;
	int currentIndex = 0;
	BaseOrder currentOrder;
	
	public Patrol(StateMachineComponent stateMachine, Game game) {
		super(stateMachine);
		this.game = game;
	}

	@Override
	public void onEnter() {
		if(currentWaypoint == -1)
		{
			currentWaypoint = game.waypoints.get(0);
		}
		OrderableC order = game.getEntityManager().getComponent(stateMachine.entityID,OrderableC.class);
		if(currentOrder != null)
			order.addOrder(currentOrder);
	}

	@Override
	public void onLeave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		//System.out.println("PATROL: " + this.stateMachine.entityID);
		if(currentOrder == null)
		{
			order();
		}
		if(currentOrder.isFinished())
		{
			currentIndex++;
			if(currentIndex >= game.waypoints.size())
			{
				currentIndex = 0;
			}
			System.out.println("INCREMENT");
			currentWaypoint = game.waypoints.get(currentIndex);
			order();
		}
		else
		{
			OrderableC order = game.getEntityManager().getComponent(stateMachine.entityID,OrderableC.class);
			order.removeOrder(currentOrder);
		}
	}
	
	public void order()
	{
		EntityManager entityM = game.getEntityManager();
		PositionC actorpos = entityM.getComponent(stateMachine.entityID, PositionC.class);
		PositionC targetpos = entityM.getComponent(currentWaypoint, PositionC.class);

		float x1 = game.getTilemap().coordToIndex_x(actorpos.getPos().x);
		float y1 = game.getTilemap().coordToIndex_z(actorpos.getPos().z);
		float x2 = game.getTilemap().coordToIndex_x(targetpos.getPos().x);
		float y2 = game.getTilemap().coordToIndex_z(targetpos.getPos().z);
		System.out.println("[" + x1 + ", " + y1 + "]");
		System.out.println("[" + x2 + ", " + y2 + "]");
		


		Tile start = game.getTilemap().getTileAt(x1,y1);
		Tile end = game.getTilemap().getTileAt(x2,y2);
		System.out.println("start: " + start);
		System.out.println("end: " + end);
		Map<Tile, Tile> path = game.getPathfinder().findShortesPath(start, end);

		if(start == null || end == null || path.containsValue(null))
			throw new RuntimeException("nope!");
		
		OrderableC order = game.getEntityManager().getComponent(stateMachine.entityID,OrderableC.class);
		Game.getCurrentGame().getEventManager().fire(new OrderToPointEvent(stateMachine.entityID, end.getCenter()));

		currentOrder = new MoveOrder(targetpos.getPos(), start, end, path, game.getTilemap());
		order.addOrder(currentOrder);
	}

}
