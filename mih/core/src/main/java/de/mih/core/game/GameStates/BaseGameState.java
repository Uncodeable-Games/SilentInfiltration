package de.mih.core.game.GameStates;

public abstract class BaseGameState {
	abstract public void onstart();
	abstract public void update();
	abstract public void render();
	abstract public void onend();
}
