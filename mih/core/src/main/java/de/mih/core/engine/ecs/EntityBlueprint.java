package de.mih.core.engine.ecs;

import java.util.HashMap;

public class EntityBlueprint {
	EntityManager entityManager;
	
	HashMap<Class<? extends Component>,Component> concreteComponents;
	
	public int generateEntity() throws InstantiationException, IllegalAccessException{
		int entityId = entityManager.createEntity();
		
//		for(Class<? extends Component> cType : concreteComponents.keySet())
//		{
//			Component c = cType.newInstance();
//		}
		
		return entityId;
	}
}
