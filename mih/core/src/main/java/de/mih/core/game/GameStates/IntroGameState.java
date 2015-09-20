package de.mih.core.game.GameStates;

import de.mih.core.engine.ecs.GameStateManager;

public class IntroGameState extends BaseGameState{

	@Override
	public void onstart() {
		// TODO Auto-generated method stub
		GameStateManager.getInstance().changeGameState(new MainMenuGameState());
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onend() {
		System.out.println("StateGame end");
	}

}
