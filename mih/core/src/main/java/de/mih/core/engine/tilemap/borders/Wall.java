package de.mih.core.engine.tilemap.borders;

import com.badlogic.gdx.graphics.g3d.Model;

public class Wall extends BorderCollider{
	static Model model;
	float rotation;
	

	public static Model getModel() {
		return model;
	}


	public static void setModel(Model model) {
		Wall.model = model;
	}


	public float getRotation() {
		return rotation;
	}


	public void setRotation(float rotation) {
		this.rotation = rotation;
	}


}