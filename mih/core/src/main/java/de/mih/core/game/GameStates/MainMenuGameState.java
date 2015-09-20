package de.mih.core.game.GameStates;

import de.mih.core.engine.ecs.GameStateManager;

public class MainMenuGameState extends BaseGameState {

	@Override
	public void onstart() {
		GameStateManager.getInstance().changeGameState(new PlayingGameState());
	}

	@Override
	public void update() {
	}

	@Override
	public void render() {
	}

	@Override
	public void onend() {
	}

}
