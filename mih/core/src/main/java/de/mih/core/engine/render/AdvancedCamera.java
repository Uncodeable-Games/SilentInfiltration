package de.mih.core.engine.render;


import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;


public class AdvancedCamera extends PerspectiveCamera {

	public AdvancedCamera(float fieldOfViewY, float viewportWidth, float viewportHeight) {
		super(fieldOfViewY, viewportWidth, viewportHeight);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
	}

	@Override
	public void update(boolean updateFrustum) {
		// TODO Auto-generated method stub
		
	}

	public Vector3 getCameraTarget(float height) {
		return this.position.cpy()
				.add(this.direction.cpy().scl((height - this.position.y) / (this.direction.y)));
	}
	
	Vector3 pos = new Vector3();

	public boolean isVisible(Visual v) {
		v.model.transform.getTranslation(pos);
		pos.add(v.center);
		return this.frustum.sphereInFrustum(pos, v.radius);
	}

}
