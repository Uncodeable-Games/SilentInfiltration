package de.mih.core.engine.ecs.component;

import java.util.Map;

public interface ComponentInfo<T extends Component>
{
	public void readFields(Map<String, String> fields);
	public T generateComponent();
}
