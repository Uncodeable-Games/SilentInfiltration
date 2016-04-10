package de.mih.core.game.steering;

import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.VelocityC;

public abstract class Steering
{
	public abstract void apply(VelocityC velocity, PositionC position);
}
