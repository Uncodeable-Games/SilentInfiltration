package com.MiH.game.components;

import com.MiH.engine.ecs.Component;
import com.badlogic.gdx.math.Vector3;

public class VelocityC extends Component {
	public Vector3 velocity;
	public float drag = 1;
	public float maxspeed = 0.75f;
	
	public VelocityC(Vector3 velocity){
		this.velocity = velocity;
	}
}
