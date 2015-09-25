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
		return generateEntity(EntityManager.getInstance().createEntity());
		
	}
	
	public int generateEntity(int entityId)
	{
		for(Class<? extends Component> cType : concreteComponents.keySet())
		{
			Component c = concreteComponents.get(cType).cpy();
			EntityManager.getInstance().addComponent(entityId, c);
		}
		for(Class<? extends Component> cType : concreteComponents.keySet())
		{
			EntityManager.getInstance().getComponent(entityId, cType).init();
		}
		return entityId;
	}



	public void addComponent(Component component) {
		this.concreteComponents.put(component.getClass(), component);		
	}
}
