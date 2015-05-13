package de.silentinfiltration.game.components;

import org.lwjgl.util.Rectangle;
import org.lwjgl.util.vector.Vector2f;

import de.silentinfiltration.engine.ecs.Component;

/**
 * This is our camera component.
 * It should be possible to have multiple cameras but only one that´s active.
 * Every camera is an entity with at least the camera and a position component.
 * @author Tobias
 *
 */
public class CCamera extends Component {
	public boolean active;
	public Rectangle screen;
	public int focus = -1;
	
	public CCamera(Rectangle screen)
	{
		this(screen,false);
	}
	
	public CCamera(Rectangle screen, boolean active)
	{
		this.screen = screen;
		this.active = active;
	}
	
	//depth of view
	//field of view
	//könnte zb ermöglichen 2 kameras nebeneinander auf einem bildschirm, vll nicht nötig
	// aber cool
}
