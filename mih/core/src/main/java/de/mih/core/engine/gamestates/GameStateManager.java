package de.mih.core.engine.gamestates;

import java.util.HashMap;

public class GameStateManager
{

	HashMap<String, GameState> gameStates;
	GameState                  start, current;

	public GameStateManager()
	{
		this.gameStates = new HashMap<>();
	}
	
	public void addGameState(String name, GameState gameState)
	{
		addGameState(name, gameState, false);
	}

	public void addGameState(String name, GameState gameState, boolean isStart)
	{
		if (isStart)
			this.start = gameState;
		this.gameStates.put(name, gameState);
	}

	public void init()
	{
		this.current = start;
		current.onEnter();
	}

	public void changeGameState(String newState)
	{
		current.onLeave();
		current = this.gameStates.get(newState);
		current.onEnter();
	}

	public void update()
	{
		current.update();
	}

	public void render()
	{
		current.render();
	}

	public GameState getCurrentGameState()
	{
		return current;
	}
}
