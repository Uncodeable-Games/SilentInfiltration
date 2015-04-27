package CoreEngine.components;

import org.lwjgl.util.vector.Vector2f;

import CoreEngine.ecs.Component;

public class PositionC extends Component {
	public Vector2f position;
	public float rotation = 0;
	
	public PositionC(Vector2f position){
		this.position = position;
	}
}
