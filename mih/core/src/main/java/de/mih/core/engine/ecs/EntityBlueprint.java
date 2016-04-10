package de.mih.core.engine.ecs;

import de.mih.core.engine.ecs.component.Component;
import de.mih.core.game.Game;

import java.io.*;
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
			try
			{
				Game.getCurrentGame().getEntityManager().addComponent(entityId, cloneComponent(comp));
				Game.getCurrentGame().getEntityManager().getComponent(entityId, comp.getClass()).Init();
			} catch (IOException | ClassNotFoundException e)
			{
				e.printStackTrace();
			}
		}
		return entityId;
	}

	private <T> T cloneComponent(T obj) throws IOException, ClassNotFoundException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		new ObjectOutputStream(baos).writeObject(obj);

		ByteArrayInputStream bais =
				new ByteArrayInputStream(baos.toByteArray());

		T out = null;
		out = (T) new ObjectInputStream(bais).readObject();
		return out;
	}
}
