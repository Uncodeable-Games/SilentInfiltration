package de.mih.core.engine.render;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

import de.mih.core.game.components.VisualC;

public class AdvancedCamera extends PerspectiveCamera {

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

	
	Ray m_target = new Ray();



	
	Vector3 pos = new Vector3();

	public boolean isVisible(VisualC v) {
		v.model.transform.getTranslation(pos);
		pos.add(v.center);
		return this.frustum.sphereInFrustum(pos, v.radius);
	}
}
