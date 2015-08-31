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
import com.badlogic.gdx.Input;
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
import com.badlogic.gdx.math.collision.Ray;
import com.sun.javafx.geom.PickRay;

public class RenderSystem extends BaseSystem{

	static List<RenderSystem> registeredRenderSystems = new ArrayList<RenderSystem>();
	
	public PerspectiveCamera camera;
	public ModelBatch modelBatch;
	public ModelBuilder modelBuilder;
	public ModelLoader modelLoader;
	public Environment environment;
	public ArrayList<Model> allmodeltypes = new ArrayList<Model>();
	public ArrayList<Visual> allvisuals = new ArrayList<Visual>();
	
	public Model robocop;
	public Model box;
	public Model redbox;
	public Model floor;
	
	public final Vector3 X_AXIS = new Vector3(1f, 0f, 0f);
	public final Vector3 Y_AXIS = new Vector3(0f, 1f, 0f);
	public final Vector3 Z_AXIS = new Vector3(0f, 0f, 1f);
	public final Vector3 V_NULL = new Vector3();
	
	public RenderSystem(SystemManager systemManager,
			EntityManager entityManager, EventManager eventManager, PerspectiveCamera cam) {
		this(systemManager,entityManager,eventManager,1,cam);
	}
	
	public RenderSystem(SystemManager systemManager,
			EntityManager entityManager, EventManager eventManager,int priority, PerspectiveCamera cam) {
		super(systemManager, entityManager, eventManager, priority);
		
		if (!registeredRenderSystems.contains(this))
			registeredRenderSystems.add(this);
		
		camera = cam;
		camera.position.set(2f,5f,3f);
		camera.lookAt(0f,0f,0f);
		camera.near = 0.1f;
		camera.far = 300f;
		modelBatch = new ModelBatch();
		modelBuilder = new ModelBuilder();
		modelLoader = new ObjLoader();
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight,0.8f,0.8f,0.8f,1f));
		
		// TODO: Outsource Modelinformations
		robocop = loadModel("robocop.obj");
		allmodeltypes.add(robocop);
		
		box = modelBuilder.createBox(1f, 1f, 1f, new Material(ColorAttribute.createDiffuse(Color.BLUE)),
                VertexAttributes.Usage.Position|VertexAttributes.Usage.Normal);
		allmodeltypes.add(box);
		
		redbox = modelBuilder.createBox(1f, 1f, 1f, new Material(ColorAttribute.createDiffuse(Color.RED)),
                VertexAttributes.Usage.Position|VertexAttributes.Usage.Normal);
		allmodeltypes.add(redbox);
		
		floor = modelBuilder.createBox(1f, .01f, 1f, new Material(ColorAttribute.createDiffuse(Color.GREEN)),
                VertexAttributes.Usage.Position|VertexAttributes.Usage.Normal);
		allmodeltypes.add(floor);
		//
	}


	@Override
	public boolean matchesSystem(int entityId) {
		return entityManager.hasComponent(entityId, Visual.class)
				&& entityManager.hasComponent(entityId, PositionC.class);
	}

	public void update(double dt, int entity) throws ComponentNotFoundEx {
	}

	Vector3 prev_scale = new Vector3();
	@Override
	public void render(int entity) throws ComponentNotFoundEx {
		Visual visual = entityManager.getComponent(entity, Visual.class);
		PositionC pos = entityManager.getComponent(entity, PositionC.class);
		
		visual.model.transform.setToTranslation(pos.position.x+visual.pos.x, pos.position.y+visual.pos.y, pos.position.z+visual.pos.z);
		visual.model.transform.rotate(0f, 1f, 0f, pos.angle+visual.angle);
		visual.model.transform.scale(visual.getScale().x, visual.getScale().y, visual.getScale().z);
	}

	@Override
	public void render(){	
		Gdx.gl.glClearColor(0, 0, 0, 1);
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT|GL20.GL_DEPTH_BUFFER_BIT);
		
	    camera.update();
		modelBatch.begin(camera);
		for (Visual v : allvisuals){
			if (isVisible(v)){
				modelBatch.render(v.model,environment);
			}
		}
		modelBatch.end();	
	}
	
	@Override
	public void update(double dt) {
	}
	
	public Vector3 getCameraTarget(float height){
		return camera.position.cpy()
				.add(camera.direction.cpy().scl((height-camera.position.y) / (camera.direction.y)));
	}
	
	Ray m_target = new Ray();
	
	public Vector3 getMouseTarget(float height,Input input){
		m_target = camera.getPickRay(input.getX(),input.getY()).cpy();
		return m_target.origin.add(m_target.direction.scl((height-m_target.origin.y) / (m_target.direction.y)));
	}
	
	
	Vector3 pos = new Vector3();
	
	public boolean isVisible(Visual v){
		v.model.transform.getTranslation(pos);
		pos.add(v.center);
		return camera.frustum.sphereInFrustum(pos, v.radius);
	}
	
	public Model loadModel(String s){
		return modelLoader.loadModel(Gdx.files.internal(s));
	}

}
