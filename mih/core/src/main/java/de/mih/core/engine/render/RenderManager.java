package de.mih.core.engine.render;

import java.util.ArrayList;
import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.engine.ecs.component.Component;
import de.mih.core.game.Game;
import de.mih.core.game.components.PositionC;
import de.mih.core.game.components.VisualC;
import de.mih.core.game.render.TilemapRenderer;
import de.mih.core.game.systems.RenderSystem;

public class RenderManager
{
	PerspectiveCamera camera;
	RenderSystem renderSystem;
	EntityManager entityManager;
	TilemapRenderer tilemapRenderer;
	public SpriteBatch spriteBatch;
	public ShapeRenderer shapeRenderer;
	private ModelBatch modelBatch;
	private ModelBuilder modelBuilder;
	private ObjLoader modelLoader;
	private Environment environment;

	ArrayList<BaseRenderer> registertMBRenderer = new ArrayList<BaseRenderer>();
	ArrayList<BaseRenderer> registertSBRenderer = new ArrayList<BaseRenderer>();

	public RenderManager(EntityManager entityManager)
	{
		this.entityManager = entityManager;
		spriteBatch = new SpriteBatch();
		modelBatch = new ModelBatch();
		modelBuilder = new ModelBuilder();
		modelLoader = new ObjLoader();
		environment = new Environment();
		shapeRenderer = new ShapeRenderer();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.2f, 0.2f, 0.2f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
	}

	Comparator<BaseRenderer> comp = new Comparator<BaseRenderer>() {
		public int compare(BaseRenderer o1, BaseRenderer o2)
		{
			if (o1.priority > o2.priority)
				return 1;
			else
				return -1;
		};
	};

	public void register(BaseRenderer renderer)
	{
		if (renderer.usemodebatch)
		{
			if (!registertMBRenderer.contains(renderer))
			{
				registertMBRenderer.add(renderer);
				registertMBRenderer.sort(comp);
			}
		}
		else
		{
			if (!registertSBRenderer.contains(renderer))
			{
				registertSBRenderer.add(renderer);
				registertSBRenderer.sort(comp);
			}
		}
	}

	public void unregister(BaseRenderer renderer)
	{
		if (renderer.usemodebatch)
		{
			if (registertMBRenderer.contains(renderer))
				registertMBRenderer.remove(renderer);
		}
		else
		{
			if (registertSBRenderer.contains(renderer))
				registertSBRenderer.remove(renderer);
		}
	}

	public void setCamera(PerspectiveCamera camera)
	{
		this.camera = camera;
	}

	public void render()
	{
		camera.update();
		modelBatch.begin(camera);
		for (BaseRenderer renderer : registertMBRenderer)
		{
			renderer.render();
		}
		modelBatch.end();

		spriteBatch.begin();
		for (BaseRenderer renderer : registertSBRenderer)
		{
			renderer.render();
		}
		spriteBatch.end();

	}

	public PerspectiveCamera getCamera()
	{
		return camera;
	}

	public ModelBatch getModelBatch()
	{
		return modelBatch;
	}

	public void setModelBatch(ModelBatch modelBatch)
	{
		this.modelBatch = modelBatch;
	}

	public ModelBuilder getModelBuilder()
	{
		return modelBuilder;
	}

	public void setModelBuilder(ModelBuilder modelBuilder)
	{
		this.modelBuilder = modelBuilder;
	}

	public ObjLoader getModelLoader()
	{
		return modelLoader;
	}

	public void setModelLoader(ObjLoader modelLoader)
	{
		this.modelLoader = modelLoader;
	}

	public Environment getEnvironment()
	{
		return environment;
	}

	public void setEnvironment(Environment environment)
	{
		this.environment = environment;
	}

	public Vector3 getCameraTarget(float height)
	{
		return camera.position.cpy()
				.add(camera.direction.cpy().scl((height - camera.position.y) / (camera.direction.y)));
	}

	Vector3 temp_pos = new Vector3();
	Vector3 min_pos = new Vector3();
	int min_entity;

	// TODO: move
	/*
	 * we should create an interface between the input and the rendering so we can move this
	 */
	@Deprecated
	@SuppressWarnings("unchecked")
	public int getSelectedEntityByFilter(int mouseX, int mouseY, Class<? extends Component>... classes)
	{
		Ray ray = camera.getPickRay(mouseX, mouseY);
		min_entity = -1;
		for (int i = 0; i < this.entityManager.entityCount; i++)
		{
			if (!this.entityManager.hasComponent(i, VisualC.class)
					|| !this.entityManager.hasComponent(i, PositionC.class))
			{
				continue;
			}

			boolean hasclass = true;
			for (Class<? extends Component> c : classes)
			{
				if (!this.entityManager.hasComponent(i, c))
				{
					hasclass = false;
				}
			}
			if (!hasclass)
				continue;

			VisualC vis = this.entityManager.getComponent(i, VisualC.class);
			PositionC pos = this.entityManager.getComponent(i, PositionC.class);

			float radius = (vis.visual.bounds.getWidth() + vis.visual.bounds.getDepth()) / 2f;

			temp_pos.set(pos.getPos());
			temp_pos.add(vis.visual.pos);
			temp_pos.y += vis.visual.bounds.getHeight() / 2f;

			if (Intersector.intersectRaySphere(ray, temp_pos, radius, null))
			{
				if (min_entity == -1 || ray.origin.dst2(temp_pos) < ray.origin.dst2(min_pos))
				{
					min_entity = i;
					min_pos = pos.getPos();
				}
			}

		}
		return min_entity;
	}

	Ray m_target = new Ray();

	public Vector3 getMouseTarget(float height, Input input)
	{
		m_target = camera.getPickRay(input.getX(), input.getY()).cpy();
		return m_target.origin.add(m_target.direction.scl((height - m_target.origin.y) / (m_target.direction.y)));
	}

	Vector3 pos = new Vector3();

	public boolean isVisible(Visual v)
	{
		v.model.transform.getTranslation(pos);
		pos.add(v.center);
		return camera.frustum.sphereInFrustum(pos, v.radius);
	}

	public boolean isVisible(Vector3 v)
	{
		PerspectiveCamera camera = Game.getCurrentGame().getCamera();
		return camera.frustum.pointInFrustum(v);
	}

}
