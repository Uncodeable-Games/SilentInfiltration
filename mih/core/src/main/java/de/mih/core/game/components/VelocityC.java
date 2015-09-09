package de.mih.core.game.components;

import de.mih.core.engine.ecs.Component;
import de.mih.core.engine.io.ComponentParser;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.badlogic.gdx.math.Vector3;

public class VelocityC extends Component implements ComponentParser {
	static
	{
		ComponentParser.addComponentParser("velocity", new ComponentParser() 
		{
			@Override
			public Component parseXML(Node node) 
			{
				VelocityC tmp = new VelocityC(Vector3.Zero);
				NodeList attr = node.getChildNodes();
				for (int j = 0; j < attr.getLength(); j++) {
					Node a = attr.item(j);
					switch (a.getNodeName()) {

					case "drag":
						tmp.drag = Float.parseFloat(a.getTextContent());
						break;

					case "maxspeed":
						tmp.maxspeed = Float.parseFloat(a.getTextContent());
						break;

					}
				}
				return tmp;
			}
		});
	}
	
	public Vector3 velocity;
	public float drag = 1;
	public float maxspeed = 0.75f;
	
	public VelocityC(Vector3 velocity){
		this.velocity = velocity;
	}

	@Override
	public Component parseXML(Node node) {
		VelocityC tmp = new VelocityC(Vector3.Zero);
		NodeList attr = node.getChildNodes();
		for (int j = 0; j < attr.getLength(); j++) {
			Node a = attr.item(j);
			switch (a.getNodeName()) {

			case "drag":
				tmp.drag = Float.parseFloat(a.getTextContent());
				break;

			case "maxspeed":
				tmp.maxspeed = Float.parseFloat(a.getTextContent());
				break;

			}
		}
		return tmp;
	}
	
}
