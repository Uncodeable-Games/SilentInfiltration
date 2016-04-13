package de.mih.core.game.components;

import de.mih.core.engine.ecs.component.Component;
import de.mih.core.engine.tilemap.TileBorder;

public class BorderC extends Component
{
	private transient TileBorder tileBorder;
	
	// Only important if isDoor is true
	private boolean closed = false;

	public BorderC()
	{
	}

	public BorderC(BorderC borderC)
	{
		this.closed = borderC.closed;
		this.tileBorder = borderC.tileBorder;
	}

	public TileBorder getTileBorder()
	{
		return tileBorder;
	}

	public void setTileBorder(TileBorder tileBorder)
	{
		this.tileBorder = tileBorder;
		if (this.tileBorder.isDoor() && this.closed == true) this.tileBorder.getDoor().close();
	}
}
