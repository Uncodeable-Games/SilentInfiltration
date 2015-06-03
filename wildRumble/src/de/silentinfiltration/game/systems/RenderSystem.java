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
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glPopMatrix;


import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import de.silentinfiltration.engine.ecs.BaseSystem;
import de.silentinfiltration.engine.ecs.EntityManager;
import de.silentinfiltration.engine.ecs.EventManager;
import de.silentinfiltration.engine.ecs.SystemManager;
import de.silentinfiltration.engine.exceptions.ComponentNotFoundEx;
import de.silentinfiltration.game.components.CCamera;
import de.silentinfiltration.game.components.PositionC;
import de.silentinfiltration.game.components.VelocityC;
import de.silentinfiltration.game.components.Visual;
import de.silentinfiltration.engine.tilemap.Tilemap;

public class RenderSystem extends BaseSystem {

	static List<RenderSystem> registeredRenderSystems = new ArrayList<RenderSystem>();
	public Tilemap tilemap;
	public int camEntity = -1;
	
	public RenderSystem(SystemManager systemManager,
			EntityManager entityManager, EventManager eventManager) {
		this(systemManager,entityManager,eventManager,1);
	}
	public RenderSystem(SystemManager systemManager,
			EntityManager entityManager, EventManager eventManager,int priority) {
		super(systemManager, entityManager, eventManager, priority);
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
	public void update(double dt, int entity) throws ComponentNotFoundEx {
		
		
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
		//System.out.println("render");
		Visual visual = entityManager.getComponent(entity, Visual.class);
		PositionC pos = entityManager.getComponent(entity, PositionC.class);

		if(!(this.camEntity > -1))
		{
			return;
		}
		//visual.tex.bind();
		Vector2f position = tilemap.mapToScreen(pos.position);
		System.out.println(position);
		PositionC camP = entityManager.getComponent(camEntity, PositionC.class);
		//CCamera cam = entityManager.getComponent(camEntity,CCamera.class);
		//Vector2f cposition =  tilemap.mapToScreen(camP.position);
		
		//System.out.println(camP.position);
//		if(pos.position.x < camP.position.x - cam.screen.getWidth()/2 || pos.position.x > camP.position.x + cam.screen.getWidth()/2 ||
//				pos.position.y > camP.position.y + cam.screen.getHeight()/2 || pos.position.y < camP.position.y - cam.screen.getHeight()/2)
//			return;
//				glTranslatef(-camP.position.x + cam.screen.getWidth()/2, -camP.position.y + cam.screen.getHeight()/2, 0);
	//	glTranslatef(-camP.position.x + cam.screen.getWidth()/2, -camP.position.y + cam.screen.getHeight()/2, 0);
		//System.out.println(pos.position);

		//System.out.println(position);
		glPushMatrix();
		//glLoadIdentity();
		glTranslatef(camP.position.x,camP.position.y,0);
		//glTranslatef(position.x, - position.y, 0);
		//glTranslatef(-visual.sprite.texture.getWidth() / 2,  -visual.sprite.texture.getHeight() / 2, 0);
		visual.sprite.draw(position);
//		glTranslatef( position.x + (visual.tex.getWidth() / 2),
//				Display.getHeight() - visual.tex.getHeight() - position.y
//						+ visual.tex.getHeight() / 2, 0);
//		glTranslatef(- camP.position.x + position.x + (visual.tex.getWidth() / 2),
//				camP.position.y + Display.getHeight() - visual.tex.getHeight() - position.y
//						+ visual.tex.getHeight() / 2, 0);
		//glOrtho(0, cam.screen.getWidth(), cam.screen.getHeight(), 0, 1, -1);
		//glTranslatef(-camP.position.x + cam.screen.getWidth()/2, -camP.position.y + cam.screen.getHeight()/2, 0);
//		glTranslatef(- camP.position.x + pos.position.x + (visual.tex.getWidth() / 2),
//				camP.position.y + Display.getHeight() - visual.tex.getHeight() - pos.position.y
//						+ visual.tex.getHeight() / 2, 0);
//		glRotatef(-pos.angle + 90, 0, 0, 1);
//		//glTranslatef(+camP.position.x - cam.screen.getWidth()/2, +camP.position.y - cam.screen.getHeight()/2, 0);
//		glTranslatef(-visual.tex.getWidth() / 2, -visual.tex.getHeight() / 2, 0);
//
//		glBegin(GL_QUADS);
//		glTexCoord2d(0, 0);
//		glVertex2f(-visual.image_size.x, -visual.image_size.y);
//
//		glTexCoord2d(0, 1);
//		glVertex2f(visual.image_size.x, -visual.image_size.y);
//
//		glTexCoord2d(1, 1);
//		glVertex2f(visual.image_size.x, visual.image_size.y);
//
//		glTexCoord2d(1, 0);
//		glVertex2f(-visual.image_size.x, visual.image_size.y);
		
		

		
		glEnd();
		glPopMatrix();

	}

	static public void disposeall(EntityManager entityManager) {

		for (int entity = 0; entity < entityManager.entityCount; entity++) {
			for (RenderSystem s : registeredRenderSystems) {
//				if (s.matchesSystem(entity)) {
//					try {
//						s.entityManager.getComponent(entity, Visual.class).tex
//								.release();
//					} catch (ComponentNotFoundEx e) {
//						e.printStackTrace();
//					}
//				}
			}
		}
	}
	
	void DrawCircle(float cx, float cy, float r, int num_segments) 
	{ 
		float theta = 2.0f * 3.1415926f / (float) num_segments; 
		float tangetial_factor = (float) Math.tan(theta);//calculate the tangential factor 

		float radial_factor = (float) Math.cos(theta);//calculate the radial factor 
		
		float x = r;//we start at angle = 0 

		float y = 0; 
	    
		GL11.glBegin(GL11.GL_LINE_LOOP); 
		for(int ii = 0; ii < num_segments; ii++) 
		{ 
			GL11.glVertex2f(x + cx, y + cy);//output vertex 
	        
			//calculate the tangential vector 
			//remember, the radial vector is (x, y) 
			//to get the tangential vector we flip those coordinates and negate one of them 

			float tx = -y; 
			float ty = x; 
	        
			//add the tangential vector 

			x += tx * tangetial_factor; 
			y += ty * tangetial_factor; 
	        
			//correct using the radial factor 

			x *= radial_factor; 
			y *= radial_factor; 
		} 
		GL11.glEnd(); 
	}

}
