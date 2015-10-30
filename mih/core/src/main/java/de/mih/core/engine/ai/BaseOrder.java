package de.mih.core.engine.ai;

public abstract class BaseOrder
{
	abstract public void handle(int entity);
	abstract public boolean isFinished();
	abstract public void finish();
}
