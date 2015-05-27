package de.silentinfiltration.game.systems;

import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector2f;

import de.silentinfiltration.engine.ecs.BaseSystem;
import de.silentinfiltration.engine.ecs.EntityManager;
import de.silentinfiltration.engine.ecs.EventManager;
import de.silentinfiltration.engine.ecs.SystemManager;
import de.silentinfiltration.engine.exceptions.ComponentNotFoundEx;
import de.silentinfiltration.game.components.CCamera;
import de.silentinfiltration.game.components.PositionC;

public class CameraSystem extends BaseSystem {

	public CameraSystem(SystemManager systemManager,
			EntityManager entityManager, EventManager eventManager,int priority) {
		super(systemManager, entityManager, eventManager, priority);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean matchesSystem(int entityId) {
		return entityManager.hasComponent(entityId, CCamera.class)
				&& entityManager.hasComponent(entityId, PositionC.class);
	}
	Vector2f activeCam;
	
	@Override
	public void update(long dt, int entity) throws ComponentNotFoundEx {
		PositionC pos = entityManager.getComponent(entity, PositionC.class);
		CCamera cam = entityManager.getComponent(entity, CCamera.class);
		if(!cam.active)
			return;
//		if(cam.focus > -1 )//&& entityManager.hasComponent(cam.focus, PositionC.class)){
//		{
//			PositionC tmp = entityManager.getComponent(cam.focus, PositionC.class);
//			pos.position.x = tmp.position.x - cam.screen.getWidth()/2;
//			pos.position.y = tmp.position.y - cam.screen.getHeight()/2;
//			activeCam = pos.position;
//			System.out.println("update cam");
			if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
				pos.position.y++;
			}
			else if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
				pos.position.y--;
			}
//			else
//				veloComp.velocity.y = 0;

			if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
				pos.position.x--;

			}
			else if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
				pos.position.x++;
			}
//		}
	}

	@Override
	public void render(int entity) throws ComponentNotFoundEx {
		//System.out.println("camera");

		if(activeCam != null){

			//GL11.glPushMatrix();
			//GL11.glLoadIdentity();
			//GLU.gluLookAt(0, 0, 0, activeCam.x, , 0, 0, 0, 0);
			//GL11.glMatrixMode();
//			glMatrixMode(GL_PROJECTION);
//			glLoadIdentity();
			//glOrtho(activeCam.x, activeCam.x + Display.getWidth(),activeCam.y + Display.getHeight(), activeCam.y, 1, -1);
		//	GL11.glTranslatef(-activeCam.x, -activeCam.y, 0);
		//	GL11.glEnd();
		//	glMatrixMode(GL11.GL_MODELVIEW);
			//GL11.glFrustum(left, right, bottom, top, zNear, zFar);
			//GL11.glPopMatrix();
		}
	}

}
