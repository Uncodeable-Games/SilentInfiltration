package de.mih.core.game.components;

import de.mih.core.engine.ecs.component.Component;

/**
 * Created by Cataract on 15.04.2016.
 */
public class StatsC extends Component
{
	int maxLife;

	transient int currentLife;

	public StatsC()
	{
	}

	public StatsC(StatsC statsC)
	{
		this.maxLife = statsC.maxLife;
		this.currentLife = statsC.maxLife;
	}

	public int getMaxLife()
	{
		return maxLife;
	}

	public void setMaxLife(int maxLife)
	{
		this.maxLife = maxLife;
	}

	public int getCurrentLife()
	{
		return currentLife;
	}

	public void setCurrentLife(int currentLife)
	{
		this.currentLife = currentLife;
	}
}
