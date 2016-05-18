package de.mih.core.engine.ecs.events;

import com.badlogic.gdx.math.Vector3;

public class BaseEvent
{
	public boolean fromRemote = false;
	public static GlobalEvent newGlobalEvent(String message)
	{
		return new GlobalEvent(message);
	}
	
	public static LocalEvent newLocalEvent(String message, Vector3 position)
	{
		return new LocalEvent(position, message);
	}

	public static class GlobalEvent extends BaseEvent
	{
		public GlobalEvent(String message)
		{
			this.message = message;
		}

		public String toString()
		{
			return super.toString() + " : " + message;
		}

		public String message;
	}

	public static class LocalEvent extends BaseEvent
	{
		Vector3 position;
		String message;
		
		public LocalEvent(Vector3 position, String message)
		{
			this.message = message;
			this.position = position;
		}

		public String toString()
		{
			return super.toString() + " : " + message + " " + position.toString();
		}

	}

}

