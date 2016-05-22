package de.mih.core.game.systems;

import de.mih.core.engine.ecs.BaseSystem;
import de.mih.core.engine.ecs.SystemManager;
import de.mih.core.engine.ecs.events.BaseEvent;
import de.mih.core.engine.ecs.events.EventListener;
import de.mih.core.game.GameLogic;
import de.mih.core.game.components.AttachmentC;
import de.mih.core.game.events.order.SelectEvent;

public class PlayerSystem extends BaseSystem implements EventListener// <SelectEvent>
{
	public PlayerSystem(SystemManager systemManager, GameLogic gameLogic)
	{
		super(systemManager, gameLogic);
		gameLogic.getEventManager().register(this);
	}

	@Override
	public boolean matchesSystem(int entityId)
	{
		return false;
	}

	@Override
	public void update(double dt)
	{
	}

	@Override
	public void update(double dt, int entity)
	{
	}

	@Override
	public void handleEvent(BaseEvent event)
	{
		if (event instanceof SelectEvent)
		{
			SelectEvent sEvent = (SelectEvent) event;

			if (!gameLogic.getEntityManager().hasComponent(sEvent.entityId, AttachmentC.class))
			{
				gameLogic.getEntityManager().addComponent(sEvent.entityId,
						new AttachmentC(sEvent.entityId));
			}
			gameLogic.getEntityManager().getComponent(sEvent.entityId, AttachmentC.class).addAttachment(1,
					"selectioncircle");

		}
	}

}
