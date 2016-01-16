package de.mih.core.game.components.info;

import java.util.Map;

import de.mih.core.engine.ecs.component.ComponentInfo;
import de.mih.core.engine.io.AdvancedAssetManager;
import de.mih.core.game.components.BorderC;

public class BorderComponentInfo implements ComponentInfo<BorderC>
{

	boolean isDoor = false;
	
	@Override
	public void readFields(Map<String, String> fields)
	{
		for(String key : fields.keySet())
		{
			if(key.equals("Door"))
			{
				this.isDoor = true;
			}
		}
	}

	@Override
	public BorderC generateComponent()
	{
		return new BorderC(this.isDoor);
	}

}
