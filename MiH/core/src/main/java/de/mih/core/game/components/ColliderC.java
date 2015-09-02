package de.mih.core.game.components;

import de.mih.core.engine.ecs.Component;
import de.mih.core.engine.ecs.EntityManager;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.sun.javafx.geom.Ellipse2D;

import javafx.scene.shape.Ellipse;

public class ColliderC extends Component{
	
	public Circle circle = new Circle();
	 
	public ColliderC(Visual vis) {
		BoundingBox bb = vis.bounds;
		circle.radius = (bb.getDepth() + bb.getWidth()) / 2f;
	}
}
