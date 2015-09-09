package de.mih.core.engine.ecs;

import java.util.HashMap;

public class EntityBlueprint {
	String name;
	
	HashMap<Class<? extends Component>,Component> concreteComponents = new HashMap<>();
	
	public EntityBlueprint(String name)
	{
		this.name = name;
	}
	
	
	public int generateEntity() {
		int entityId = EntityManager.getInstance().createEntity();
		return generateEntity(entityId);
		
	}
	
	public int generateEntity(int entityId)
	{
		for(Class<? extends Component> cType : concreteComponents.keySet())
		{
			Component c = concreteComponents.get(cType).cpy();
			EntityManager.getInstance().addComponent(entityId, c);
		}
		return entityId;
	}



	public void addComponent(Component component) {
		this.concreteComponents.put(component.getClass(), component);		
	}
}
