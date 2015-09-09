package de.mih.core.game.components;

import java.util.Vector;

import com.badlogic.gdx.math.Vector3;

import de.mih.core.engine.ecs.Component;
import de.mih.core.game.systems.RenderSystem;

public class AttachmentC extends Component {
	int entity;
	public Visual vis;
	
	RenderSystem renderS;
	
	public AttachmentC(int e, String modelname, RenderSystem rs) {
		entity = e;
		renderS = rs;
		vis = new Visual(modelname, rs);
	}
	
	@Override
	public void onRemove() {
		renderS.allvisuals.remove(vis);
	}
}
