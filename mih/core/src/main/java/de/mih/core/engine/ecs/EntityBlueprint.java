package de.mih.core.engine.ecs;

import de.mih.core.engine.ecs.component.Component;
import de.mih.core.game.Game;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * This class represents a entity blueprint.
 * A blueprint can create multiple entities with the same base data.
 *
 * @author Tobias
 */
public class EntityBlueprint extends ArrayList<Component>
{
	public int generateEntity()
	{
		return generateEntity(Game.getCurrentGame().getEntityManager().createEntity());
	}

	@SuppressWarnings("rawtypes")
	public int generateEntity(int entityId)
	{
		for (Component comp : this)
		{
			Game.getCurrentGame().getEntityManager().addComponent(entityId, generateComponent(comp));
		}
		return entityId;
	}

	public <T extends Component> Component generateComponent(T comp)
	{
		try
		{
			return comp.getClass().getConstructor(comp.getClass()).newInstance(comp);
		}
		catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e)
		{
			System.out.println("ERROR with component: " + comp + " of type: " + comp.getClass());
			e.printStackTrace();
		}
		return null;
	}
}
