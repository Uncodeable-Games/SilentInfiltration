package de.mih.core.game.components;

import de.mih.core.engine.ecs.Component;
import de.mih.core.engine.io.ComponentParser;

import java.util.StringTokenizer;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.badlogic.gdx.math.Vector3;

public class PositionC extends Component {
	public final static String name = "position";

	public Vector3 position;
	public int angle;
	
	public PositionC()
	{
		this.position = Vector3.Zero;
	}
	public PositionC(Vector3 position){
		this.position = position;
	}

	@Override
	public Component cpy() {
		PositionC tmp = new PositionC(new Vector3(position.x,position.y,position.z));
		tmp.angle = angle;
		return tmp;
	}
	@Override
	public void setField(String fieldName, String fieldValue) {
		if(fieldName.equals("angle"))
		{
			angle = Integer.parseInt(fieldValue);
		}
	}
}
