package de.silentinfiltration.engine.render;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2d;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.Texture;

public class Sprite {
	Texture texture;
	Vector2f image_size;
	
	public void drawAt(Vector2f position)
	{
		texture.bind();

		glTranslatef(-texture.getWidth() / 2, -texture.getHeight() / 2, 0);
		glBegin(GL_QUADS);
		glTexCoord2d(0, 0);
		glVertex2f(-image_size.x, -image_size.y/2);

		glTexCoord2d(0, 1);
		glVertex2f(image_size.x, -image_size.y/2);

		glTexCoord2d(1, 1);
		glVertex2f(image_size.x, image_size.y/2);

		glTexCoord2d(1, 0);
		glVertex2f(-image_size.x, image_size.y/2);
		glEnd();
		GL11.glPopMatrix();
	}
}
