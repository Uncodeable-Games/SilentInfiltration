package de.mih.core.game.systems;

import de.mih.core.engine.ecs.BaseSystem;
import de.mih.core.engine.ecs.SystemManager;
import de.mih.core.game.Game;
import de.mih.core.game.components.StateMachineComponent;

public class StateMachineSystem extends BaseSystem
{

	public StateMachineSystem(SystemManager systemManager, Game game)
	{
		super(systemManager, game, 0);
	}

	public StateMachineSystem(SystemManager systemManager, Game game, int priority)
	{
		super(systemManager, game, priority);
	}

	@Override
	public boolean matchesSystem(int entityId)
	{
		return game.getEntityManager().hasComponent(entityId, StateMachineComponent.class);
	}

	@Override
	public void update(double dt)
	{
	}

	@Override
	public void update(double dt, int entity)
	{
		game.getEntityManager().getComponent(entity, StateMachineComponent.class).current.update();
	}

	@Override
	public void render()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void render(int entity)
	{
		// TODO Auto-generated method stub

	}
}
