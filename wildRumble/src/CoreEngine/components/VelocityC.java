package CoreEngine.components;

import org.lwjgl.util.vector.Vector2f;

import CoreEngine.ecs.Component;

public class VelocityC extends Component {
	public Vector2f velocity;
	public float drag = 1;
	
	public VelocityC(Vector2f velocity){
		this.velocity = velocity;
	}
}
