package de.mih.core.game.systems;

import de.mih.core.engine.ecs.BaseSystem;
import de.mih.core.engine.ecs.SystemManager;
import de.mih.core.game.Game;

public class PlayerSystem extends BaseSystem
{
	public PlayerSystem(SystemManager systemManager, Game game)
	{
		super(systemManager, game);
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
	public void render()
	{

	}

	@Override
	public void render(int entity)
	{

	}
}
