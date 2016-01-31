package de.mih.core.engine.ai.navigation.pathfinder.PathGenerator;

import com.badlogic.gdx.math.Vector2;

public interface Node {

	public float g = 0f, f = 0f;

	public abstract float getDistance(Node target);

	public abstract Vector2 getPos();

}
