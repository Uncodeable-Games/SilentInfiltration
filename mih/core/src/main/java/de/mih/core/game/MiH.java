package de.mih.core.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;

import de.mih.core.engine.gamestates.GameStateManager;
import de.mih.core.game.gamestates.IntroGameState;
import de.mih.core.game.gamestates.MainMenuGameState;
import de.mih.core.game.gamestates.PlayingGameState;
import de.mih.core.game.gamestates.PlayingGameState2;

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
		IntroGameState intro = new IntroGameState(gamestateManager);
		MainMenuGameState mainMenu = new MainMenuGameState(gamestateManager);
		PlayingGameState playing = new PlayingGameState(gamestateManager);
		PlayingGameState2 playing2 = new PlayingGameState2(gamestateManager);

		intro.setNextState(mainMenu);
		mainMenu.setNextState(playing);

		this.gamestateManager.addGameState("INTRO", intro, false);
		this.gamestateManager.addGameState("MAIN_MENU", mainMenu, true);
		this.gamestateManager.addGameState("PLAYING", playing, false);
		this.gamestateManager.addGameState("PLAYING2", playing2, false);


		this.gamestateManager.init();
	}

	public void render()
	{
		this.gamestateManager.getCurrentGameState().update(Gdx.graphics.getDeltaTime());
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
