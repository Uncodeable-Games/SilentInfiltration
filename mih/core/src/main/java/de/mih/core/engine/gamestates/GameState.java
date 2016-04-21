package de.mih.core.engine.gamestates;

public abstract class GameState
{
	protected GameStateManager gamestateManager;
	GameState previous, next;
	
	public GameState(GameStateManager gamestateManager)
	{
		this.gamestateManager = gamestateManager;
	}
	
	public void setNextState(GameState next)
	{
		this.next = next;
		next.previous = this;
	}

	abstract public void onEnter();

	abstract public void update();

	abstract public void render();

	abstract public void onLeave();

	abstract public void resize(int width, int height);
}
