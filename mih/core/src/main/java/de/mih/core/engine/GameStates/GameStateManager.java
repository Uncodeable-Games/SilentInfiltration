package de.mih.core.engine.GameStates;

import org.omg.CORBA.Current;

import de.mih.core.game.Game;

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
	
	public Game getCurrentGame(){
		if (currgamestate instanceof PlayingGameState){
			return ((PlayingGameState) currgamestate).game;
		}
		return null;
	}
	
}
