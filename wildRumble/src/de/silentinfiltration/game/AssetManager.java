package de.silentinfiltration.game;

import static org.lwjgl.opengl.GL11.GL_NEAREST;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class AssetManager {
	HashMap<String, Texture> textureStore = new HashMap<String, Texture>();
	
	public void loadTexture(String name, String path, String format) throws IOException{
		textureStore.put(name, TextureLoader.getTexture(format,
				ResourceLoader.getResourceAsStream(path), GL_NEAREST));
	}
	
	public Texture getTexture(String name)
	{
		return textureStore.get(name);
	}
	
//	TextureLoader.getTexture("PNG",
//			ResourceLoader.getResourceAsStream(imgpath), GL_NEAREST);
}
