package de.mih.core.engine.gamestates;

import org.omg.CORBA.Current;

import de.mih.core.game.Game;
import de.mih.core.game.gamestates.IntroGameState;
import de.mih.core.game.gamestates.PlayingGameState;

public class GameStateManager {
	
	BaseGameState currentState;

	
	public void init(){
		currentState = new IntroGameState(this);
		currentState.onstart();
	}
	
	public void changeGameState(BaseGameState gamestate){
		currentState.onend();
		currentState = gamestate;
		currentState.onstart();
	}
	
	public BaseGameState getCurrentGameState(){
		return currentState;
	}
	
	
}
