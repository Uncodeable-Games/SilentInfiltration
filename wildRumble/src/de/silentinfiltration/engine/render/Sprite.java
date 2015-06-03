package de.silentinfiltration.engine.render;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTexCoord2d;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.Texture;

public class Sprite {
	public Texture texture;
	public Vector2f imageSize;
	
	public Sprite(Texture texture, Vector2f imageSize)
	{
		this.texture = texture;
		this.imageSize = imageSize;
	}
	
	public void draw(Vector2f position)
	{
		texture.bind();
		glTranslatef(position.x, - position.y, 0);
		glTranslatef(-texture.getWidth() / 2,  Display.getHeight() -texture.getHeight() / 2, 0);
		glTranslatef(-texture.getWidth() / 2,  -texture.getHeight() / 2, 0);
		glRotatef(90,0,0,1);
		glBegin(GL_QUADS);
		glTexCoord2d(0, 0);
		glVertex2f(-imageSize.x, -imageSize.y);

		glTexCoord2d(0, 1);
		glVertex2f(imageSize.x, -imageSize.y);

		glTexCoord2d(1, 1);
		glVertex2f(imageSize.x, imageSize.y);

		glTexCoord2d(1, 0);
		glVertex2f(-imageSize.x, imageSize.y);
		//glEnd();
	}
}
