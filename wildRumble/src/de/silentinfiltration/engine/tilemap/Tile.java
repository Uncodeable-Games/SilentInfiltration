package de.silentinfiltration.engine.tilemap;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTexCoord2d;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL11.glOrtho;

import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Rectangle;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.Texture;

import de.silentinfiltration.engine.ai.Node;
import de.silentinfiltration.engine.render.Sprite;

public class Tile extends Node{
	//public Texture tex;
	//public Vector2f image_size;
	public Sprite sprite;
	//public Tilemap map;
//	public Vector2f position;
	//TODO: kann später durch ne body class oder so ersetzt werden
	public Rectangle body;
	
	public String toString() { return x + ", " + y; }
	
	public void render(Vector2f camP, Vector2f position, Map<Node, Node> currentPath)
	{
		if(sprite == null)
			return;
		//tex.bind();
		//
		GL11.glPushMatrix();
		glLoadIdentity();
		glTranslatef(camP.x,camP.y,0);
		glTranslatef(0,-sprite.imageSize.y/2,0);
		//glTranslatef( position.x,- position.y, 0);
		
		//glTranslatef(-sprite.imageSize.x /2,  -sprite.imageSize.y / 2, 0);

		//glTranslatef(sprite.texture.getWidth() / 2, sprite.texture.getHeight() / 2, 0);
//		glTranslatef(-tex.getWidth() / 2, -tex.getHeight() / 2, 0);
//		glRotatef(90,0,0,1);
////		glRotatef(35.264f, 1.0f, 0.0f, 0.0f);
////		glRotatef(-45.0f, 0.0f, 1.0f, 0.0f);
		if(currentPath != null && currentPath.containsKey(this)){
			GL11.glColor3f(1, 1, 0);
		}


		sprite.draw(position);

		glEnd();
		GL11.glPopMatrix();
		if( currentPath != null && 
				currentPath.containsKey(this)){
			//System.out.println(this.previous);
			GL11.glColor3f(1, 1, 1);
		}
	}
}
