package de.mih.core.engine.ecs;

import java.util.HashMap;

public class EntityBlueprint {
	String name;
	
	HashMap<Class<? extends Component>,Component> concreteComponents = new HashMap<>();
	EntityManager entityManager;
	
	public EntityBlueprint(EntityManager entityManager, String name)
	{
		this.entityManager = entityManager;
		this.name = name;
	}
	
	
	public int generateEntity() {
		return generateEntity(this.entityManager.createEntity());
		
	}
	
	public int generateEntity(int entityId)
	{
		for(Class<? extends Component> cType : concreteComponents.keySet())
		{
			Component c = concreteComponents.get(cType).cpy();
			this.entityManager.addComponent(entityId, c);
		}
		for(Class<? extends Component> cType : concreteComponents.keySet())
		{
			this.entityManager.getComponent(entityId, cType).init();
		}
		return entityId;
	}



	public void addComponent(Component component) {
		this.concreteComponents.put(component.getClass(), component);		
	}
}
