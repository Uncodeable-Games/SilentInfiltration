package de.mih.core.engine.tilemap.borders;

import de.mih.core.engine.tilemap.Tile;

public class TileBorder {
	Tile adjacentTile1, adjacentTile2;
	BorderCollider collider;
	
	public class Vertex {}
	Vertex beginn, end;
	
	public void setBorderCollider(BorderCollider collider)
	{
		this.collider = collider;
	}
}