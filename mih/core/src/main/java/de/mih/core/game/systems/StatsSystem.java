package de.mih.core.game.systems;

import de.mih.core.engine.ecs.BaseSystem;
import de.mih.core.engine.ecs.SystemManager;
import de.mih.core.engine.ecs.events.BaseEvent;
import de.mih.core.engine.ecs.events.EventListener;
import de.mih.core.game.GameLogic;
import de.mih.core.game.components.StatsC;
import de.mih.core.game.events.stats.DamageEvent;

/**
 * Created by Cataract on 15.04.2016.
 */
public class StatsSystem extends BaseSystem implements EventListener
{
	public StatsSystem(SystemManager systemManager, GameLogic gameLogic)
	{
		super(systemManager, gameLogic);
		gameLogic.getEventManager().register(this);
	}

	@Override
	public boolean matchesSystem(int entityId)
	{
		return GameLogic.getCurrentGame().getEntityManager().hasComponent(entityId, StatsC.class);
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
			int currentLive = GameLogic.getCurrentGame().getEntityManager().getComponent(dEvent.getTarget(),StatsC.class).getCurrentLife();
			currentLive -= dEvent.getDamage();
			GameLogic.getCurrentGame().getEntityManager().getComponent(dEvent.getTarget(),StatsC.class).setCurrentLife(currentLive);
			if (currentLive <= 0){
				GameLogic.getCurrentGame().getEntityManager().getComponent(dEvent.getTarget(),StatsC.class).setAlive(false);
				//GameLogic.getCurrentGame().getEntityManager().removeEntity(dEvent.getTarget());
			}
		}
		
	}
}
