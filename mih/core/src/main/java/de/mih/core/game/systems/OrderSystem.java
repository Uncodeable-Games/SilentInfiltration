package de.mih.core.game.systems;

import de.mih.core.engine.ecs.BaseSystem;
import de.mih.core.engine.ecs.SystemManager;
import de.mih.core.engine.ecs.events.EventListener;
import de.mih.core.game.Game;
import de.mih.core.game.components.OrderableC;
import de.mih.core.game.events.order.OrderToPointEvent;

public class OrderSystem extends BaseSystem implements EventListener<OrderToPointEvent>
{

	public OrderSystem(SystemManager systemManager, Game game)
	{
		super(systemManager, game);
		game.getEventManager().register(OrderToPointEvent.class, this);
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
	public void handleEvent(OrderToPointEvent event)
	{
		// TODO Auto-generated method stub

	}
}
