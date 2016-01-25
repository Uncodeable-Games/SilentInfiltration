package de.mih.core.game.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.mih.core.engine.gamestates.GameState;
import de.mih.core.engine.gamestates.GameStateManager;

public class IntroGameState extends GameState
{

	public IntroGameState(GameStateManager gamestateManager)
	{
		super(gamestateManager);
	}

	SpriteBatch batch;

	Texture logo;
	int x = 1;
	int y = 1;
	int time = 0;

	@Override
	public void onEnter()
	{
		logo = new Texture(Gdx.files.internal("assets/ui/logo.png"));
		batch = new SpriteBatch();
		time = x = y = 0;
	}

	@Override
	public void update()
	{
		if(time < 120)
		{
			x += 5;
			y += 5;
		}

		if (time++ > 150)
		{
			this.gamestateManager.changeGameState("MAIN_MENU");
		}
	}

	@Override
	public void render()
	{
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(logo, Gdx.graphics.getWidth() / 2f - x / 2f, Gdx.graphics.getHeight() / 2f - y / 2f, x, y);
		batch.end();

	}

	@Override
	public void onLeave()
	{
	}

	@Override
	public void resize(int width, int height)
	{

	}

}
