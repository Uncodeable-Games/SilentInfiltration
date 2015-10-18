package de.mih.core.engine.ecs.component;

import java.util.Map;

/**
 * Used to convert the xml parsed values into a concrete {@link Component}.
 * @author Tobias
 *
 * @param <T>
 */
public interface ComponentInfo<T extends Component>
{
	public void readFields(Map<String, String> fields);
	public T generateComponent();
}
