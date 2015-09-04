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
import de.mih.core.engine.ecs.SystemManager;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.SelectableC;
import de.mih.core.game.components.Visual;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

@SuppressWarnings("rawtypes")
public class RenderSystem extends BaseSystem {

	static List<RenderSystem> registeredRenderSystems = new ArrayList<RenderSystem>();

	public PerspectiveCamera camera;
	public ModelBatch modelBatch;
	public ModelBuilder modelBuilder;

	public ModelLoader modelLoader;
	public Environment environment;
	public ArrayList<Model> allmodeltypes = new ArrayList<Model>();
	public ArrayList<Visual> allvisuals = new ArrayList<Visual>();
	public HashMap<String, Model> storedmodels;

	public final Vector3 X_AXIS = new Vector3(1f, 0f, 0f);
	public final Vector3 Y_AXIS = new Vector3(0f, 1f, 0f);
	public final Vector3 Z_AXIS = new Vector3(0f, 0f, 1f);
	public final Vector3 V_NULL = new Vector3();

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
		modelLoader = new ObjLoader();
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f, 0.8f, 1f));
		
		storedmodels = readinModels("assets/models/");
		
		for(String s : storedmodels.keySet())
		{
			System.out.println(s + " " + storedmodels.get(s).toString());
		}
		// TODO: Outsource Modelinformations

		Model box = modelBuilder.createBox(1f, 1f, 1f, new Material(ColorAttribute.createDiffuse(Color.BLUE)),
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
		allmodeltypes.add(box);
		storedmodels.put("box", box);

		Model redbox = modelBuilder.createBox(1f, 1f, 1f, new Material(ColorAttribute.createDiffuse(Color.RED)),
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
		allmodeltypes.add(redbox);
		storedmodels.put("redbox", redbox);

		Model floor = modelBuilder.createBox(1f, .01f, 1f, new Material(ColorAttribute.createDiffuse(Color.GREEN)),
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
		allmodeltypes.add(floor);
		storedmodels.put("floor", floor);
		//
	}

	@Override
	public boolean matchesSystem(int entityId) {
		return entityManager.hasComponent(entityId, Visual.class)
				&& entityManager.hasComponent(entityId, PositionC.class);
	}

	public void update(double dt, int entity) {
	}

	Vector3 prev_scale = new Vector3();

	@Override
	public void render(int entity) {
		Visual visual = entityManager.getComponent(entity, Visual.class);
		PositionC pos = entityManager.getComponent(entity, PositionC.class);

		
		
		visual.model.transform.setToTranslation(pos.position.x + visual.pos.x, pos.position.y + visual.pos.y,
				pos.position.z + visual.pos.z);
		visual.model.transform.rotate(0f, 1f, 0f, pos.angle + visual.angle);
		visual.model.transform.scale(visual.getScale().x, visual.getScale().y, visual.getScale().z);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		camera.update();
		
		modelBatch.begin(camera);
		//entityManager.getEntitiesForType(Visual.class).iterator();
		for (Visual v : allvisuals) {
			if (isVisible(v)) {
				modelBatch.render(v.model, environment);
			}
		}
		modelBatch.end();
	}

	@Override
	public void update(double dt) {
	}

	public Vector3 getCameraTarget(float height) {
		return camera.position.cpy()
				.add(camera.direction.cpy().scl((height - camera.position.y) / (camera.direction.y)));
	}

	
	Ray m_target = new Ray();

	public Vector3 getMouseTarget(float height, Input input) {
		m_target = camera.getPickRay(input.getX(), input.getY()).cpy();
		return m_target.origin.add(m_target.direction.scl((height - m_target.origin.y) / (m_target.direction.y)));
	}

	
	Vector3 pos = new Vector3();

	public boolean isVisible(Visual v) {
		v.model.transform.getTranslation(pos);
		pos.add(v.center);
		return camera.frustum.sphereInFrustum(pos, v.radius);
	}

	public Model getModelByName(String s) {
		if (storedmodels.containsKey(s)) {
			return storedmodels.get(s);
		}
		System.out.println("Model " + s + " not found!");
		return storedmodels.get("redbox");
	}

	
	/**
	 * Reads in all .obj-Files in 'path' and subfolders,
	 * converts them into {@link Model} and saves then in a 
	 * {@link HashMap} with 'path' as the key
	 * 
	 * @param path	Path where the .obj Files will be read
	 *            
	 * @return		{@link HashMap} where {@link Model}s are saved			
	 */
	public HashMap<String, Model> readinModels(String path) {
		HashMap<String, Model> temp = new HashMap<String, Model>();
		try {
			Files.walk(Paths.get(path)).forEach(filePath -> {
				if (Files.isRegularFile(filePath)) {
					FileHandle handle = Gdx.files.internal(filePath.toAbsolutePath().toString());
					if (handle.extension().equals("obj")) {
						temp.put(handle.name(), modelLoader.loadModel(Gdx.files.internal(handle.path())));
						allmodeltypes.add(temp.get(handle.name()));
					}
				}
			});
		} catch (IOException e) {e.printStackTrace();}
		return temp;
	}

}
