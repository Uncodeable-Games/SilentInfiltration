package de.mih.core.game;

import com.badlogic.gdx.ApplicationAdapter;

import de.mih.core.engine.gamestates.GameStateManager;
import de.mih.core.game.gamestates.IntroGameState;
import de.mih.core.game.gamestates.MainMenuGameState;
import de.mih.core.game.gamestates.PlayingGameState;

public class MiH extends ApplicationAdapter
{
	GameStateManager gamestateManager;

	public void create()
	{

		this.gamestateManager = new GameStateManager();
		IntroGameState intro = new IntroGameState(gamestateManager);
		MainMenuGameState mainMenu = new MainMenuGameState(gamestateManager);
		PlayingGameState playing = new PlayingGameState(gamestateManager);
		
		intro.setNextState(mainMenu);
		mainMenu.setNextState(playing);
		
		this.gamestateManager.addGameState(intro, true);
		this.gamestateManager.addGameState(mainMenu, false);
		this.gamestateManager.addGameState(playing, false);

		this.gamestateManager.init();
	}

	public void render()
	{
		this.gamestateManager.getCurrentGameState().update();
		this.gamestateManager.getCurrentGameState().render();
	}
}
