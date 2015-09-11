package de.mih.core.game.tilemap.borders;


import de.mih.core.engine.render.Visual;
import de.mih.core.engine.tilemap.borders.TileBorder;

public class Door extends Wall{
	public static Visual doorVisual;


	boolean doorClosed;
	
	public Door()
	{
		setVisual(new Visual(doorVisual));
		this.collider = null;
	}
	
	public boolean isDoorClosed() {
		return doorClosed;
	}


	public void setDoorClosed(boolean doorClosed) {
		this.doorClosed = doorClosed;
	}



}