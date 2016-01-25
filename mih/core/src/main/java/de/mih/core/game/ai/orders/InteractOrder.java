package de.mih.core.game.ai.orders;

import de.mih.core.engine.ai.BaseOrder;
import de.mih.core.game.player.Interaction;

public class InteractOrder extends BaseOrder
{

	int target;
	Interaction action;
	boolean isFinished = false;

	@Override
	public void handle(int entity)
	{

	}

	@Override
	public boolean isFinished()
	{
		return isFinished;
	}

	@Override
	public void finish()
	{
		isFinished = true;
	}

}
