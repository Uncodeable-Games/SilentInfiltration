package de.mih.core.game.components;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.badlogic.gdx.math.Vector3;

import de.mih.core.engine.ecs.component.Component;

public class VelocityC extends Component
{
	public final static String name = "velocity";

	public Vector3 velocity;
	public Vector3 steering;
	public float drag = 1;
	public float maxspeed = 0.75f;

	public VelocityC()
	{
		this(new Vector3());
	}

	public VelocityC(Vector3 velocity)
	{
		this.velocity = velocity;
		this.steering = new Vector3();
	}

}
