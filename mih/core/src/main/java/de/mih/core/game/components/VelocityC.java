package de.mih.core.game.components;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.badlogic.gdx.math.Vector3;

import de.mih.core.engine.ecs.component.Component;

public class VelocityC extends Component {
	public final static String name = "velocity";

	
	public Vector3 velocity;
	public Vector3 steering;
	public float drag = 1;
	public float maxspeed = 0.75f;
	
	public VelocityC()
	{
		this(new Vector3());
	}
	
	public VelocityC(Vector3 velocity){
		this.velocity = velocity;
		this.steering = new Vector3();
	}

	@Override
	public Component cpy() {
		VelocityC tmp = new VelocityC(new Vector3(velocity));
		tmp.drag = drag;
		tmp.maxspeed = maxspeed;
		tmp.steering = new Vector3(steering);
		return tmp;
	}

	@Override
	public void setField(String fieldName, String fieldValue) {
		switch(fieldName)
		{
		case "drag":
			drag = Float.parseFloat(fieldValue);
			break;
		case "maxspeed":
			maxspeed = Float.parseFloat(fieldValue);
			break;
		}
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	
	
}
