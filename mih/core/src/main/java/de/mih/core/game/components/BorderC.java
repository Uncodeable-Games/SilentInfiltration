package de.mih.core.game.components;

import de.mih.core.engine.ecs.component.Component;
import de.mih.core.engine.tilemap.TileBorder;
import de.mih.core.levedit.Entities.Abstract.Editable;

public class BorderC extends Component
{

	public enum BorderType{
		Door,
		Wall,
	}

	private transient TileBorder tileBorder;

	@Editable(value = "Is closed",bool = true)
	private boolean closed = false;
	@Editable("Type of Border")
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
