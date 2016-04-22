package de.mih.core.engine.io.Blueprints.Tilemap;

import de.mih.core.engine.io.Blueprints.Blueprint;
import de.mih.core.engine.tilemap.TileBorder;
import de.mih.core.engine.tilemap.Tilemap;

import java.util.ArrayList;

/**
 * Created by Cataract on 21.04.2016.
 */
public class TilemapBlueprint implements Blueprint
{
	private String name;

	private float TILESIZE;
	private int length;
	private int width;

	private ArrayList<TileBorderBlueprint> borders = new ArrayList<>();
	private ArrayList<TileBlueprint> tiles = new ArrayList<>();

	public TilemapBlueprint(){}

	public TilemapBlueprint(Tilemap tilemap)
	{
		this.name = tilemap.getName();
		this.TILESIZE = tilemap.getTILESIZE();
		this.length = tilemap.getLength();
		this.width = tilemap.getWidth();
		for (TileBorder tileBorder : tilemap.getBorders()){
			if (tileBorder.hasCollider()){
				borders.add(new TileBorderBlueprint(tileBorder));
			}
		}
		for (int i = 0; i<tilemap.getWidth();i++){
			for (int k = 0;k<tilemap.getLength();k++){
				tiles.add(new TileBlueprint(i,k,tilemap.getTileAt(i,k).getTexture()));
			}
		}
	}

	public String getName()
	{
		return name;
	}

	public float getTILESIZE()
	{
		return TILESIZE;
	}

	public int getLength()
	{
		return length;
	}

	public int getWidth()
	{
		return width;
	}

	public ArrayList<TileBorderBlueprint> getBorders()
	{
		return borders;
	}

	public ArrayList<TileBlueprint> getTiles()
	{
		return tiles;
	}
}
