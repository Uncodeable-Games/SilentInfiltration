package de.mih.core.engine.ecs;

import de.mih.core.game.Game;

/**
 * Systems work on entities that have the wanted components.
 *
 * @author Tobias
 */
public abstract class BaseSystem implements Comparable<BaseSystem>
{
	// #38
	protected int  priority;
	protected Game game;
	
	public BaseSystem(SystemManager systemManager, Game game)
	{
		this(systemManager, game, 1);
	}
	
	public BaseSystem(SystemManager systemManager, Game game, int priority)
	{
		this.game = game;
		systemManager.register(this);
		this.priority = priority;
	}

	/**
	 * check if the enitity has the needed components
	 *
	 * @param entity
	 * @return
	 */
	public abstract boolean matchesSystem(int entityId);

	/**
	 * Call for the game-loop, here all needed changes on the data is made.
	 * In contrast to update(double dt, int entity) this is independent of the entities
	 *
	 * @param dt Time in ms since the last call
	 */
	public abstract void update(double dt);
	
	/**
	 * Call for the game-loop, here all needed changes on the data is made.
	 *
	 * @param dt Time in ms since the last call
	 */
	public abstract void update(double dt, int entity);

	/**
	 * Call for the game-loop, only needed for systems that want to render
	 * something. In contrast to render(int entity) this is independent of the entities
	 */
//	public abstract void render();
	
	/**
	 * Call for the game-loop, only needed for systems that want to render
	 * something.
	 */
//	public abstract void render(int entity);

//	public abstract void onEventRecieve(BaseEvent event);
	
	@Override
	public int compareTo(BaseSystem other)
	{
		int result = priority - other.priority;
		if (result < 0)
			return 1;
		if (result == 0)
			return 0;
		return -1;
	}
}
