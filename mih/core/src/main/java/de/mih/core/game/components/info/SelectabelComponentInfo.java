package de.mih.core.game.components.info;

import java.util.Map;

import de.mih.core.engine.ecs.component.ComponentInfo;
import de.mih.core.game.components.SelectableC;

public class SelectabelComponentInfo extends ComponentInfo<SelectableC>
{

	private boolean selected;

	@Override
	public void readFields(Map<String, String> fields)
	{
		for(String key : fields.keySet())
		{
			if(key.equals("selected"))
			{
				selected = Boolean.parseBoolean(fields.get(key));
			}
		}
	}

	@Override
	public SelectableC generateComponent()
	{
		SelectableC tmp = new SelectableC();
		tmp.selected = selected;
		return tmp;
	}

}
