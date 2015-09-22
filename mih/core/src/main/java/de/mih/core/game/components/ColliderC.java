package de.mih.core.game.components;

import org.w3c.dom.Node;

import com.badlogic.gdx.math.Circle;

import de.mih.core.engine.ecs.Component;
import de.mih.core.engine.io.ComponentParser;

public class ColliderC extends Component{
	
	public final static String name = "collider";
	
	public Circle circle = new Circle();
	
	public ColliderC(){}
	
	public ColliderC(VisualC vis) {
		circle.radius = (vis.visual.bounds.getDepth() + vis.visual.bounds.getWidth()) / 2f;
	}
	
	public ColliderC(float radius)
	{
		this.circle.radius = radius;
	}

	@Override
	public Component cpy() {
		return new ColliderC(circle.radius);
	}

	@Override
	public void setField(String fieldName, String fieldValue) {
		//if()
	}


}
