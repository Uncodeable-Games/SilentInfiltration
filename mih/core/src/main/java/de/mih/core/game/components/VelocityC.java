package de.mih.core.game.components;

import com.badlogic.gdx.math.Vector3;
import de.mih.core.engine.ecs.component.Component;
import de.mih.core.game.steering.Steering;

public class VelocityC extends Component
{
	public final static String name = "velocity";

	public Vector3 velocity;
	public Vector3 steering;
	
	public Steering steerBehaviour;
	
	public float drag     = 1;
	public float maxspeed = 0.75f;

	public VelocityC()
	{
		this(new Vector3());
	}

	public VelocityC(VelocityC velocityC){
		this(velocityC.velocity.cpy());
		this.steerBehaviour = velocityC.steerBehaviour;
		this.drag = velocityC.drag;
		this.maxspeed = velocityC.maxspeed;
	}

	public VelocityC(Vector3 velocity)
	{
		this.velocity = velocity;
		this.steering = new Vector3();
	}
}
