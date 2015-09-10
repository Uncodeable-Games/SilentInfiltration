package de.mih.core.game.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.render.Visual;
import de.mih.core.game.components.VisualC;
import de.mih.core.game.systems.RenderSystem;

public class RenderManager {
	PerspectiveCamera camera;
	RenderSystem renderSystem;
	EntityManager entityManager;
	TilemapRenderer tilemapRenderer;
	private ModelBatch modelBatch;
	private ModelBuilder modelBuilder;
	private ObjLoader modelLoader;
	private Environment environment;
	
	static RenderManager renderManager;
	
	public static RenderManager getInstance()
	{
		if(renderManager == null)
		{
			renderManager = new RenderManager();
		}
		return renderManager;
	}

	public RenderManager()
	{
		modelBatch = new ModelBatch();
		modelBuilder = new ModelBuilder();
		modelLoader = new ObjLoader();
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f, 0.8f, 1f));
		
		
	}
	
	public void setCamera(PerspectiveCamera camera)
	{
		this.camera = camera;
	}
	
	public void startRender()
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		camera.update();
		modelBatch.begin(camera);

	}
	
	public void endRender()
	{
		modelBatch.end();
	}
	
	
	public PerspectiveCamera getCamera()
	{
		return camera;
	}
	
	
	public ModelBatch getModelBatch() {
		return modelBatch;
	}

	public void setModelBatch(ModelBatch modelBatch) {
		this.modelBatch = modelBatch;
	}

	public ModelBuilder getModelBuilder() {
		return modelBuilder;
	}

	public void setModelBuilder(ModelBuilder modelBuilder) {
		this.modelBuilder = modelBuilder;
	}

	public ObjLoader getModelLoader() {
		return modelLoader;
	}

	public void setModelLoader(ObjLoader modelLoader) {
		this.modelLoader = modelLoader;
	}

	public Environment getEnvironment() {
		return environment;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
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


}
