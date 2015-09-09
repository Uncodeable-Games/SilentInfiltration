package de.mih.core.engine.tilemap.borders;

import com.badlogic.gdx.graphics.g3d.Model;

public class Door extends Wall{
	boolean doorClosed;
	
	
	public boolean isDoorClosed() {
		return doorClosed;
	}


	public void setDoorClosed(boolean doorClosed) {
		this.doorClosed = doorClosed;
	}



}