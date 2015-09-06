package de.mih.core.engine.tilemap.borders;

import com.badlogic.gdx.math.Shape2D;

import de.mih.core.engine.tilemap.Tile.Direction;

public abstract class BorderCollider{
	protected TileBorder position;
	Shape2D collider;
	Direction facing;
	
	
	public void setFacing(Direction facing) {
		this.facing = facing;
	}
}