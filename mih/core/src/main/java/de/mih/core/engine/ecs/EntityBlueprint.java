package de.mih.core.engine.ecs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.mih.core.engine.ecs.component.Component;
import de.mih.core.engine.ecs.component.ComponentInfo;

public class EntityBlueprint {
	String name;
	
	List<ComponentInfo> components = new ArrayList<>();
	EntityManager entityManager;
	
	public EntityBlueprint(EntityManager entityManager, String name)
	{
		this.entityManager = entityManager;
		this.name = name;
	}
	
	
	public int generateEntity() {
		return generateEntity(this.entityManager.createEntity());
		
	}
	
	@SuppressWarnings("rawtypes")
	public int generateEntity(int entityId)
	{
		for(ComponentInfo componentInfo : components)
		{
			this.entityManager.addComponent(entityId, componentInfo.generateComponent());
		}
		return entityId;
	}


	public void addComponentInfo(@SuppressWarnings("rawtypes") ComponentInfo componenInfo)
	{
		this.components.add(componenInfo);
	}
}
