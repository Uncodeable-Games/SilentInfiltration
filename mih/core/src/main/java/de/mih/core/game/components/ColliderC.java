package de.mih.core.game.components;

import de.mih.core.engine.ecs.component.Component;

public class ColliderC extends Component
{

	public final static float COLLIDER_RADIUS = 0.3f;

	private float width, length = 0;

	public ColliderC()
	{
	}

	public ColliderC(ColliderC colliderC)
	{
		this(colliderC.getWidth(), colliderC.getLength());
	}
	
	public ColliderC(float width, float length)
	{
		this.width = width;
		this.length = length;
	}

	public void setScale(float x, float y)
	{
		this.width *= x;
		this.length *= y;
	}
	
	public float getWidth()
	{
		return width;
	}

	public float getLength()
	{
		return length;
	}

}
