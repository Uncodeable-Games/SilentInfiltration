package de.mih.core.engine.GameStates;

public abstract class BaseGameState {
	abstract public void onstart();
	abstract public void update();
	abstract public void render();
	abstract public void onend();
	abstract public void resize(int width, int height);
}
