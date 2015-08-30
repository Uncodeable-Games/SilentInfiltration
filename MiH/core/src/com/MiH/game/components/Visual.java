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
	
	//Frustrum culling
	public final static BoundingBox bounds = new BoundingBox();
	public final Vector3 center = new Vector3();
	public final Vector3 dimensions = new Vector3();
	public final float radius;
	
	public Visual(Model m_type, RenderSystem rs)
	{
		this.rs = rs;
		this.modeltype = m_type;
		model = new ModelInstance(modeltype);
		
		//Frustrum culling
		model.calculateBoundingBox(bounds);
		bounds.getCenter(center);
		bounds.getDimensions(dimensions);
		radius = dimensions.len() / 2f;
		show();
	}
	
	public void hide(){
		if (rs.allvisuals.contains(this)) rs.allvisuals.remove(this);
	}
	
	public void show(){
		if (!rs.allvisuals.contains(this)) rs.allvisuals.add(this);
	}
	
	public boolean ishidden(){
		return (!rs.allvisuals.contains(this));
	}
	
}
