package de.mih.core.game.components;

import de.mih.core.engine.ecs.Component;
import com.badlogic.gdx.math.Vector3;

public class PositionC extends Component {
	public Vector3 position;
	public int angle;
	
	public PositionC(Vector3 position){
		this.position = position;
	}
}
