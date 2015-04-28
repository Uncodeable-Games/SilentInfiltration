package CoreEngine.systems;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTexCoord2d;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;

import CoreEngine.components.PositionC;
import CoreEngine.components.Visual;
import CoreEngine.ecs.BaseSystem;
import CoreEngine.ecs.EntityManager;
import CoreEngine.ecs.EventManager;
import CoreEngine.ecs.SystemManager;
import Exceptions.ComponentNotFoundEx;

public class RenderSystem extends BaseSystem {

	static List<RenderSystem> registeredRenderSystems = new ArrayList<RenderSystem>();

	public RenderSystem(SystemManager systemManager,
			EntityManager entityManager, EventManager eventManager) {
		super(systemManager, entityManager, eventManager);
		if (!registeredRenderSystems.contains(this))
			registeredRenderSystems.add(this);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean matchesSystem(int entityId) {
		return entityManager.hasComponent(entityId, Visual.class)
				&& entityManager.hasComponent(entityId, PositionC.class);
	}

	@Override
	public void update(long dt, int entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(int entity) throws ComponentNotFoundEx {
		Visual visual = entityManager.getComponent(entity, Visual.class);
		PositionC pos = entityManager.getComponent(entity, PositionC.class);

		visual.tex.bind();

		glLoadIdentity();
		glTranslatef(pos.position.x + (visual.tex.getWidth() / 2),
				Display.getHeight() - visual.tex.getHeight() - pos.position.y
						+ visual.tex.getHeight() / 2, 0);
		glRotatef(-pos.angle + 90, 0, 0, 1);
		glTranslatef(-visual.tex.getWidth() / 2, -visual.tex.getHeight() / 2, 0);

		glBegin(GL_QUADS);
		glTexCoord2d(0, 0);
		glVertex2f(-visual.image_size.x, -visual.image_size.y);

		glTexCoord2d(0, 1);
		glVertex2f(visual.image_size.x, -visual.image_size.y);

		glTexCoord2d(1, 1);
		glVertex2f(visual.image_size.x, visual.image_size.y);

		glTexCoord2d(1, 0);
		glVertex2f(-visual.image_size.x, visual.image_size.y);

		glEnd();

	}

	static public void disposeall(EntityManager entityManager) {

		for (int entity = 0; entity < entityManager.entityCount; entity++) {
			for (RenderSystem s : registeredRenderSystems) {
				if (s.matchesSystem(entity)) {
					try {
						s.entityManager.getComponent(entity, Visual.class).tex
								.release();
					} catch (ComponentNotFoundEx e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

}
