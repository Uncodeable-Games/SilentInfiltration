package de.mih.core.game.components;

import java.util.Vector;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;

import de.mih.core.engine.ecs.Component;
import de.mih.core.engine.io.AdvancedAssetManager;
import de.mih.core.engine.render.Visual;
import de.mih.core.game.systems.RenderSystem;

public class AttachmentC extends Component {
	
	public static final String name = "attachment";
	
	int entity;
	public Visual vis;
	
	
	public AttachmentC(int e, Model model) {
		entity = e;
		vis = new Visual(model);
	}
	
	@Override
	public void onRemove() {
		AdvancedAssetManager.getInstance().allvisuals.remove(vis);
	}

	@Override
	public void setField(String fieldName, String fieldValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Component cpy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
}
