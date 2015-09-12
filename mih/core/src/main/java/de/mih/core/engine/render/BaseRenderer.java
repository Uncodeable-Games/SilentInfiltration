package de.mih.core.engine.render;

import de.mih.core.engine.ecs.RenderManager;

public abstract class BaseRenderer {
	
	public boolean usemodebatch = true;

	abstract public void render();
	
	public BaseRenderer(boolean usemodelbatch){
		this.usemodebatch = usemodelbatch;
		RenderManager.getInstance().register(this);
	}
	
}
