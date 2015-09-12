package de.mih.core.engine.tilemap.borders;

@Deprecated
public abstract class BorderColliderFactory {
	public abstract BorderCollider colliderForName(TileBorder border, String name);
}
