package de.mih.core.game.components;

import de.mih.core.engine.ecs.component.Component;

public class BorderC extends Component {

	public static final String name = "border";
	
	@Override
	public void setField(String fieldName, String fieldValue) {
	}

	@Override
	public Component cpy() {
		return new BorderC();
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
}
