package de.mih.core.engine.gamestates;

public abstract class BaseGameState {
	protected GameStateManager gamestateManager;
	
	public BaseGameState(GameStateManager gamestateManager)
	{
		this.gamestateManager = gamestateManager;
	}
	
	abstract public void onstart();
	abstract public void update();
	abstract public void render();
	abstract public void onend();
	abstract public void resize(int width, int height);
}
