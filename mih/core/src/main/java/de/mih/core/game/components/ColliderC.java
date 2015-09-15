package de.mih.core.game.components;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;

import de.mih.core.engine.ecs.Component;



public class ColliderC extends Component{
	public final static String name = "collider";
	
	public Shape2D collider;
	//public Circle circle = new Circle();
	
	public ColliderC()
	{
		
	}
	
//	public ColliderC(VisualC vis) {
//		circle.radius = (vis.visual.bounds.getDepth() + vis.visual.bounds.getWidth()) / 2f;
//	}
	public ColliderC(Shape2D collider)
	{
		this.collider = collider;
	}
	
//	public ColliderC(float radius)
//	{
//		Circle tmp = new Circle(0,0,radius);
//		tmp.radius = radius;
//		this.collider = tmp;
//	}

	@Override
	public Component cpy() {
		return new ColliderC(this.collider);
	}

	@Override
	public void setField(String fieldName, String fieldValue) {
		//if()
		//System.out.println(fieldName + ": " + fieldValue);
		if(fieldName.equals("rectangle"))
		{
			String[] split = fieldValue.split(",");
			float width = Float.parseFloat(split[0]);
			float height = Float.parseFloat(split[1]);
			Rectangle rect = new Rectangle();
			rect.width = width;
			rect.height = height;
			this.collider = rect;
		}
		else if(fieldName.equals("circle"))
		{
			float radius = Float.parseFloat(fieldValue);
			Circle circle = new Circle();
			circle.radius = radius;
			this.collider = circle;
		}
	}


}
