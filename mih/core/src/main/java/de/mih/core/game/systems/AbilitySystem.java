package de.mih.core.game.systems;

import de.mih.core.engine.ecs.BaseSystem;
import de.mih.core.engine.ecs.SystemManager;
import de.mih.core.engine.ecs.events.BaseEvent;
import de.mih.core.engine.ecs.events.EventListener;
import de.mih.core.game.Game;
import de.mih.core.game.GameLogic;
import de.mih.core.game.components.AbilityC;
import de.mih.core.game.events.order.AbilityCastOnPointEvent;
import de.mih.core.game.events.order.AbilityCastOnTargetEvent;

public class AbilitySystem extends BaseSystem implements EventListener
{

	public AbilitySystem(SystemManager systemManager, GameLogic gameLogic)
	{
		super(systemManager, gameLogic);
		// TODO Auto-generated constructor stub
		gameLogic.getEventManager().register(this);
	}

	@Override
	public boolean matchesSystem(int entityId)
	{
		return GameLogic.getCurrentGame().getEntityManager().hasComponent(entityId, AbilityC.class);
	}

	@Override
	public void update(double dt)
	{
		// TODO Auto-generated method stub
		//Game.getCurrentGame().getAbilityManager().
	}

	@Override
	public void update(double dt, int entity)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void handleEvent(BaseEvent event)
	{
		if(event instanceof AbilityCastOnPointEvent)
		{
			AbilityCastOnPointEvent abilityEvent = (AbilityCastOnPointEvent) event;
			Game.getCurrentGame().getAbilityManager().getAbilityById(abilityEvent.abilityID).castOnPoint(abilityEvent.getCasterId(), abilityEvent.target);
		}
		else if(event instanceof AbilityCastOnTargetEvent)
		{
			AbilityCastOnTargetEvent abilityEvent = (AbilityCastOnTargetEvent) event;
			Game.getCurrentGame().getAbilityManager().getAbilityById(abilityEvent.abilityID).castOnTarget(abilityEvent.getCasterId(), abilityEvent.targetID, abilityEvent.intersection);
		}
	}
	
	

}
