package de.mih.core.engine.ecs.component;

import java.util.Map;

public abstract class ComponentInfo<T extends Component>
{
	public abstract void readFields(Map<String, String> fields);
	public abstract T generateComponent();
}
