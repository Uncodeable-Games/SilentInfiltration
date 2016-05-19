package de.mih.core.game.systems;

import de.mih.core.engine.ai.navigation.pathfinder.Path;
import de.mih.core.engine.ecs.BaseSystem;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.ecs.SystemManager;
import de.mih.core.engine.ecs.events.BaseEvent;
import de.mih.core.engine.ecs.events.EventListener;
import de.mih.core.game.Game;
import de.mih.core.game.ai.orders.MoveOrder;
import de.mih.core.game.components.OrderableC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.events.order.OrderToPointEvent;

public class OrderSystem extends BaseSystem implements EventListener//<OrderToPointEvent>
{

	public OrderSystem(SystemManager systemManager, Game game)
	{
		super(systemManager, game);
		game.getEventManager().register(this);
	}

	@Override
	public boolean matchesSystem(int entityId)
	{
		return game.getEntityManager().hasComponent(entityId, OrderableC.class);
	}

	@Override
	public void update(double dt)
	{
	}

	@Override
	public void update(double dt, int entity)
	{
		OrderableC order = game.getEntityManager().getComponent(entity, OrderableC.class);
		if ((order.currentorder == null || order.currentorder.isFinished() || order.currentorder.isStopped()) && order.hasOrder())
			order.currentorder = order.getOrder();
		else if (order.currentorder != null && !order.currentorder.isFinished() && !order.currentorder.isStopped())
			order.currentorder.handle();
	}

	@Override
	public void render()
	{
	}

	@Override
	public void render(int entity)
	{
	}

	@Override
	public void handleEvent(BaseEvent event)
	{
		if(event instanceof OrderToPointEvent)
		{
			OrderToPointEvent oEvent = (OrderToPointEvent) event;
			
			EntityManager entityM = game.getEntityManager();
			PositionC actorpos = entityM.getComponent(oEvent.actor, PositionC.class);
			PositionC targetpos = entityM.getComponent(oEvent.target, PositionC.class);


			Path path = game.getNavigationManager().getPathfinder().getPath(actorpos.getPos(), targetpos.getPos());

			if (path == Path.getNoPath()){
				System.out.println("No Path found!");
				return;
			}
			
			OrderableC order = game.getEntityManager().getComponent(oEvent.actor, OrderableC.class);
			
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
