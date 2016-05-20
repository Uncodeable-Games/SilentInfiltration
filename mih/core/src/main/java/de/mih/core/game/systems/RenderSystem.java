package de.mih.core.game.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import de.mih.core.engine.ecs.BaseSystem;
import de.mih.core.engine.ecs.SystemManager;
import de.mih.core.engine.render.BaseRenderer;
import de.mih.core.engine.render.RenderManager;
import de.mih.core.engine.render.Visual;
import de.mih.core.game.GameLogic;
import de.mih.core.game.components.AttachmentC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.VisualC;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public class RenderSystem extends BaseSystem implements BaseRenderer
{
	RenderManager renderManager;
	
	static List<RenderSystem> registeredRenderSystems = new ArrayList<RenderSystem>();

	public final Vector3 X_AXIS = new Vector3(1f, 0f, 0f);
	public final Vector3 Y_AXIS = new Vector3(0f, 1f, 0f);
	public final Vector3 Z_AXIS = new Vector3(0f, 0f, 1f);
	public final Vector3 V_NULL = new Vector3();

	public RenderSystem(SystemManager systemManager, RenderManager renderManager, GameLogic gameLogic)
	{
		this(systemManager, renderManager, gameLogic, 1);
	}

	public RenderSystem(SystemManager systemManager, RenderManager renderManager, GameLogic gameLogic, int priority)
	{
		super(systemManager, gameLogic, priority);
		this.renderManager = renderManager;
		if (!registeredRenderSystems.contains(this))
			registeredRenderSystems.add(this);
		
		renderManager.register(this, priority, true);
	}

	@Override
	public boolean matchesSystem(int entityId)
	{
		return gameLogic.getEntityManager().hasComponent(entityId, VisualC.class)
				&& gameLogic.getEntityManager().hasComponent(entityId, PositionC.class);
	}

	public void update(double dt, int entity)
	{
	}

	Vector3 prev_scale = new Vector3();

	public void render(int entity)
	{
		Visual    visual = gameLogic.getEntityManager().getComponent(entity, VisualC.class).getVisual();
		PositionC pos    = gameLogic.getEntityManager().getComponent(entity, PositionC.class);

		visual.getAnimationController().update(Gdx.graphics.getDeltaTime());

		if (visual == null || gameLogic.getEntityManager().getComponent(entity, VisualC.class).ishidden()) return;

		// TODO: Change AttachmentC
		if (gameLogic.getEntityManager().hasComponent(entity, AttachmentC.class))
		{
			for (Visual vis : gameLogic.getEntityManager().getComponent(entity, AttachmentC.class).getVisuals())
			{
				//Visual vis = gameLogic.getEntityManager().getComponent(entity, AttachmentC.class).vis;
				vis.getModel().transform.setToTranslation(pos.getX() + vis.getPos().x, pos.getY() + vis.getPos().y,
						pos.getZ() + vis.getPos().z);
				vis.getModel().transform.rotate(0f, 1f, 0f, pos.getAngle() + vis.getAngle());
				vis.getModel().transform.scale(vis.getScale().x, vis.getScale().y, vis.getScale().z);
				if (renderManager.isVisible(vis))
				{
					renderManager.getModelBatch().render(vis.getModel(), renderManager.getEnvironment());
				}
			}
		}
		//
		visual.getModel().transform.setToTranslation(pos.getX() + visual.getPos().x,
				pos.getY() + visual.getPos().y, pos.getZ() + visual.getPos().z);
		visual.getModel().transform.rotate(0f, 1f, 0f, pos.getAngle() + visual.getAngle());
		visual.getModel().transform.scale(visual.getScale().x, visual.getScale().y, visual.getScale().z);
		if (renderManager.isVisible(visual))
		{
			if (visual.getShader() != null)
			{
				renderManager.getModelBatch().render(visual.getModel(),
						renderManager.getEnvironment(), visual.getShader());
			}
			else
				renderManager.getModelBatch().render(visual.getModel(),
						renderManager.getEnvironment());
		}
	}

	@Override
	public void render()
	{
		for (int entity = 0; entity < gameLogic.getEntityManager().entityCount; entity++)
		{
			if (matchesSystem(entity))
			{
				this.render(entity);
			}
		}
	}

	@Override
	public void update(double dt)
	{
	}
}
