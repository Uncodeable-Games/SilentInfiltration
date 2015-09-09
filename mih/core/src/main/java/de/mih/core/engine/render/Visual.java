package de.mih.core.engine.render;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class Visual
{
	public Model modeltype;
	public ModelInstance model;
	public int angle;
	public Vector3 pos = new Vector3();
	Vector3 scale = new Vector3(1f,1f,1f); // Do not make this public!
	public BoundingBox bounds = new BoundingBox();
	
	//Frustum Culling
	
	public Vector3 center = new Vector3();
	public Vector3 dimensions = new Vector3();
	public float radius;
	
	public Visual(Model modeltype)
	{
		this.modeltype = modeltype;
		model = new ModelInstance(modeltype);
		model.calculateBoundingBox(bounds);
		bounds.getCenter(center);
		bounds.getDimensions(dimensions);
		radius = dimensions.len() /2f;
	}
	
	
	public void setScale(float x, float y, float z){
		scale.x = x;
		scale.y = y;
		scale.z = z;
		model.transform.scale(x, y, z);
		model.calculateBoundingBox(bounds);
		bounds.mul(model.transform);
		bounds.getDimensions(dimensions);
		radius = dimensions.len() /2f;
	}
	
	public Vector3 getScale(){
		return scale;
	}
	
	
	public Visual(Visual visual)
	{
		this.modeltype = visual.modeltype;
		model = new ModelInstance(modeltype);
		model.calculateBoundingBox(bounds);
		bounds.getCenter(center);
		bounds.getDimensions(dimensions);
		radius = dimensions.len() /2f;
		
		this.scale = new Vector3(visual.scale);
		
	}
}
