package de.mih.core.engine.ecs;

import de.mih.core.game.GameStates.BaseGameState;
import de.mih.core.game.GameStates.IntroGameState;

public class GameStateManager {

	public static GameStateManager gamestateM;
	
	BaseGameState currgamestate;
	
	public static GameStateManager getInstance(){
		if (gamestateM == null){
			gamestateM = new GameStateManager();
		}
		return gamestateM;
	}
	
	public void init(){
		currgamestate = new IntroGameState();
		currgamestate.onstart();
	}
	
	public void changeGameState(BaseGameState gamestate){
		currgamestate.onend();
		currgamestate = gamestate;
		currgamestate.onstart();
	}
	
	public BaseGameState getCurrentGameState(){
		return currgamestate;
	}
	
}
