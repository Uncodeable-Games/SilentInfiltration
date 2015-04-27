package Widgetclasses;

import org.lwjgl.util.vector.Vector2f;

public class Player {

	Vector2f cam_pos;
	
	public Player(){
		
	}
	
	public void setCam(Vector2f newcam){
		cam_pos = newcam;
	}
	
}
