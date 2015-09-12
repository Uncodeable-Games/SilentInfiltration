package de.mih.core.game.tilemap.borders;


import de.mih.core.engine.render.Visual;
import de.mih.core.engine.tilemap.borders.TileBorder;

public class DoorBorder extends WallBorder{
	public static Visual doorVisual;
	
	public DoorBorder()
	{
		setVisual(new Visual(doorVisual));
	}



}