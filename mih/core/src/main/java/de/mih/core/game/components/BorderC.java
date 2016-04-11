package de.mih.core.game.components;

import de.mih.core.engine.ecs.component.Component;

public class BorderC extends Component
{
	public boolean isDoor = false;
	
	// Only important if isDoor is true
	public boolean checked  = false;
	public boolean isclosed = false;

	public BorderC()
	{
	}

	public BorderC(BorderC borderC)
	{
		this.isDoor = borderC.isDoor;
		this.checked = borderC.checked;
		this.isclosed = borderC.isclosed;
	}
}
