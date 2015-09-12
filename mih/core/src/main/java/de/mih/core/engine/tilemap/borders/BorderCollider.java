package de.mih.core.engine.tilemap.borders;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.Shape2D;

import de.mih.core.engine.render.Visual;
import de.mih.core.engine.tilemap.Tile.Direction;

@Deprecated
public abstract class BorderCollider{
	protected TileBorder position;
	protected Shape2D collider;
	Visual visual;
	float rotation;
	
//	public BorderCollider(TileBorder border)
//	{
//		this.position = border;
//	}

	public Visual getVisual() {
		return visual;
	}


	public void setVisual(Visual visual) {
		this.visual = visual;
	}


	public float getRotation() {
		return rotation;
	}


	public void setRotation(float rotation) {
		this.rotation = rotation;
	}
	
	public TileBorder getPosition() {
		return position;
	}

	public void setPosition(TileBorder position) {
		this.position = position;
	//	this.visual.center = position.center;
	}

	public Shape2D getCollider() {
		return collider;
	}

	public void setCollider(Shape2D collider) {
		this.collider = collider;
	}
	
	public boolean hasCollistion()
	{
		return collider != null;
	}
	
	public void render()
	{
		visual.model.transform.setToTranslation(position.center.x + visual.pos.x, position.center.y + visual.pos.y, position.center.z + visual.pos.z);
		visual.model.transform.rotate(0f, 1f, 0f, rotation + position.angle);
		visual.model.transform.scale(visual.getScale().x, visual.getScale().y, visual.getScale().z);
	}

}