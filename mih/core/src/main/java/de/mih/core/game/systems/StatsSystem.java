package de.mih.core.game.systems;

import de.mih.core.engine.ecs.BaseSystem;
import de.mih.core.engine.ecs.SystemManager;
import de.mih.core.engine.ecs.events.BaseEvent;
import de.mih.core.engine.ecs.events.EventListener;
import de.mih.core.game.Game;
import de.mih.core.game.components.StatsC;
import de.mih.core.game.events.order.DamageEvent;

/**
 * Created by Cataract on 15.04.2016.
 */
public class StatsSystem extends BaseSystem implements EventListener
{
	public StatsSystem(SystemManager systemManager, Game game)
	{
		super(systemManager, game);
		game.getEventManager().register(this);
	}

	@Override
	public boolean matchesSystem(int entityId)
	{
		return Game.getCurrentGame().getEntityManager().hasComponent(entityId, StatsC.class);
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
		if(event instanceof DamageEvent)
		{
			DamageEvent dEvent = (DamageEvent) event;
			if (Game.getCurrentGame().getEntityManager().getComponent(dEvent.getTarget(),StatsC.class).getCurrentLife() <= 0){
				Game.getCurrentGame().getEntityManager().removeEntity(dEvent.getTarget());
			}
		}
		
	}
}
