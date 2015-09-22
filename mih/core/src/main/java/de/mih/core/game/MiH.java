package de.mih.core.game;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;

import de.mih.core.engine.GameStates.GameStateManager;
import de.mih.core.engine.ecs.RenderManager;

public class MiH extends ApplicationAdapter {
	

	public void create() {
		GameStateManager.getInstance().init();
	}

	public void render() {
		GameStateManager.getInstance().getCurrentGameState().update();
		GameStateManager.getInstance().getCurrentGameState().render();
	}
	
	public void resize(int width, int height){
		GameStateManager.getInstance().getCurrentGameState().resize(width,height);
	}
}
