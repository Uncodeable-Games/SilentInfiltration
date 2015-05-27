package de.silentinfiltration.game.tilemap;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTexCoord2d;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL11.glOrtho;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Rectangle;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.Texture;

import de.silentinfiltration.engine.ai.Node;

public class Tile extends Node{
	public Texture tex;
	public Vector2f image_size;
//	public Vector2f position;
	//TODO: kann später durch ne body class oder so ersetzt werden
	public Rectangle body;

	
	public void render(Vector2f camP, Vector2f position)
	{
		if(tex == null)
			return;
		tex.bind();
		//
		GL11.glPushMatrix();
		glLoadIdentity();
		glTranslatef(camP.x,camP.y,0);

//		glTranslatef(- camP.x + position.x + (tex.getWidth() / 2),
//				camP.y + Display.getHeight() - tex.getHeight() - position.y
//						+ tex.getHeight() / 2, 0);
//		glTranslatef(- camP.x + position.x + (tex.getWidth() / 2),
//				camP.y + Display.getHeight() - image_size.y - position.y
//						+ image_size.y / 2, 0);
		glTranslatef( position.x + (tex.getWidth() / 2),
				 Display.getHeight() - image_size.y - position.y
						+ image_size.y / 2, 0);
		//glTranslatef(-tex.getWidth() / 2, -tex.getHeight() / 2, 0);
		glTranslatef(-tex.getWidth() / 2, -tex.getHeight() / 2, 0);
		glRotatef(90,0,0,1);
//		glRotatef(35.264f, 1.0f, 0.0f, 0.0f);
//		glRotatef(-45.0f, 0.0f, 1.0f, 0.0f);
		if(this.isPath){
			//System.out.println(this.previous);
			GL11.glColor3f(1, 1, 0);
		}
		glBegin(GL_QUADS);
		glTexCoord2d(0, 0);
		glVertex2f(-image_size.x, -image_size.y);

		glTexCoord2d(0, 1);
		glVertex2f(image_size.x, -image_size.y);

		glTexCoord2d(1, 1);
		glVertex2f(image_size.x, image_size.y);

		glTexCoord2d(1, 0);
		glVertex2f(-image_size.x, image_size.y);
		glEnd();
		GL11.glPopMatrix();
		if(this.isPath){
			//System.out.println(this.previous);
			GL11.glColor3f(1, 1, 1);
		}
	}
}
