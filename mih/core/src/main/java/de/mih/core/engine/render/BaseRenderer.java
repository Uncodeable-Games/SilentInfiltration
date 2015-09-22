package de.mih.core.engine.render;

import de.mih.core.engine.ecs.RenderManager;

public abstract class BaseRenderer {
	
	public boolean usemodebatch = true;
	public int priority = 0;

	abstract public void render();
	
	public BaseRenderer(boolean usemodelbatch, int priority){
		this.usemodebatch = usemodelbatch;
		this.priority = priority;
		RenderManager.getInstance().register(this);
	}
	
}
