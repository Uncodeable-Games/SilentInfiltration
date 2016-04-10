package de.mih.core.game.components;

import de.mih.core.engine.ecs.component.Component;

public class BorderC extends Component
{
	public boolean isDoor = false;
	
	// Only important if isDoor is true
	public boolean checked  = false;
	public boolean isclosed = false;
}
