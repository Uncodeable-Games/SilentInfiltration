package de.mih.core.game.components;

import com.badlogic.gdx.math.Vector3;
import de.mih.core.engine.ecs.component.Component;

public class PositionC extends Component
{

	private Vector3 position = new Vector3();
	private Vector3 facing   = new Vector3();
	private float   angle    = 0;
	
	public PositionC()
	{
	}

	public PositionC(PositionC positionC)
	{
		this(positionC.position);
		this.facing = positionC.facing.cpy();
	}

	public PositionC(Vector3 position)
	{
		setPos(position);
	}
	
	public void setPos(Vector3 vec)
	{
		setPos(vec.x, vec.y, vec.z);
	}
	
	public void setPos(float x, float y, float z)
	{
		position.x = x;
		position.y = y;
		position.z = z;
	}

	public Vector3 getPos()
	{
		return position;
	}
	
	public float getX()
	{
		return position.x;
	}
	
	public float getY()
	{
		return position.y;
	}
	
	public float getZ()
	{
		return position.z;
	}
	
	public void setX(float x)
	{
		setPos(x, getY(), getZ());
	}

	public void setY(float y)
	{
		setPos(getX(), y, getZ());
	}

	public void setZ(float z)
	{
		setPos(getX(), getY(), z);
	}

	public Vector3 getFacing()
	{
		return facing;
	}

	public void setFacing(Vector3 facing)
	{
		this.facing = facing;
	}

	public void setFacing(float x, float y, float z){
		facing.set(x, y, z);
	}

	public void setAngle(float angle)
	{
		this.angle = angle;
	}
	
	public float getAngle()
	{
		return this.angle;
	}
}
