package de.mih.core.game.components;

import java.util.Vector;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;

import de.mih.core.engine.ecs.component.Component;
import de.mih.core.engine.io.AdvancedAssetManager;
import de.mih.core.engine.render.Visual;
import de.mih.core.game.systems.RenderSystem;

public class AttachmentC extends Component {
		
	int entity;
	public Visual vis;
	
	
	public AttachmentC(int e, Model model) {
		entity = e;
		vis = new Visual(model);
	}
	
}
