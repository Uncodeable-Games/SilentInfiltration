package de.mih.core.engine.tilemap.borders;

public abstract class BorderColliderFactory {
	public abstract BorderCollider colliderForName(TileBorder border, String name);
}
