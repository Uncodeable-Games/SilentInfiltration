package de.mih.core.game.components.info;

import java.util.Map;

import com.badlogic.gdx.math.Rectangle;

import de.mih.core.engine.ecs.component.ComponentInfo;
import de.mih.core.game.components.ColliderC;

public class ColliderComponentInfo extends ComponentInfo<ColliderC>
{

	Rectangle collider;
	
	@Override
	public void readFields(Map<String, String> fields)
	{
		for(String key : fields.keySet())
		{
			if(key.equals("rectangle"))
			{
				String[] split = fields.get(key).split(",");
				float width = Float.parseFloat(split[0]);
				float height = Float.parseFloat(split[1]);
				Rectangle rect = new Rectangle();
				rect.width = width;
				rect.height = height;
				this.collider = rect;
			}
		}
	}

	@Override
	public ColliderC generateComponent()
	{
		//TODO: use pool!
		return new ColliderC(collider);
	}

}
