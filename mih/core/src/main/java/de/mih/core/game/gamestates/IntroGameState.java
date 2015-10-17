package de.mih.core.game.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.mih.core.engine.gamestates.BaseGameState;
import de.mih.core.engine.gamestates.GameStateManager;

public class IntroGameState extends BaseGameState{

	public IntroGameState(GameStateManager gamestateManager)
	{
		super(gamestateManager);
	}

	SpriteBatch batch;
	
	Texture logo;
	int x = 0;
	int y = 0;
	
	
	@Override
	public void onstart() {
		// TODO Auto-generated method stub
		logo = new Texture(Gdx.files.internal("assets/ui/logo.png"));
		batch = new SpriteBatch();
		
	}

	@Override
	public void update() {
		x++;
		y++;
		if (x>3){
			this.gamestateManager.changeGameState(new MainMenuGameState(this.gamestateManager));
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
	public void onend() {
	}

	@Override
	public void resize(int width, int height) {
		
	}

}
