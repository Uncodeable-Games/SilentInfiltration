package de.mih.core.game.components;

import com.badlogic.gdx.math.Vector3;
import de.mih.core.engine.ecs.component.Component;
import de.mih.core.engine.io.AdvancedAssetManager;
import de.mih.core.engine.render.Visual;
import de.mih.core.game.Game;

public class VisualC extends Component
{

	public String modeltype;

	public VisualC()
	{
		AdvancedAssetManager.getInstance().allvisuals.add(this);
	}


	public VisualC(Visual visual)
	{
		modeltype = visual.getModeltype();
		Visual.visualc.put(this, visual);
		AdvancedAssetManager.getInstance().allvisuals.add(this);
	}

	public VisualC(String m_type)
	{
		modeltype = m_type;
		Visual.visualc.put(this, new Visual(m_type));
		AdvancedAssetManager.getInstance().allvisuals.add(this);
	}

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
		getVisual().setScale(x, y, z);
		if (Game.getCurrentGame().getEntityManager().hasComponent(entityID, ColliderC.class)
				&& !Game.getCurrentGame().getEntityManager().hasComponent(entityID, VelocityC.class))
		{
			Game.getCurrentGame().getEntityManager().getComponent(entityID, ColliderC.class).setScale(x, z);
		}
	}

	public Vector3 getScale()
	{
		return getVisual().getScale();
	}

	private void addVisual(Visual vis)
	{
		Visual.visualc.put(this, vis);
	}

	public Visual getVisual()
	{
		return Visual.visualc.get(this);
	}

	@Override
	public void Init()
	{
		super.Init();
		addVisual(new Visual(this.modeltype));
	}
}
