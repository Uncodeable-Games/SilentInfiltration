package de.mih.core.engine.physic;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;

public class Line
{
	public Vector2 from, to;
	
	public Line(Vector2 from, Vector2 to)
	{
		this.from = from;
		this.to = to;
	}
	
	public boolean intersects(Line line)
	{
		return intersects(line, new Vector2());
	}
	
	public boolean intersects(Line line, Vector2 intersection)
	{
		//schneiden sich die geraden?
		float scalar = Intersector.intersectRayRay(from, to.cpy().nor(), line.from, line.to.cpy().nor());
		if(scalar != Float.POSITIVE_INFINITY)
		{

			float r1 = scalar / to.cpy().sub(from).len();
			float r2 = scalar / line.to.cpy().sub(line.from).len();

			if(r1 >= 0 && r1 <= 1 && r2 >= 0 && r2 <= 1) //Liegt der schnittpunkt auf den strecken?
			{
				//System.out.println(f1 + " " + f2 + " : " + scalar);
				return true;
			}
		}
		return false;
	}
}
