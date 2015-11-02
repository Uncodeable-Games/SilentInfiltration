package de.mih.core.engine.ecs.events;

import com.badlogic.gdx.math.Vector3;

public class BaseEvent
{
	public static GlobalEvent newGlobalEvent(String message)
	{
		return new GlobalEvent(message);
	}
	public static class GlobalEvent extends BaseEvent { 
		public GlobalEvent(String message)
		{
			this.message = message;
		}
		
		public String toString()
		{
			return super.toString() + " : " + message;
		}
		public String message;}
	public class LocalEvent extends BaseEvent { Vector3 position; }

}

