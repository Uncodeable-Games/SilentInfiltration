package de.mih.core.game.components;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.mih.core.engine.ecs.Component;
import de.mih.core.engine.io.ComponentParser;

public class Control extends Component {
	static
	{
		System.out.println("ADD TO PARSER");
		ComponentParser.addComponentParser("control", new ComponentParser() 
		{
			@Override
			public Component parseXML(Node node) {
				Control tmp = new Control();
				NodeList attr = node.getChildNodes();
				for (int j = 0; j < attr.getLength(); j++) {
					Node a = attr.item(j);
					switch (a.getNodeName()) {

					case "withmouse":
						tmp.withmouse = (a.getTextContent().equals("true"));
						break;

					case "withwasd":
						tmp.withwasd = (a.getTextContent().equals("true"));
						break;

					case "withkeys":
						tmp.withkeys = (a.getTextContent().equals("true"));
						break;

					}
				}
				return tmp;
			}
		});
	}
	
	int player;
	
	public boolean withmouse = false;
	public boolean withwasd = false;
	public boolean withkeys = false;
}
