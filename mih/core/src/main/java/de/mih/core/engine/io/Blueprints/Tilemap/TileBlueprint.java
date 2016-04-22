package de.mih.core.engine.io.Blueprints.Tilemap;

/**
 * Created by Cataract on 22.04.2016.
 */
public class TileBlueprint
{
	private int x = -1;
	private int y = -1;

	private String texture;

	public TileBlueprint(){}

	public TileBlueprint(int x, int y, String texture)
	{
		this.x = x;
		this.y = y;
		this.texture = texture;
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}

	public String getTexture()
	{
		return texture;
	}
}
