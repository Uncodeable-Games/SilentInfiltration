package de.mih.core.game.components.info;

import java.util.Map;

import de.mih.core.engine.ecs.component.ComponentInfo;
import de.mih.core.engine.io.AdvancedAssetManager;
import de.mih.core.engine.render.Visual;
import de.mih.core.game.components.VisualC;

public class VisualComponentInfo implements ComponentInfo<VisualC>
{

	
	private Visual visual;

	@Override
	public void readFields(Map<String, String> fields)
	{
		for(String key : fields.keySet())
		{
			if(key.equals("model"))
			{
				this.visual = new Visual(AdvancedAssetManager.getInstance().getModelByName(fields.get(key)));
			}
		}
	}

	@Override
	public VisualC generateComponent()
	{
		return new VisualC(visual);
	}

}
