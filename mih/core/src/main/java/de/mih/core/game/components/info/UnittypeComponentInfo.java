package de.mih.core.game.components.info;

import java.util.Map;

import de.mih.core.engine.ecs.component.ComponentInfo;
import de.mih.core.game.components.UnittypeC;

public class UnittypeComponentInfo implements ComponentInfo<UnittypeC>
{

	private String unitType;

	@Override
	public void readFields(Map<String, String> fields)
	{
		for (String key : fields.keySet())
		{
			if (key.equals("unittype"))
			{
				this.unitType = fields.get(key);
			}
		}
	}

	@Override
	public UnittypeC generateComponent()
	{
		UnittypeC tmp = new UnittypeC(unitType);
		return tmp;
	}

}
