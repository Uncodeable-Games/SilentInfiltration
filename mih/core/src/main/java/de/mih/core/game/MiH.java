package de.mih.core.game;

import com.badlogic.gdx.ApplicationAdapter;

import de.mih.core.engine.GameStates.GameStateManager;

public class MiH extends ApplicationAdapter {
	

	public void create() {
		GameStateManager.getInstance().init();
	}

	public void render() {
		GameStateManager.getInstance().getCurrentGameState().update();
		GameStateManager.getInstance().getCurrentGameState().render();
	}
}
