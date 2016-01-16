package de.mih.core.game.components.info;

import java.util.Map;

import de.mih.core.engine.ecs.component.ComponentInfo;
import de.mih.core.game.components.ColliderC;

public class ColliderComponentInfo implements ComponentInfo<ColliderC>
{

	float width,length = 0;
	
	@Override
	public void readFields(Map<String, String> fields)
	{
		for(String key : fields.keySet())
		{
			if(key.equals("rectangle"))
			{
				String[] split = fields.get(key).split(",");
				width = Float.parseFloat(split[0]);
				length = Float.parseFloat(split[1]);
			}
		}
	}

	@Override
	public ColliderC generateComponent()
	{
		//TODO: use pool!
		return new ColliderC(width,length);
	}

}
