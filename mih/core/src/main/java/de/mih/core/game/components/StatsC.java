package de.mih.core.game.components;

import de.mih.core.engine.ecs.component.Component;
import de.mih.core.levedit.Entities.Abstract.Editable;

/**
 * Created by Cataract on 15.04.2016.
 */
public class StatsC extends Component
{
	@Editable("Maximum Life")
	int maxLife;

	transient int currentLife;
	transient boolean isAlive = true;

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

	public boolean isAlive()
	{
		return isAlive;
	}

	public void setAlive(boolean isAlive)
	{
		this.isAlive = isAlive;
	}
	
	
}
