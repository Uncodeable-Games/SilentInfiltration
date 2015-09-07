package de.mih.core.game.components;

import de.mih.core.engine.ecs.Component;
import com.badlogic.gdx.math.Circle;

public class ColliderC extends Component{
	
	public Circle circle = new Circle();
	 
	public ColliderC(VisualC vis) {
		circle.radius = (vis.visual.bounds.getDepth() + vis.visual.bounds.getWidth()) / 2f;
	}
}
