package de.mih.core.engine.render;

public abstract class BaseRenderer {
	
	public boolean usemodebatch = true;
	public int priority = 0;

	protected RenderManager renderManager;
	abstract public void render();
	
	public BaseRenderer(RenderManager renderManager, boolean usemodelbatch, int priority){
		this.usemodebatch = usemodelbatch;
		this.priority = priority;
		this.renderManager = renderManager;
		this.renderManager.register(this);
	}
	
}
