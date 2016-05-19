package de.mih.core.game.components;

import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;

import de.mih.core.engine.ecs.component.Component;
import de.mih.core.engine.io.AdvancedAssetManager;
import de.mih.core.engine.render.Visual;
import de.mih.core.game.systems.RenderSystem;

public class AttachmentC extends Component {
		
	int entity;
	public int id;
	public HashMap<Integer, Visual> visuals;
	
	public AttachmentC(int e) {
		entity = e;
		visuals = new HashMap<>();
	}
	
	public void addAttachment(int id, Model model)
	{
		visuals.put(id, new Visual(model));
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
	
}
