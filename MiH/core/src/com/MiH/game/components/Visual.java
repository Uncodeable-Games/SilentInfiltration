package com.MiH.game.components;

import com.MiH.engine.ecs.Component;
import com.MiH.game.systems.RenderSystem;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class Visual extends Component {

	public RenderSystem rs;
	public Model modeltype;
	public ModelInstance model;
	public Vector3 pos = new Vector3();
	public Vector3 scale = new Vector3(1f,1f,1f);
	public int angle;
	
	//Frustum Culling
	final static BoundingBox bounds = new BoundingBox();
	public Vector3 center = new Vector3();
	Vector3 dimensions = new Vector3();
	public final float radius;
	
	
	public Visual(Model m_type, RenderSystem rs)
	{
		this.modeltype = m_type;
		this.rs = rs;
		model = new ModelInstance(modeltype);
		model.calculateBoundingBox(bounds);
		bounds.getCenter(center);
		bounds.getDimensions(dimensions);
		radius = dimensions.len() /2f;
		rs.allvisuals.add(this);
	}
	
	public void onRemove(){
		hide();
	}
	
	public void show(){
		if (ishidden()) rs.allvisuals.add(this);
	}
	
	public void hide(){
		if (!ishidden()) rs.allvisuals.remove(this);
	}
	
	public boolean ishidden(){
		return !rs.allvisuals.contains(this);
	}
	
}
