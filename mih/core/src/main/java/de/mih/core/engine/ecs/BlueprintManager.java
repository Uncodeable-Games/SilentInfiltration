package de.mih.core.engine.ecs;

import java.util.HashMap;
import java.util.Map;

import de.mih.core.engine.io.Blueprints.EntityBlueprint;

/**
 * The BlueprintManager reads blueprints from XML and stores them internally.
 * With the stored blueprints the manager creates new entities.
 * 
 * @author Tobias
 */
public class BlueprintManager
{

	Map<String, EntityBlueprint> blueprints = new HashMap<>();

	private EntityManager entityManager;

	public BlueprintManager(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}



	public int createEntityFromBlueprint(String name)
	{
		return this.blueprints.get(name).generateEntity();
	}

	public int createEntityFromBlueprint(String name, int entityId)
	{
		return this.blueprints.get(name).generateEntity(entityId);
	}
}
