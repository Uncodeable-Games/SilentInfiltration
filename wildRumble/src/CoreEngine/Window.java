package CoreEngine;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;


public class Window {
	
	public static void create(int width, int height){
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setTitle("Wild Rumble");
			Display.create();
			initGL();
			initInput();
			
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	private static void initGL(){
		
		glEnable(GL_TEXTURE_2D);              
		glClearColor(193/255f,142/255f, 23/255f, 0.0f);         
 
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glViewport(0,0,Display.getWidth(),Display.getHeight());
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, Display.getWidth(),Display.getHeight(), 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		
	}
	
	private static void initInput(){
		try {
			Keyboard.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	public static void update(){
		Display.update();
		Display.sync(60);
	}
	
	public static void clear(){
		glClear(GL_COLOR_BUFFER_BIT);
	}
	
	public static void destroy(){
		Keyboard.destroy();
		Display.destroy();
		System.exit(0);
	}
}
