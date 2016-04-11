package de.mih.core.game.components;

import de.mih.core.engine.ecs.component.Component;

public class ControlC extends Component
{

	int player = -1;
	
	public boolean withmouse = false;
	public boolean withwasd  = false;
	public boolean withkeys  = false;

	public ControlC()
	{
	}

	public ControlC(ControlC controlC)
	{
		this.player = controlC.player;
		this.withmouse = controlC.withmouse;
		this.withwasd = controlC.withwasd;
		this.withkeys = controlC.withkeys;
	}
}
