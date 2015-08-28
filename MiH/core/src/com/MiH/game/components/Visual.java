package com.MiH.game.components;

import com.MiH.engine.ecs.Component;
import com.MiH.game.systems.RenderSystem;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

public class Visual extends Component {

	public Model modeltype;
	public ModelInstance model;
	public Vector3 pos = new Vector3();
	public int angle;
	
	public Visual(Model m_type, RenderSystem rs)
	{
		this.modeltype = m_type;
		model = new ModelInstance(modeltype);
		rs.allmodels.add(model);
	}
	
	
}
