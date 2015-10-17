package de.mih.core.game.components.info;

import java.util.Map;

import de.mih.core.engine.ecs.component.ComponentInfo;
import de.mih.core.game.components.OrderableC;

public class OrderableComponentInfo extends ComponentInfo<OrderableC>
{

	@Override
	public void readFields(Map<String, String> fields)
	{
		
	}

	@Override
	public OrderableC generateComponent()
	{
		return new OrderableC();
	}

}
