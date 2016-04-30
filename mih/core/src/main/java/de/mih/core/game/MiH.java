package de.mih.core.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import de.mih.core.engine.gamestates.GameStateManager;
import de.mih.core.game.gamestates.IntroGameState;
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

		Gdx.graphics.setDisplayMode(1600,900,false);
		Gdx.input.setCursorPosition(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);

		this.gamestateManager = new GameStateManager();
		IntroGameState    intro    = new IntroGameState(gamestateManager);
		MainMenuGameState mainMenu = new MainMenuGameState(gamestateManager);
		PlayingGameState  playing  = new PlayingGameState(gamestateManager);

		intro.setNextState(mainMenu);
		mainMenu.setNextState(playing);

		this.gamestateManager.addGameState("INTRO", intro, false);
		this.gamestateManager.addGameState("MAIN_MENU", mainMenu, false);
		this.gamestateManager.addGameState("PLAYING", playing, true);

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
		gamestateManager.resize(width, height);
	}
}
