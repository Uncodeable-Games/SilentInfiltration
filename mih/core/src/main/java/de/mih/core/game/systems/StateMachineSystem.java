package de.mih.core.game.systems;

import de.mih.core.engine.ecs.BaseSystem;
import de.mih.core.engine.ecs.SystemManager;
import de.mih.core.game.GameLogic;
import de.mih.core.game.components.StateMachineComponent;

public class StateMachineSystem extends BaseSystem
{

	public StateMachineSystem(SystemManager systemManager, GameLogic gameLogic)
	{
		super(systemManager, gameLogic, 0);
	}

	public StateMachineSystem(SystemManager systemManager, GameLogic gameLogic, int priority)
	{
		super(systemManager, gameLogic, priority);
	}

	@Override
	public boolean matchesSystem(int entityId)
	{
		return gameLogic.getEntityManager().hasComponent(entityId, StateMachineComponent.class);
	}

	@Override
	public void update(double dt)
	{
	}

	@Override
	public void update(double dt, int entity)
	{
		gameLogic.getEntityManager().getComponent(entity, StateMachineComponent.class).current.update();
	}

}
