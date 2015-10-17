package de.mih.core.game.components.info;

import java.util.Map;

import de.mih.core.engine.ecs.component.ComponentInfo;
import de.mih.core.game.components.Control;

public class ControlComponentInfo extends ComponentInfo<Control>
{

	@Override
	public void readFields(Map<String, String> fields)
	{
		
	}

	@Override
	public Control generateComponent()
	{
		return new Control();
	}

}
