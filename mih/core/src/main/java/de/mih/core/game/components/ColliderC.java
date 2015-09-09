package de.mih.core.game.components;

import org.w3c.dom.Node;

import com.badlogic.gdx.math.Circle;

import de.mih.core.engine.ecs.Component;
import de.mih.core.engine.io.ComponentParser;

public class ColliderC extends Component{
	
//	static
//	{
//		ComponentParser.addComponentParser("collider", new ComponentParser() {
//			
//			@Override
//			public Component parseXML(Node node) {
//				//node.
//			}
//		});
//	}
	
	public Circle circle = new Circle();
	 
	public ColliderC(VisualC vis) {
		circle.radius = (vis.visual.bounds.getDepth() + vis.visual.bounds.getWidth()) / 2f;
	}

}
