package de.mih.core.engine.gamestates;

import java.util.ArrayList;
import java.util.List;

import de.mih.core.game.gamestates.IntroGameState;

public class GameStateManager
{

	List<GameState> gameStates;
	GameState start, current;

	public GameStateManager()
	{
		//this.start = current = start;
		this.gameStates = new ArrayList<>();
	}

	public void addGameState(GameState gameState, boolean isStart)
	{
		if(isStart)
			this.start = gameState;
		this.gameStates.add(gameState);
	}
	

	public void init()
	{
		this.current = start;
		current.onEnter();
	}

	public void changeGameState()
	{// GameState gamestate){
		current.onLeave();
		current = current.next;
		current.onEnter();
	}

	public GameState getCurrentGameState()
	{
		return current;
	}

}
