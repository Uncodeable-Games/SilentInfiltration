package de.mih.core.engine.ecs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.mih.core.engine.ecs.component.Component;
import de.mih.core.engine.ecs.component.ComponentInfo;

public class EntityBlueprint {
	String name;
	
	//HashMap<Class<? extends Component>,Component> concreteComponents = new HashMap<>();
	List<ComponentInfo> components = new ArrayList<>();
	//Hashmap<>
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


	public void addComponentInfo(ComponentInfo componenInfo)
	{
		this.components.add(componenInfo);
	}
//	public void addComponent(Component component) {
//		this.concreteComponents.put(component.getClass(), component);		
//	}
}
