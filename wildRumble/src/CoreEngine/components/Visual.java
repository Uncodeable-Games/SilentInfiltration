package CoreEngine.components;

import static org.lwjgl.opengl.GL11.GL_NEAREST;

import java.io.IOException;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import CoreEngine.ecs.Component;

public class Visual extends Component {

	public Texture tex;
	public String imgpath;
	public Vector2f image_size = new Vector2f(32, 32);
	
	public Visual(String path){
		imgpath = path;
		try {
			tex = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream(imgpath), GL_NEAREST);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}