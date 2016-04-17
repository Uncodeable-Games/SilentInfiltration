package de.mih.core.game.components;

import com.badlogic.gdx.math.Vector3;
import de.mih.core.engine.ecs.component.Component;
import de.mih.core.engine.render.Visual;
import de.mih.core.game.Game;

public class VisualC extends Component
{

	private String modeltype;
	private boolean ishidden = false;
	private float scale = 1f;

	private transient Visual visual;

	public VisualC()
	{
	}

	public VisualC(VisualC vis)
	{
		this.modeltype = vis.modeltype;
		this.ishidden = vis.ishidden;
		if (vis.visual == null)
		{
			this.visual = new Visual(this.modeltype);
		}
		else
		{
			this.visual = vis.visual;
		}
		setScale(vis.scale);
	}

	public VisualC(Visual visual)
	{
		modeltype = visual.getModeltype();
		this.visual = visual;
	}

	public VisualC(String m_type)
	{
		modeltype = m_type;
		this.visual = new Visual(this.modeltype);
	}

	public void show()
	{
		ishidden = false;
	}

	public void hide()
	{
		ishidden = true;
	}

	public boolean ishidden()
	{
		return this.ishidden;
	}

	public void setScale(float x, float y, float z)
	{
		getVisual().setScale(x, y, z);
		if (Game.getCurrentGame().getEntityManager().hasComponent(entityID, ColliderC.class)
				&& !Game.getCurrentGame().getEntityManager().hasComponent(entityID, VelocityC.class))
		{
			Game.getCurrentGame().getEntityManager().getComponent(entityID, ColliderC.class).setScale(x, z);
		}
	}

	public void setScale(Vector3 scale){
		getVisual().setScale(scale.x, scale.y, scale.z);
		if (Game.getCurrentGame().getEntityManager().hasComponent(entityID, ColliderC.class)
				&& !Game.getCurrentGame().getEntityManager().hasComponent(entityID, VelocityC.class))
		{
			Game.getCurrentGame().getEntityManager().getComponent(entityID, ColliderC.class).setScale(scale.x, scale.z);
		}
	}

	public void setScale(float scale){
		setScale(getScale().x*scale,getScale().y*scale,getScale().z*scale);
	}

	public Vector3 getScale()
	{
		return getVisual().getScale();
	}

	private void addVisual(Visual vis)
	{
		visual = vis;
	}

	public Visual getVisual()
	{
		return visual;
	}
}
