package de.mih.core.game.components.info;

import java.util.Map;

import com.badlogic.gdx.graphics.g3d.Model;

import de.mih.core.engine.ecs.component.ComponentInfo;
import de.mih.core.engine.io.AdvancedAssetManager;
import de.mih.core.game.components.AttachmentC;

public class AttachmentComponentInfo implements ComponentInfo<AttachmentC>
{

	private Model model;
	private int entity;

	@Override
	public void readFields(Map<String, String> fields)
	{
		for(String key : fields.keySet())
		{
			if(key.equals("model"))
			{
				this.model = AdvancedAssetManager.getInstance().getModelByName(fields.get(key));
			}
			if(key.equals("entity"))
			{
				this.entity = Integer.parseInt(fields.get(key));
			}
		}
	}

	@Override
	public AttachmentC generateComponent()
	{
		AttachmentC tmp = new AttachmentC(entity);
		return tmp;
	}

}
