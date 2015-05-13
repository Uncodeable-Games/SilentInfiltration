package de.silentinfiltration.engine.ecs;

public class Event<T> {
	public int entityID;
	public String eventType;
	public T message;
}
