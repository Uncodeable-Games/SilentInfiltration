package de.silentinfiltration.game.tilemap;


import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glLoadIdentity;

import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Rectangle;
import org.lwjgl.util.vector.Vector2f;

import de.silentinfiltration.engine.ai.Node;
import de.silentinfiltration.engine.tilemap.Tilemap;
import de.silentinfiltration.engine.tilemap.TilemapRenderer;

public class IsometricTileMapRenderer extends TilemapRenderer{
	public Rectangle viewport;
	public Vector2f cam;
	
	public Tilemap tilemap;
	
	int tile_width = 64;
	int tile_height = 32;
	
	private int halftwidth, halftheight;
	
	public Map<Node, Node> currentPath;
	public Vector2f tempvec;
	
	public void render()  {
		if(tilemap == null)
			return;
		Vector2f tmp = new Vector2f();
		Vector2f tmp2 = new Vector2f();

		
		for(int y = 0; y < tilemap.length ; y++){
			//reversed  loop for correct rendering
			for(int x = 0; x < tilemap.width; x ++)  // tilemap.width -1 ; x >= 0; x--)
			{
		
//				tmp.x = j * tile_width;
//				tmp.y = i * tile_height;
				tmp.x = (x - y ) * tile_width  / 2;
			    tmp.y = (x + y)  * tile_height / 2 ;

			   //if((tmp2.x < 0 && tmp2.y < 0) || tmp2.x > viewport.getWidth()  || tmp2.y > viewport.getHeight())
//			    if(tmp2.x < viewport.getX() || tmp2.y < viewport.getY() || tmp2.x > viewport.getX() + viewport.getWidth()  || tmp2.y > viewport.getY() + viewport.getHeight())
//			    	continue;

			    tilemap.map[y][x].render(cam, tmp, currentPath);
			}
		}

//		GL11.glEnd();
//		//GL11.glPopMatrix();
//		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);// Reset The Color
	}

	@Override
	public Vector2f fromScreen(Vector2f screen) {
Vector2f result = new Vector2f();
		//screen.x * 2 = (x-y) * tile_width
		//screen.y * 2 = (x+y) * tile_height
		//screen.x * 2  / tile_width =  x - y
		//screen.y * 2 / tile_height = x + y
		result.x = screen.x / halftwidth+ screen.y / halftheight;
		result.y = (screen.y ) / halftheight - screen.x / halftwidth;
		return result;
	}

	@Override
	public Vector2f toScreen(Vector2f map) {
		Vector2f tmp = new Vector2f();
		tmp.x = (map.x * halftwidth)
				+ (map.y * halftwidth);
		tmp.y = (map.y * halftheight)
				- (map.x * halftheight);
		// System.out.println(tmp);
		return tmp;
	}
}
