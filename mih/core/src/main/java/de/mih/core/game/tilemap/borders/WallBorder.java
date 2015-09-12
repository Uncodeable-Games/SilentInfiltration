package de.mih.core.game.tilemap.borders;

import com.badlogic.gdx.math.Shape2D;

import de.mih.core.engine.render.Visual;
import de.mih.core.engine.tilemap.borders.BorderCollider;
import de.mih.core.engine.tilemap.borders.TileBorder;

public class WallBorder extends BorderCollider{
	public static Visual wallVisual;
	public WallBorder()
	{
		setVisual(new Visual(wallVisual));
	}

}