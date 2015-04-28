package CoreEngine.components;

import org.lwjgl.util.vector.Vector2f;

import CoreEngine.ecs.Component;

public class PositionC extends Component {
	public Vector2f position;
	public int angle;
	
	public PositionC(Vector2f position){
		this.position = position;
	}
}
