package de.mih.core.game.components;

import de.mih.core.engine.ecs.component.Component;
import de.mih.core.engine.tilemap.TileBorder;

public class BorderC extends Component
{

	public enum BorderType{
		Door,
		Wall,
	}

	private transient TileBorder tileBorder;

	private boolean closed = false;
	private BorderType borderType;

	public BorderC()
	{
	}

	public BorderC(BorderC borderC)
	{
		this.closed = borderC.closed;
		this.tileBorder = borderC.tileBorder;
		this.borderType = borderC.borderType;
	}

	public TileBorder getTileBorder()
	{
		return tileBorder;
	}

	public BorderType getBorderType()
	{
		return borderType;
	}

	public void setTileBorder(TileBorder tileBorder)
	{
		this.tileBorder = tileBorder;
		if (this.tileBorder.isDoor() && this.closed == true) this.tileBorder.getDoor().close();
	}
}
