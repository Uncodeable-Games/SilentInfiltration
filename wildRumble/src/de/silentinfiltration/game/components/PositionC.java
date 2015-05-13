package de.silentinfiltration.game.components;

import org.lwjgl.util.vector.Vector2f;

import de.silentinfiltration.engine.ecs.Component;

public class PositionC extends Component {
	public Vector2f position;
	public int angle;
	
	public PositionC(Vector2f position){
		this.position = position;
	}
}
