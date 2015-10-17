package de.mih.core.game.systems;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.mih.core.engine.ecs.BaseSystem;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.ecs.EventManager;
import de.mih.core.engine.ecs.RenderManager;
import de.mih.core.engine.ecs.SystemManager;
import de.mih.core.engine.render.Visual;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.SelectableC;
import de.mih.core.game.components.VisualC;
import de.mih.core.game.events.orderevents.SelectEntity_Event;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

import de.mih.core.engine.ecs.BaseSystem;
import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.ecs.SystemManager;
import de.mih.core.engine.ecs.events.BaseEvent;
import de.mih.core.game.Game;
import de.mih.core.game.components.AttachmentC;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.player.Player;

@SuppressWarnings("rawtypes")
public class RenderSystem extends BaseSystem {

	static List<RenderSystem> registeredRenderSystems = new ArrayList<RenderSystem>();

	public ModelBatch modelBatch;
	public ModelBuilder modelBuilder;

	public ModelLoader modelLoader;
	public Environment environment;

	public final Vector3 X_AXIS = new Vector3(1f, 0f, 0f);
	public final Vector3 Y_AXIS = new Vector3(0f, 1f, 0f);
	public final Vector3 Z_AXIS = new Vector3(0f, 0f, 1f);
	public final Vector3 V_NULL = new Vector3();

	public RenderSystem(SystemManager systemManager, Game game) {
		super(systemManager, game, 1);

	}

	public RenderSystem(SystemManager systemManager, Game game, int priority) {
		super(systemManager, game, priority);

		if (!registeredRenderSystems.contains(this))
			registeredRenderSystems.add(this);

		//
	}

	@Override
	public boolean matchesSystem(int entityId) {
		return game.getEntityManager().hasComponent(entityId, VisualC.class)
				&& game.getEntityManager().hasComponent(entityId, PositionC.class);
	}

	public void update(double dt, int entity) {
	}

	Vector3 prev_scale = new Vector3();

	@Override
	public void render(int entity) {
		VisualC visual = game.getEntityManager().getComponent(entity, VisualC.class);
		PositionC pos = game.getEntityManager().getComponent(entity, PositionC.class);

		// TODO: Change AttachmentC
		if (game.getEntityManager().hasComponent(entity, AttachmentC.class)) {
			Visual vis = game.getEntityManager().getComponent(entity, AttachmentC.class).vis;
			vis.model.transform.setToTranslation(pos.getX() + vis.pos.x, pos.getY() + vis.pos.y,
					pos.getZ() + vis.pos.z);
			vis.model.transform.rotate(0f, 1f, 0f, pos.getAngle() + vis.angle);
			vis.model.transform.scale(vis.getScale().x, vis.getScale().y, vis.getScale().z);
			if (game.getRenderManager().isVisible(vis)) {
				game.getRenderManager().getModelBatch().render(vis.model,
						game.getRenderManager().getEnvironment());
			}
		}
		//

		visual.visual.model.transform.setToTranslation(pos.getX() + visual.visual.pos.x,
				pos.getY() + visual.visual.pos.y, pos.getZ() + visual.visual.pos.z);
		visual.visual.model.transform.rotate(0f, 1f, 0f, pos.getAngle() + visual.visual.angle);
		visual.visual.model.transform.scale(visual.getScale().x, visual.getScale().y, visual.getScale().z);
		if (game.getRenderManager().isVisible(visual.visual)) {
			game.getRenderManager().getModelBatch().render(visual.visual.model,
					game.getRenderManager().getEnvironment());
		}
	}

	@Override
	public void render() {

	}

	@Override
	public void update(double dt) {
	}

	@Override
	public void onEventRecieve(BaseEvent event) {
		// TODO Auto-generated method stub

	}

}
