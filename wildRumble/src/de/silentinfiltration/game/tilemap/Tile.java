package de.silentinfiltration.game.tilemap;

import org.lwjgl.util.Rectangle;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.Texture;

public class Tile {
	public Texture tex;
	public Vector2f position;
	//TODO: kann später durch ne body class oder so ersetzt werden
	public Rectangle body;

}
