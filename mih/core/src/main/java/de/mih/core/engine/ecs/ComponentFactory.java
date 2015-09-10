package de.mih.core.engine.ecs;

import java.util.Map;

public abstract class ComponentFactory {
	public abstract Component componentFromType(String type, Map<String,String> fields);
}
