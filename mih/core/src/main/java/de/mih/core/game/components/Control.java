package de.mih.core.game.components;

import de.mih.core.engine.ecs.Component;

public class Control extends Component {

	int player;
	
	public boolean withmouse = false;
	public boolean withwasd = false;
	public boolean withkeys = false;
	//TODO: add keybindings
}
