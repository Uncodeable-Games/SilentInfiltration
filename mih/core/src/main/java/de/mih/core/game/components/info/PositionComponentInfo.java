package de.mih.core.game.components.info;

import java.util.Map;

import de.mih.core.engine.ecs.component.ComponentInfo;
import de.mih.core.game.components.PositionC;

public class PositionComponentInfo implements ComponentInfo<PositionC>
{

	private int angle;

	@Override
	public void readFields(Map<String, String> fields)
	{
		for(String key : fields.keySet())
		{
			if(key.equals("angle"))
			{
				angle = Integer.parseInt(fields.get(key));
			}
		}
		
	}

	@Override
	public PositionC generateComponent()
	{
		PositionC tmp = new PositionC();
		tmp.setAngle(angle);
		return tmp;
	}

}
