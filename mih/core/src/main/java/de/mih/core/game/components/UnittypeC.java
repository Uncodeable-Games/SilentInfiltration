package de.mih.core.game.components;

import de.mih.core.engine.ecs.Component;

public class UnittypeC extends Component {
	public String unitType;

	public UnittypeC()
	{
		
	}
	
	public UnittypeC(String unitType)
	{
		this.unitType = unitType;
	}
	@Override
	public void setField(String fieldName, String fieldValue) {
		switch(fieldName)
		{
			case "unittype":
				this.unitType = fieldValue;
				break;
		}
		
	}

	@Override
	public Component cpy() {
		return new UnittypeC(unitType);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
}
