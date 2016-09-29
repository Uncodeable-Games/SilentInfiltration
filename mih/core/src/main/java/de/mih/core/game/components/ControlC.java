package de.mih.core.game.components;

import de.mih.core.engine.ecs.component.Component;
import de.mih.core.levedit.Entities.Abstract.Editable;

public class ControlC extends Component
{

	@Editable(value="PlayerId",bool = true)
	int player = -1;

	@Editable("Mouse")
	public boolean withmouse = false;
	@Editable("Wasd")
	public boolean withwasd  = false;
	@Editable("Keys")
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
