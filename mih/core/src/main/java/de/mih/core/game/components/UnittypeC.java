package de.mih.core.game.components;

import de.mih.core.engine.ecs.component.Component;

public class UnittypeC extends Component
{

	public String unitType;

	public UnittypeC()
	{
	}

	public UnittypeC(UnittypeC unittypeC)
	{
		this.unitType = unittypeC.unitType;
	}

	public UnittypeC(String unitType)
	{
		this.unitType = unitType;
	}
}
