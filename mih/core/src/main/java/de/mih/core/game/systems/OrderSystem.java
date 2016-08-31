package de.mih.core.game.systems;

import de.mih.core.engine.ai.navigation.pathfinder.Path;
import de.mih.core.engine.ecs.BaseSystem;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.ecs.SystemManager;
import de.mih.core.engine.ecs.events.BaseEvent;
import de.mih.core.engine.ecs.events.EventListener;
import de.mih.core.game.GameLogic;
import de.mih.core.game.ai.orders.MoveOrder;
import de.mih.core.game.components.OrderableC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.events.order.OrderToPointEvent;

public class OrderSystem extends BaseSystem implements EventListener//<OrderToPointEvent>
{

	public OrderSystem(SystemManager systemManager, GameLogic gameLogic)
	{
		super(systemManager, gameLogic);
		gameLogic.getEventManager().register(this);
	}

	@Override
	public boolean matchesSystem(int entityId)
	{
		return gameLogic.getEntityManager().hasComponent(entityId, OrderableC.class);
	}

	@Override
	public void update(double dt)
	{
	}

	@Override
	public void update(double dt, int entity)
	{
		OrderableC order = gameLogic.getEntityManager().getComponent(entity, OrderableC.class);
		if ((order.currentorder == null || order.currentorder.isFinished() || order.currentorder.isStopped()) && order.hasOrder())
			order.currentorder = order.getOrder();
		else if (order.currentorder != null && !order.currentorder.isFinished() && !order.currentorder.isStopped())
			order.currentorder.handle();
	}


	@Override
	public void handleEvent(BaseEvent event)
	{
		if(event instanceof OrderToPointEvent)
		{		
			OrderToPointEvent oEvent = (OrderToPointEvent) event;
			

			
			EntityManager entityM = gameLogic.getEntityManager();
			PositionC actorpos = entityM.getComponent(oEvent.entityId, PositionC.class);

			Path path = gameLogic.getNavigationManager().getPathfinder().getPath(actorpos.getPos(), oEvent.target);

			if (path == Path.getNoPath()){
				return;
			}
			OrderableC order = gameLogic.getEntityManager().getComponent(oEvent.entityId, OrderableC.class);
			
			order.isinit = false;
			if(order.currentorder != null && !order.currentorder.isFinished() && !order.currentorder.isStopped())
			{
				order.currentorder.stop();
			}
			order.addOrder(new MoveOrder(path));
		}
		// TODO Auto-generated method stub

	}
}
