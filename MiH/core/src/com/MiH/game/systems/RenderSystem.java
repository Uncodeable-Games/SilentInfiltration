package com.MiH.game.systems;

import java.util.ArrayList;
import java.util.List;

import com.MiH.engine.ecs.BaseSystem;
import com.MiH.engine.ecs.EntityManager;
import com.MiH.engine.ecs.EventManager;
import com.MiH.engine.ecs.SystemManager;
import com.MiH.engine.exceptions.ComponentNotFoundEx;
import com.MiH.game.components.PositionC;
import com.MiH.game.components.Visual;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class RenderSystem extends BaseSystem {

	static List<RenderSystem> registeredRenderSystems = new ArrayList<RenderSystem>();

	public static PerspectiveCamera camera;
	public static ModelBatch modelBatch;
	public static ModelBuilder modelBuilder;
	public static ModelLoader modelLoader = new ObjLoader();
	public static Environment environment;
	public static ArrayList<Model> allmodeltypes = new ArrayList<Model>();
	public static ArrayList<Visual> allvisuals = new ArrayList<Visual>();

	public static Model robocop;
	public static Model box;
	public static Model floor;

	public RenderSystem(SystemManager systemManager, EntityManager entityManager, EventManager eventManager,
			PerspectiveCamera cam) {
		this(systemManager, entityManager, eventManager, 1, cam);
	}

	public RenderSystem(SystemManager systemManager, EntityManager entityManager, EventManager eventManager,
			int priority, PerspectiveCamera cam) {
		super(systemManager, entityManager, eventManager, priority);

		if (!registeredRenderSystems.contains(this))
			registeredRenderSystems.add(this);

		camera = cam;
		camera.position.set(2f, 5f, 3f);
		camera.lookAt(0f, 0f, 0f);
		camera.near = 0.1f;
		camera.far = 300f;
		modelBatch = new ModelBatch();
		modelBuilder = new ModelBuilder();
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f, 0.8f, 1f));
		
		// TODO: Outsource Modelinformations		
		robocop = loadModel("robocop.obj");
		allmodeltypes.add(robocop);
		
		box = modelBuilder.createBox(1f, 1f, 1f, new Material(ColorAttribute.createDiffuse(Color.BLUE)),
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
		allmodeltypes.add(box);
		
		floor = modelBuilder.createBox(1, .01f, 1, new Material(ColorAttribute.createDiffuse(Color.GREEN)),
                VertexAttributes.Usage.Position|VertexAttributes.Usage.Normal);
		allmodeltypes.add(floor);
		
	}

	@Override
	public boolean matchesSystem(int entityId) {
		return entityManager.hasComponent(entityId, Visual.class)
				&& entityManager.hasComponent(entityId, PositionC.class);
	}

	public void update(double dt, int entity) throws ComponentNotFoundEx {
	}

	@Override
	public void render(int entity) throws ComponentNotFoundEx {
		Visual visual = entityManager.getComponent(entity, Visual.class);
		PositionC pos = entityManager.getComponent(entity, PositionC.class);

		if (isVisible(visual)) {
			visual.model.transform.setToTranslation(pos.position.x + visual.pos.x, pos.position.y + visual.pos.y,
					pos.position.z + visual.pos.z);
			visual.model.transform.rotate(0f, 1f, 0f, pos.angle + visual.angle);
			visual.model.transform.scale(visual.scale.x, visual.scale.y, visual.scale.z);
		}
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		camera.update();
		modelBatch.begin(camera);
		for(Visual v : allvisuals){
			if (isVisible(v)){
				modelBatch.render(v.model);
			}
		}
		
		modelBatch.end();
	}

	@Override
	public void update(double dt) {
	}

	Vector3 getCamTarget(float h) {
		return camera.position.cpy().add(camera.direction.cpy().scl((h - camera.position.y) / (camera.direction.y)));
	}
	
	private Vector3 pos = new Vector3();
	
	boolean isVisible(Visual vis){
		vis.model.transform.getTranslation(pos);
		pos.add(vis.center);
		return camera.frustum.sphereInFrustum(pos, vis.radius);
	}

	Model loadModel(String s){
		return modelLoader.loadModel(Gdx.files.internal(s));
	}
}
