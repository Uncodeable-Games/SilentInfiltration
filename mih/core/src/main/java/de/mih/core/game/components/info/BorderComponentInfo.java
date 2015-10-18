package de.mih.core.game.components.info;

import java.util.Map;

import de.mih.core.engine.ecs.component.ComponentInfo;
import de.mih.core.game.components.BorderC;

public class BorderComponentInfo implements ComponentInfo<BorderC>
{

	@Override
	public void readFields(Map<String, String> fields)
	{
		
	}

	@Override
	public BorderC generateComponent()
	{
		return new BorderC();
	}

}
