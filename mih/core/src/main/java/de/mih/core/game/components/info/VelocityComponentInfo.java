package de.mih.core.game.components.info;

import java.util.Map;

import de.mih.core.engine.ecs.component.ComponentInfo;
import de.mih.core.game.components.VelocityC;

public class VelocityComponentInfo implements ComponentInfo<VelocityC>
{

	private float drag;
	private float maxspeed;

	@Override
	public void readFields(Map<String, String> fields)
	{
		for(String key : fields.keySet())
		{
			if(key.equals("drag"))
			{
				drag = Float.parseFloat(fields.get(key));
			}
			if(key.equals("maxspeed"))
			{
				maxspeed = Float.parseFloat(fields.get(key));
			}
		}
	}

	@Override
	public VelocityC generateComponent()
	{
		VelocityC tmp = new VelocityC();
		tmp.drag = drag;
		tmp.maxspeed = maxspeed;
		return tmp;
	}

}
