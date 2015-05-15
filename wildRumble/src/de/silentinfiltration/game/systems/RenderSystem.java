package de.silentinfiltration.game.systems;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTexCoord2d;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL11.glOrtho;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;

import de.silentinfiltration.engine.ecs.BaseSystem;
import de.silentinfiltration.engine.ecs.EntityManager;
import de.silentinfiltration.engine.ecs.EventManager;
import de.silentinfiltration.engine.ecs.SystemManager;
import de.silentinfiltration.engine.exceptions.ComponentNotFoundEx;
import de.silentinfiltration.game.components.CCamera;
import de.silentinfiltration.game.components.PositionC;
import de.silentinfiltration.game.components.VelocityC;
import de.silentinfiltration.game.components.Visual;

public class RenderSystem extends BaseSystem {

	static List<RenderSystem> registeredRenderSystems = new ArrayList<RenderSystem>();

	public int camEntity = -1;
	
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
	public void update(long dt, int entity) throws ComponentNotFoundEx {
		
		
	}
	
	public void setCamera(int entityID)
	{
		if(entityManager.hasComponent(entityID, CCamera.class)
		&& entityManager.hasComponent(entityID, PositionC.class))
			this.camEntity = entityID;
		else
			this.camEntity = -1;
	}

	@Override
	public void render(int entity) throws ComponentNotFoundEx {
		Visual visual = entityManager.getComponent(entity, Visual.class);
		PositionC pos = entityManager.getComponent(entity, PositionC.class);

		if(!(this.camEntity > -1))
		{
			return;
		}
		visual.tex.bind();
		
		PositionC camP = entityManager.getComponent(camEntity, PositionC.class);
		CCamera cam = entityManager.getComponent(camEntity,CCamera.class);
		
		//System.out.println(camP.position);
//		if(pos.position.x < camP.position.x - cam.screen.getWidth()/2 || pos.position.x > camP.position.x + cam.screen.getWidth()/2 ||
//				pos.position.y > camP.position.y + cam.screen.getHeight()/2 || pos.position.y < camP.position.y - cam.screen.getHeight()/2)
//			return;
//				glTranslatef(-camP.position.x + cam.screen.getWidth()/2, -camP.position.y + cam.screen.getHeight()/2, 0);
	//	glTranslatef(-camP.position.x + cam.screen.getWidth()/2, -camP.position.y + cam.screen.getHeight()/2, 0);

		glLoadIdentity();
		//glOrtho(0, cam.screen.getWidth(), cam.screen.getHeight(), 0, 1, -1);
		//glTranslatef(-camP.position.x + cam.screen.getWidth()/2, -camP.position.y + cam.screen.getHeight()/2, 0);
		glTranslatef(- camP.position.x + pos.position.x + (visual.tex.getWidth() / 2),
				camP.position.y + Display.getHeight() - visual.tex.getHeight() - pos.position.y
						+ visual.tex.getHeight() / 2, 0);
		glRotatef(-pos.angle + 90, 0, 0, 1);
		//glTranslatef(+camP.position.x - cam.screen.getWidth()/2, +camP.position.y - cam.screen.getHeight()/2, 0);
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
