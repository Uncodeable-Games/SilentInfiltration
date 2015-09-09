package de.mih.core.engine.tilemap.borders;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.Shape2D;

import de.mih.core.engine.tilemap.Tile.Direction;

public abstract class BorderCollider{
	protected TileBorder position;
	Shape2D collider;
	
	
	
	public TileBorder getPosition() {
		return position;
	}

	public void setPosition(TileBorder position) {
		this.position = position;
	}

	public Shape2D getCollider() {
		return collider;
	}

	public void setCollider(Shape2D collider) {
		this.collider = collider;
	}

}