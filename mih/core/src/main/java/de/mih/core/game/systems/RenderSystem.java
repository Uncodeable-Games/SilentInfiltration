package de.mih.core.game.systems;

import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import de.mih.core.engine.ecs.BaseSystem;
import de.mih.core.engine.ecs.SystemManager;
import de.mih.core.engine.render.Visual;
import de.mih.core.game.Game;
import de.mih.core.game.components.AttachmentC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.VisualC;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public class RenderSystem extends BaseSystem
{

	static List<RenderSystem> registeredRenderSystems = new ArrayList<RenderSystem>();

	public ModelBatch   modelBatch;
	public ModelBuilder modelBuilder;

	public ModelLoader modelLoader;
	public Environment environment;

	public final Vector3 X_AXIS = new Vector3(1f, 0f, 0f);
	public final Vector3 Y_AXIS = new Vector3(0f, 1f, 0f);
	public final Vector3 Z_AXIS = new Vector3(0f, 0f, 1f);
	public final Vector3 V_NULL = new Vector3();

	public RenderSystem(SystemManager systemManager, Game game)
	{
		super(systemManager, game, 1);
	}

	public RenderSystem(SystemManager systemManager, Game game, int priority)
	{
		super(systemManager, game, priority);

		if (!registeredRenderSystems.contains(this))
			registeredRenderSystems.add(this);

		//
	}

	@Override
	public boolean matchesSystem(int entityId)
	{
		return game.getEntityManager().hasComponent(entityId, VisualC.class)
				&& game.getEntityManager().hasComponent(entityId, PositionC.class);
	}

	public void update(double dt, int entity)
	{
	}

	Vector3 prev_scale = new Vector3();

	@Override
	public void render(int entity)
	{
		Visual    visual = game.getEntityManager().getComponent(entity, VisualC.class).getVisual();
		PositionC pos    = game.getEntityManager().getComponent(entity, PositionC.class);

		if (visual == null || game.getEntityManager().getComponent(entity, VisualC.class).ishidden()) return;

		// TODO: Change AttachmentC
		if (game.getEntityManager().hasComponent(entity, AttachmentC.class))
		{
			for (Visual vis : game.getEntityManager().getComponent(entity, AttachmentC.class).getVisuals())
			{
				//Visual vis = game.getEntityManager().getComponent(entity, AttachmentC.class).vis;
				vis.getModel().transform.setToTranslation(pos.getX() + vis.getPos().x, pos.getY() + vis.getPos().y,
						pos.getZ() + vis.getPos().z);
				vis.getModel().transform.rotate(0f, 1f, 0f, pos.getAngle() + vis.getAngle());
				vis.getModel().transform.scale(vis.getScale().x, vis.getScale().y, vis.getScale().z);
				if (game.getRenderManager().isVisible(vis))
				{

					game.getRenderManager().getModelBatch().render(vis.getModel(), game.getRenderManager().getEnvironment());
				}
			}
		}
		//
		visual.getModel().transform.setToTranslation(pos.getX() + visual.getPos().x,
				pos.getY() + visual.getPos().y, pos.getZ() + visual.getPos().z);
		visual.getModel().transform.rotate(0f, 1f, 0f, pos.getAngle() + visual.getAngle());
		visual.getModel().transform.scale(visual.getScale().x, visual.getScale().y, visual.getScale().z);
		if (game.getRenderManager().isVisible(visual))
		{
			if (visual.getShader() != null)
			{
				game.getRenderManager().getModelBatch().render(visual.getModel(),
						game.getRenderManager().getEnvironment(), visual.getShader());
			}
			else
				game.getRenderManager().getModelBatch().render(visual.getModel(),
						game.getRenderManager().getEnvironment());
		}
	}

	@Override
	public void render()
	{

	}

	@Override
	public void update(double dt)
	{
	}
}
