package de.mih.core.game.components;

import de.mih.core.engine.ecs.component.Component;
import de.mih.core.engine.render.Visual;

import java.util.Collection;
import java.util.HashMap;

public class AttachmentC extends Component
{
	int entity = -1;
	public int id = -1;
	public HashMap<Integer, Visual> visuals;

	public AttachmentC()
	{
	}

	public AttachmentC(AttachmentC attachmentC)
	{
		this(attachmentC.entity);
	}

	public AttachmentC(int e)
	{
		entity = e;
		visuals = new HashMap<>();
	}

	public boolean containsAttachment(int id)
	{
		return visuals.containsKey(id);
	}
	
	public void removeAttachment(int id)
	{
		visuals.remove(id);
	}
	
	public Collection<Visual> getVisuals()
	{
		return visuals.values();
	}

	public void addAttachment(int id, String model)
	{
		visuals.put(id, new Visual(model));
	}
}
