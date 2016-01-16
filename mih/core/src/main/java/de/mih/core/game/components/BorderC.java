package de.mih.core.game.components;

import java.util.ArrayList;

import de.mih.core.engine.ai.navigation.NavPoint;
import de.mih.core.engine.ecs.component.Component;

public class BorderC extends Component {
	public ArrayList<NavPoint> navpoints = new ArrayList<NavPoint>();
	public boolean isDoor = false;
	
	 // Only important if isDoor is true
	public boolean checked = false;
	public boolean isclosed = false;
	
	public BorderC(boolean isDoor){
		this.isDoor = isDoor;
	}
}
