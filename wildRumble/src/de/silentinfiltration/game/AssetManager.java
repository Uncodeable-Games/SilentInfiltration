package de.silentinfiltration.game;

import static org.lwjgl.opengl.GL11.GL_NEAREST;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import de.silentinfiltration.engine.render.Sprite;

public class AssetManager {
	HashMap<String, Sprite> textureStore = new HashMap<>();
	
	public void loadTexture(String name, String path, String format, Vector2f imageSize) throws IOException{
		Texture tex = TextureLoader.getTexture(format,
				ResourceLoader.getResourceAsStream(path), GL_NEAREST);
		//TODO: finish
		Sprite sprite = new Sprite(tex,imageSize);
		textureStore.put(name, sprite);
	}
	
	public Sprite getTexture(String name)
	{
		return textureStore.get(name);
	}
	
//	TextureLoader.getTexture("PNG",
//			ResourceLoader.getResourceAsStream(imgpath), GL_NEAREST);
}
