package de.mih.core.game.components;

import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.ecs.component.Component;
import de.mih.core.engine.io.AdvancedAssetManager;
import de.mih.core.engine.render.Visual;
import de.mih.core.game.Game;
import de.mih.core.game.systems.RenderSystem;

import org.w3c.dom.Node;

import com.badlogic.gdx.math.Vector3;

public class RenderC extends Component
{

	public Visual visual;

	public RenderC()
	{

	}

	public RenderC(Visual visual)
	{
		this.visual = visual;
		AdvancedAssetManager.getInstance().allvisuals.add(this);
	}

//	public RenderC(String m_type)
//	{
//		this.visual = new Visual(AdvancedAssetManager.getInstance().getModelByName(m_type));
//
//		AdvancedAssetManager.getInstance().allvisuals.add(this);
//	}

	public void onRemove()
	{
		hide();
	}

	public void show()
	{
		if (ishidden())
			AdvancedAssetManager.getInstance().allvisuals.add(this);
	}

	public void hide()
	{
		if (!ishidden())
			AdvancedAssetManager.getInstance().allvisuals.remove(this);
	}

	public boolean ishidden()
	{
		return !AdvancedAssetManager.getInstance().allvisuals.contains(this);
	}

	public void setScale(float x, float y, float z)
	{
		visual.setScale(x, y, z);
		if (Game.getCurrentGame().getEntityManager().hasComponent(entityID, ColliderC.class)
				&& !Game.getCurrentGame().getEntityManager().hasComponent(entityID, VelocityC.class))
		{
			Game.getCurrentGame().getEntityManager().getComponent(entityID, ColliderC.class).setScale(x, z);
		}
	}

	public Vector3 getScale()
	{
		return visual.getScale();
	}

}
