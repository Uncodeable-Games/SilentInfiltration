package Widgetclasses;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTexCoord2d;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import Widgetclasses.*;
import CoreEngine.*;

public class Widget extends Entity {

	public int angle;

	public Texture image_texture;
	public String image_path;
	public int image_ID;
	public Vector2f image_size = new Vector2f(32, 32);

	public int cord_x = 50;
	public int cord_y = 50;

	public int widget_TypeId;
	public int widget_Id;

	public int col_height = 1;

	static LinkedList<Widget> llwidget = new LinkedList<Widget>();

	public Widget(String path) {
		llwidget.add(this);

		image_path = path;
		try {
			image_texture = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream(image_path), GL_NEAREST);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		;
		image_ID = image_texture.getTextureID();

	}

	public Widget(Texture tex) {
		llwidget.add(this);
		image_texture = tex;
		image_ID = image_texture.getTextureID();
	}

	public static void renderall() {
		Iterator<Widget> llwidgetiterator = llwidget.iterator();
		while (llwidgetiterator.hasNext()) {
			llwidgetiterator.next().render();
		}
	}

	public static void inputall() {
		Iterator<Widget> llwidgetiterator = llwidget.iterator();
		while (llwidgetiterator.hasNext()) {
			llwidgetiterator.next().input();
		}
	}

	public void dispose() {
		image_texture.release();
	}
	
	public static void bla(int a){ a=34; System.out.println(a);}

	public void setPos(int x, int y, boolean withcol) {
		if (!withcol) {
			this.cord_x = x;
			this.cord_y = y;
		} else {

			LinkedList<Widget> templl = llwidget;

			boolean again = false;
			int newcord_x = x;
			int newcord_y = y;

			Iterator<Widget> llwidgetiterator = templl.iterator();
			while (llwidgetiterator.hasNext()) {
				Widget nextwidget = llwidgetiterator.next();
				if (nextwidget != this) {
					if (nextwidget instanceof Moveable) {
						Moveable thismoveable = (Moveable) this;
						Moveable nextmoveable = (Moveable) nextwidget;
						if (Math.sqrt(((x - nextmoveable.cord_x) * (x - nextmoveable.cord_x))
								+ ((y - nextmoveable.cord_y) * (y - nextmoveable.cord_y))) <= thismoveable.col
								+ nextmoveable.col) {
							Vector2f newpos = new Vector2f();
							newpos.x = x - nextwidget.cord_x;
							newpos.y = y - nextwidget.cord_y;
							newpos.x = newpos.x
									* (thismoveable.col + nextmoveable.col)
									/ newpos.length();
							newpos.y = newpos.y
									* (thismoveable.col + nextmoveable.col)
									/ newpos.length();

							int deltax = 0;
							int deltay = 0;

							if (newpos.x < 0)
								deltax = -1;
							if (newpos.x > 0)
								deltax = 1;
							if (newpos.y < 0)
								deltay = -1;
							if (newpos.y > 0)
								deltay = 1;

							newcord_x = (int) (nextwidget.cord_x + newpos.x + deltax);
							newcord_y = (int) (nextwidget.cord_y + newpos.y + deltay);
							again = true;
						}
					}

					if (nextwidget instanceof Obstracle) {
						Moveable thismoveable = (Moveable) this;
						Obstracle nextobstracle = (Obstracle) nextwidget;

						// Distance between both Widgets
						double tmp1 = ((nextobstracle.cord_x - thismoveable.cord_x) * (nextobstracle.cord_x - thismoveable.cord_x))
										+ ((nextobstracle.cord_y - thismoveable.cord_y) * (nextobstracle.cord_y - thismoveable.cord_y));

						// Max Collision of Obstracle + Collision of Moveable
						double tmp2 = (nextobstracle.col_x * nextobstracle.col_x)
										+ (nextobstracle.col_y * nextobstracle.col_y)
								+ thismoveable.col * thismoveable.col;

						if (tmp1 <= tmp2) {

							int tmpRecToCircle_x = nextwidget.cord_x
									- thismoveable.cord_x;
							int tmpRecToCircle_y = nextwidget.cord_y
									- thismoveable.cord_y;

							int RecToCircle_x = (int) (tmpRecToCircle_x
									* Math.cos((double) nextobstracle.angle) - tmpRecToCircle_y
									* Math.sin((double) nextobstracle.angle));
							int RecToCircle_y = (int) (tmpRecToCircle_x
									* Math.sin((double) nextobstracle.angle) + tmpRecToCircle_y
									* Math.cos((double) nextobstracle.angle));

							float cx = Math.abs(thismoveable.cord_x
									- nextobstracle.cord_x
									- nextobstracle.col_x);
							float xDist = nextobstracle.col_x
									+ thismoveable.col;
							if (cx > xDist)
								continue;

							float cy = Math.abs(thismoveable.cord_y
									- nextobstracle.cord_y
									- nextobstracle.col_y);
							float yDist = nextobstracle.col_y
									+ thismoveable.col;
							if (cy > yDist)
								continue;
							if (!(cx <= nextobstracle.col_x || cy <= nextobstracle.col_y)) {
								float xCornerDist = cx - nextobstracle.col_x;
								float yCornerDist = cy - nextobstracle.col_y;
								float xCornerDistSq = xCornerDist * xCornerDist;
								float yCornerDistSq = yCornerDist * yCornerDist;
								float maxCornerDistSq = thismoveable.col
										* thismoveable.col;
								if (!(xCornerDistSq + yCornerDistSq <= maxCornerDistSq))
									continue;
							}
							
							

						}
					}
				}
			}
			if (again) {
				setPos(newcord_x, newcord_y, true);
			} else {
				cord_x = newcord_x;
				cord_y = newcord_y;
			}

		}
	}

	void render() {

		image_texture.bind();

		glLoadIdentity();
		glTranslatef(cord_x + (image_texture.getWidth() / 2),
				Display.getHeight() - image_texture.getHeight() - cord_y
						+ image_texture.getHeight() / 2, 0);
		glRotatef(-angle + 90, 0, 0, 1);
		glTranslatef(-image_texture.getWidth() / 2,
				-image_texture.getHeight() / 2, 0);

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

	}

	void resize(float a, float b) {
		image_size.x = a;
		image_size.y = b;
	}

	void input() {
	}

	public int getWidgetTypeId() {
		return widget_TypeId;
	}

}
