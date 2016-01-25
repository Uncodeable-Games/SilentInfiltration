package de.mih.core.engine.ecs.events;

import com.badlogic.gdx.math.Vector3;

public class BaseEvent
{
	public class GlobalEvent extends BaseEvent {}
	public class LocalEvent extends BaseEvent { Vector3 position; }

}

