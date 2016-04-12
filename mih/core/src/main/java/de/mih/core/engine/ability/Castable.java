package de.mih.core.engine.ability;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Tobias on 12.04.2016.
 */
public interface Castable{
	default void noTarget(int caster)
	{

	}

	default void onTarget(int caster, int targetId)
	{

	}

	default void onPoint(int caster, Vector2 target)
	{

	}
}
