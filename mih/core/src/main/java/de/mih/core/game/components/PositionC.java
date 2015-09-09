package de.mih.core.game.components;

import de.mih.core.engine.ecs.Component;
import de.mih.core.engine.io.ComponentParser;

import java.util.StringTokenizer;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.badlogic.gdx.math.Vector3;

public class PositionC extends Component {
	static
	{
		ComponentParser.addComponentParser("position", new ComponentParser() 
		{
			@Override
			public Component parseXML(Node node) {
				PositionC tmp = new PositionC(Vector3.Zero);
				NodeList attr = node.getChildNodes();
				for (int j = 0; j < attr.getLength(); j++) {
					Node a = attr.item(j);
					switch (a.getNodeName()) {

					case "position":
						StringTokenizer tokenizer = new StringTokenizer(a.getTextContent(), ",");
						tmp.position.x = Float.parseFloat(tokenizer.nextToken());
						tmp.position.y = Float.parseFloat(tokenizer.nextToken());
						tmp.position.z = Float.parseFloat(tokenizer.nextToken());
						break;

					case "angle":
						tmp.angle = Integer.parseInt(a.getTextContent());
						break;

					}
				}
				return tmp;
			
			}
		});
		
	}
	public Vector3 position;
	public int angle;
	
	public PositionC(Vector3 position){
		this.position = position;
	}
}
