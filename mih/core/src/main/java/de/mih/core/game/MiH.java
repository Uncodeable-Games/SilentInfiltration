package de.mih.core.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;

import de.mih.core.engine.gamestates.GameStateManager;
import de.mih.core.game.gamestates.IntroGameState;
import de.mih.core.game.gamestates.LobbyState;
import de.mih.core.game.gamestates.MainMenuGameState;
import de.mih.core.game.gamestates.PlayingGameState;

public class MiH extends ApplicationAdapter
{
	GameStateManager gamestateManager;
	static MiH instance;

	public MiH()
	{
		instance = this;
	}

	public static MiH getInstance()
	{
		return instance;
	}

	public void create()
	{

		this.gamestateManager = new GameStateManager();
		MainMenuGameState mainMenu = new MainMenuGameState(gamestateManager);
		PlayingGameState playing = new PlayingGameState(gamestateManager);
		
		mainMenu.setNextState(playing);

		this.gamestateManager.addGameState("MAIN_MENU", mainMenu, true);
		this.gamestateManager.addGameState("PLAYING", playing, false);

		this.gamestateManager.init();
	}

	public void render()
	{
		this.gamestateManager.getCurrentGameState().update();
		this.gamestateManager.getCurrentGameState().render();
	}

	@Override
	public void dispose()
	{
	}

	@Override
	public void resize(int width, int height)
	{
		
	}
}
