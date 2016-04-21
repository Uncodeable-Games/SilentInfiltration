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
}
