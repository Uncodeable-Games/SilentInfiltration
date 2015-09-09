package de.mih.core.game.components;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.mih.core.engine.ecs.Component;
import de.mih.core.engine.io.ComponentParser;

public class SelectableC extends Component {
	static
	{
		ComponentParser.addComponentParser("selectable", new ComponentParser() 
		{
			@Override
			public Component parseXML(Node node) 
			{
				SelectableC tmp = new SelectableC();
				NodeList attr = node.getChildNodes();
				for (int j = 0; j < attr.getLength(); j++) {
					Node a = attr.item(j);
					switch (a.getNodeName()) {

					case "selected":
						tmp.selected = (a.getTextContent().equals("true"));
						break;
					}
				}
				return tmp;
			}
			
		});
	}
	
	public boolean selected = false;	
}
