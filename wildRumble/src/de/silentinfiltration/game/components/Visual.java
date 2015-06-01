package de.silentinfiltration.game.components;

import static org.lwjgl.opengl.GL11.GL_NEAREST;

import java.io.IOException;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import de.silentinfiltration.engine.ecs.Component;
import de.silentinfiltration.engine.render.Sprite;

public class Visual extends Component {

//	public Texture tex; 
//	//public String imgpath;
//	public Vector2f image_size = new Vector2f(16,16);
//	
	public Sprite sprite;
//	public Visual(String path){
//		imgpath = path;
//		//TODO: move TextureLoader into own loading class "asset manager"?
//		try {
//			tex = TextureLoader.getTexture("PNG",
//					ResourceLoader.getResourceAsStream(imgpath), GL_NEAREST);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	public Visual(Sprite sprite)
	{
		this.sprite = sprite;
	}
	
	
}
