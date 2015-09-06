package de.mih.core.game.components;

import de.mih.core.engine.ecs.Component;
import de.mih.core.engine.render.Visual;
import de.mih.core.game.systems.RenderSystem;
import com.badlogic.gdx.math.Vector3;

public class VisualC extends Component {

	public RenderSystem rs;
	
	public Visual visual;
	
	public VisualC(String m_type, RenderSystem rs)
	{
		this.visual = new Visual(rs.getModelByName(m_type));
		this.rs = rs;


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
	
	public void setScale(float x, float y, float z){
		visual.setScale(x, y, z);
	}
	
	public Vector3 getScale(){
		return visual.getScale();
	}
	
}
