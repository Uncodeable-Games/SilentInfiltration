package de.mih.core.engine.io.Blueprints;

import de.mih.core.engine.ecs.component.Component;
import de.mih.core.game.GameLogic;
import de.mih.core.game.components.VisualC;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * This class represents a entity blueprint.
 * A blueprint can create multiple entities with the same base data.
 *
 * @author Tobias
 */
public class EntityBlueprint extends ArrayList<Component> implements Blueprint
{
	public int generateEntity()
	{
		return generateEntity(GameLogic.getCurrentGame().getEntityManager().createEntity());
	}

	@SuppressWarnings("rawtypes")
	public int generateEntity(int entityId)
	{
		GameLogic.getCurrentGame().getEntityManager().setMaxEntity(entityId + 1);
		for (Component comp : this)
		{
			if(comp instanceof VisualC && GameLogic.getCurrentGame().getBlueprintManager().isNoGraphics()) continue;
			GameLogic.getCurrentGame().getEntityManager().addComponent(entityId, generateComponent(comp));
		}
		return entityId;
	}

	private <T extends Component> Component generateComponent(T comp)
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
