package de.mih.core.game.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import de.mih.core.engine.gamestates.GameState;
import de.mih.core.engine.gamestates.GameStateManager;
import de.mih.core.game.Game;

public class PlayingGameState extends GameState
{

	public PlayingGameState(GameStateManager gamestateManager)
	{
		super(gamestateManager);
	}

	public Game game;
	BitmapFont font;

	@Override
	public void onEnter()
	{
		game = new Game();
		game.init("assets/maps/map1.xml");
		font = new BitmapFont();
	}

	// TODO: reorganize!
	@Override
	public void update()
	{
		game.getSystemManager().update(Gdx.graphics.getDeltaTime());
		game.update();
	}

	@Override
	public void render()
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		game.getRenderManager().render();
		game.getRenderManager().spriteBatch.begin();

		if (game.isEditMode())
		{
			font.draw(game.getRenderManager().spriteBatch, "EDIT MODE - (F10) to calculate Navigation (F11) to save (F12) to close", 10,
					Gdx.graphics.getHeight()-10);
			font.draw(game.getRenderManager().spriteBatch, "(w) place/remove wall", 10, Gdx.graphics.getHeight()-26);
			font.draw(game.getRenderManager().spriteBatch, "(s) place/remove halfwall", 10, Gdx.graphics.getHeight()-42);
			font.draw(game.getRenderManager().spriteBatch, "(d) place/remove door", 10, Gdx.graphics.getHeight()-58);
			font.draw(game.getRenderManager().spriteBatch, "(a) place/remove halfdoor", 10, Gdx.graphics.getHeight()-74);
		}

		game.getRenderManager().spriteBatch.end();

		if (game.isGameOver)
		{
			gamestateManager.changeGameState("MAIN_MENU");
		}
	}

	@Override
	public void onLeave()
	{
	}

	@Override
	public void resize(int width, int height)
	{
		game.getRenderManager().spriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
		game.getUI().resize(width, height);
	}
}
