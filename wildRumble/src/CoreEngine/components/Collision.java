package CoreEngine.components;

import java.util.Vector;

import CoreEngine.ecs.Component;

public class Collision extends Component {

	public boolean iscircle = true;
	public int ccol=0;
	public Vector v = new Vector(0,0);
	
	public Collision(boolean b) {
		iscircle = b;
	}
	
}
