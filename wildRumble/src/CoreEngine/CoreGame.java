package CoreEngine;




import Widgetclasses.*;


public class CoreGame {
	
	
	public CoreGame(){

		
		
	}

	public void input() {
		Widget.inputall();
		
	}

	long newtime = System.nanoTime();
	long oldtime = System.nanoTime();
	
	public void logic() {
	}

	public void render() {
		Widget.renderall();
	}
	
	public void dispose(){
	}

}
