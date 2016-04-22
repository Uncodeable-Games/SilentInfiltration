package de.mih.core.engine.io.Blueprints.Tilemap;

import de.mih.core.engine.io.Blueprints.Blueprint;
import de.mih.core.engine.tilemap.Tile;
import de.mih.core.engine.tilemap.TileBorder;

/**
 * Created by Cataract on 21.04.2016.
 */
public class TileBorderBlueprint implements Blueprint
{
	public static class Border{
		private int x = -1,y = -1;
		private Tile.Direction direction;

		public Border(){}

		public Border(int x, int y, Tile.Direction direction)
		{
			this.x = x;
			this.y = y;
			this.direction = direction;
		}

		public int getX()
		{
			return x;
		}

		public int getY()
		{
			return y;
		}

		public Tile.Direction getDirection()
		{
			return direction;
		}
	}

	private Border[] borders = new Border[2];

	private String collider;

	private String texture1, texture2;

	public TileBorderBlueprint(){}

	public TileBorderBlueprint(TileBorder tileBorder)
	{
		this.collider = tileBorder.getBlueprint();
		Tile t =tileBorder.getAdjacentTiles().get(0);
		borders[0] = new Border(t.getX(),t.getY(),t.getDirection(tileBorder));
		if (tileBorder.getAdjacentTiles().size() > 1)
		{
			t = tileBorder.getAdjacentTiles().get(1);
			borders[1] = new Border(t.getX(), t.getY(), t.getDirection(tileBorder));
		}
		this.texture1 = tileBorder.getTexture1();
		this.texture2 = tileBorder.getTexture2();
	}

	public Border[] getBorders()
	{
		return borders;
	}

	public String getCollider()
	{
		return collider;
	}

	public String getTexture1()
	{
		return texture1;
	}

	public String getTexture2()
	{
		return texture2;
	}
}
