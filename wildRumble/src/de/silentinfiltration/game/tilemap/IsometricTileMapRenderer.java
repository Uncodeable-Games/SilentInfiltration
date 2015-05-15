package de.silentinfiltration.game.tilemap;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTexCoord2d;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.ReadablePoint;
import org.lwjgl.util.Rectangle;
import org.lwjgl.util.WritablePoint;
import org.lwjgl.util.vector.Vector2f;

import de.silentinfiltration.engine.exceptions.ComponentNotFoundEx;
import de.silentinfiltration.game.components.CCamera;
import de.silentinfiltration.game.components.PositionC;
import de.silentinfiltration.game.components.Visual;

public class IsometricTileMapRenderer {
	public Rectangle viewport;
	public Vector2f cam;
	
	public Tilemap tilemap;
	
	int tile_width = 32;
	int tile_height = 16;
	
	public void render()  {
		if(tilemap == null)
			return;
		Vector2f tmp = new Vector2f();;
		Vector2f tmp2 = new Vector2f();;

		for(int i = 0; i < tilemap.length; i++){
			//reversed inner loop for correct rendering
			for(int j = tilemap.width -1; j >= 0; j--){
				tmp.x = (j * tile_width   / 2) + (i * tile_width  / 2);
			    tmp.y = (i * tile_height / 2) - (j * tile_height / 2);
			    tmp2.x = (j * tile_width   / 2) + (i * tile_width  / 2) - cam.x;
			    tmp2.y = (i * tile_height / 2) - (j * tile_height / 2) - cam.y;
			    System.out.println(tmp2);

			   //if((tmp2.x < 0 && tmp2.y < 0) || tmp2.x > viewport.getWidth()  || tmp2.y > viewport.getHeight())
			    if(tmp2.x < viewport.getX() || tmp2.y < viewport.getY() || tmp2.x > viewport.getX() + viewport.getWidth()  || tmp2.y > viewport.getY() + viewport.getHeight())
			    	continue;

			    tilemap.map[i][j].render(cam, tmp);
			}
		}
	//	glLoadIdentity();
		//glOrtho(0, cam.screen.getWidth(), cam.screen.getHeight(), 0, 1, -1);
		//glTranslatef(-camP.position.x + cam.screen.getWidth()/2, -camP.position.y + cam.screen.getHeight()/2, 0);
	
		/*glTranslatef(- camP.position.x + pos.position.x + (visual.tex.getWidth() / 2),
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
		glVertex2f(-visual.image_size.x, visual.image_size.y);*/
		


		
	//	glEnd();

	}
}
