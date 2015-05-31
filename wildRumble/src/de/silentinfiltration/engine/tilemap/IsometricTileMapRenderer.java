package de.silentinfiltration.engine.tilemap;


import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glLoadIdentity;

import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Rectangle;
import org.lwjgl.util.vector.Vector2f;

import de.silentinfiltration.engine.ai.Node;

public class IsometricTileMapRenderer {
	public Rectangle viewport;
	public Vector2f cam;
	
	public Tilemap tilemap;
	
	int tile_width = 64;
	int tile_height = 32;
	public Map<Node, Node> currentPath;
	
	public void render()  {
		if(tilemap == null)
			return;
		Vector2f tmp = new Vector2f();;
		Vector2f tmp2 = new Vector2f();;
//
//		GL11.glPushMatrix();
//		glLoadIdentity();
//		glBegin(GL11.GL_LINES);
		
		for(int i = tilemap.length -1; i >= 0; i--){
			//reversed  loop for correct rendering
			for(int j = 0; j < tilemap.width; j++)
			{
				tmp.x = (j * tile_width   / 2) + (i * tile_width  / 2);
			    tmp.y = (i * tile_height / 2) - (j * tile_height / 2);
			//    Vector2f cam = tilemap.mapToScreen(this.cam);
//			    tmp2.x = (j * tile_width   / 2) + (i * tile_width  / 2) - cam.x;
//			    tmp2.y = (i * tile_height / 2) - (j * tile_height / 2) - cam.y;
			    //System.out.println(tmp2);

			   //if((tmp2.x < 0 && tmp2.y < 0) || tmp2.x > viewport.getWidth()  || tmp2.y > viewport.getHeight())
//			    if(tmp2.x < viewport.getX() || tmp2.y < viewport.getY() || tmp2.x > viewport.getX() + viewport.getWidth()  || tmp2.y > viewport.getY() + viewport.getHeight())
//			    	continue;

			    tilemap.map[i][j].render(cam, tmp, currentPath);
			}
		}

//		GL11.glEnd();
//		//GL11.glPopMatrix();
//		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);// Reset The Color
	}
}
