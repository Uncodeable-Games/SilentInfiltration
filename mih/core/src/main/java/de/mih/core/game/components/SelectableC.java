package de.mih.core.game.components;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.mih.core.engine.ecs.Component;
import de.mih.core.engine.io.ComponentParser;

public class SelectableC extends Component {
	public final static String name = "selectable";

	
	public boolean selected = false;


	@Override
	public Component cpy() {
		SelectableC tmp = new SelectableC();
		tmp.selected = selected;
		return tmp;
	}


	@Override
	public void setField(String fieldName, String fieldValue) {
		switch(fieldName)
		{
			case "selected":
				selected = Boolean.parseBoolean(fieldValue);
				break;
		}
		
	}	
}
