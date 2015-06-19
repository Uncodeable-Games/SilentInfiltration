package de.silentinfiltration.engine;
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
		
	     // glEnable(GL_TEXTURE_2D);
	      //glDisable(GL_DEPTH_TEST);
		
//			   glShadeModel(GL_SMOOTH);              // Enable Smooth Shading
//			   glClearColor(0.0f, 0.0f, 0.0f, 0.5f); // Black Background
//			   glClearDepth(1.0f);                   // Depth Buffer Setup
//			   glEnable(GL_DEPTH_TEST);              // Enables Depth Testing
//			   glDepthFunc(GL_LEQUAL);               // The Type Of Depth Testing To Do
//			   glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); // Really Nice Perspective Calculations


	      
		glViewport(0,0,Display.getWidth(),Display.getHeight());
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, Display.getWidth(), Display.getHeight(),0, 1, -1);
		//glOrtho(0, Display.getWidth(),Display.getHeight(), 1, -1);

		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
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
