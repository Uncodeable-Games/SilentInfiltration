package de.mih.core.game.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.mih.core.engine.gamestates.GameState;
import de.mih.core.engine.gamestates.GameStateManager;

public class IntroGameState extends GameState{

	public IntroGameState(GameStateManager gamestateManager)
	{
		super(gamestateManager);
	}

	SpriteBatch batch;
	
	Texture logo;
	int x = 0;
	int y = 0;
	
	
	@Override
	public void onEnter() {
		// TODO Auto-generated method stub
		logo = new Texture(Gdx.files.internal("assets/ui/logo.png"));
		batch = new SpriteBatch();
		
	}

	@Override
	public void update() {
		x++;
		y++;
		if (x>3){
			this.gamestateManager.changeGameState();
		}
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(logo,Gdx.graphics.getWidth()/2f-x/2f,Gdx.graphics.getHeight()/2f-y/2f,x,y);
		batch.end();
		
	}

	@Override
	public void onLeave() {
	}

	@Override
	public void resize(int width, int height) {
		
	}

}
