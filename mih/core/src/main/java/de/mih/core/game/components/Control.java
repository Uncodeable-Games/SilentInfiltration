package de.mih.core.game.components;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.mih.core.engine.ecs.component.Component;

public class Control extends Component {
	public final static String name = "control";

	
	int player;
	
	public boolean withmouse = false;
	public boolean withwasd = false;
	public boolean withkeys = false;
	@Override
	public Component cpy() {
		Control tmp = new Control();
		tmp.player = player;
		tmp.withkeys = withkeys;
		tmp.withwasd = withwasd;
		tmp.withmouse = withmouse;
		return tmp;
	}
	
	@Override
	public void setField(String fieldName, String fieldValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
	
	
}
