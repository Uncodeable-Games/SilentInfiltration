package de.mih.core.game;

import com.badlogic.gdx.ApplicationAdapter;

import de.mih.core.engine.gamestates.GameStateManager;

public class MiH extends ApplicationAdapter
{
	GameStateManager gamestateManager;

	public void create()
	{
		this.gamestateManager = new GameStateManager();
		this.gamestateManager.init();
		// GameStateManager.getInstance().init();
	}

	public void render()
	{
		this.gamestateManager.getCurrentGameState().update();
		this.gamestateManager.getCurrentGameState().render();
	}
}
