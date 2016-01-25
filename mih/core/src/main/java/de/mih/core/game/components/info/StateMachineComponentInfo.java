package de.mih.core.game.components.info;

import java.util.Map;

import de.mih.core.engine.ecs.component.ComponentInfo;
import de.mih.core.engine.io.AdvancedAssetManager;
import de.mih.core.game.components.AttachmentC;
import de.mih.core.game.components.StateMachineComponent;

public class StateMachineComponentInfo implements ComponentInfo<StateMachineComponent>
{

	@Override
	public void readFields(Map<String, String> fields)
	{
		for(String key : fields.keySet())
		{
			System.out.println(key + " " + fields.get(key));
			if(key.equals("guard-observe"))
			{
				
			}
			
		}		
	}
	
//	List<Integer> waypoints = new ArrayList<>();
//
//	int entity = this.entityManager.createEntity();
//	guard = entity;
//	StateMachineComponent guard = new StateMachineComponent();
//	StateMachineComponent patrol = new StateMachineComponent();
//	Observing obState = new Observing(guard, patrol, this);
//
//	sight = obState.sight;
//	obState.setTarget(robo);
//	guard.addState("OBSERVE", obState);
//
//	Patrol patrolState = new Patrol(patrol, this);
//	patrol.addState("PATROL", patrolState);
//
//	patrol.current = patrol.states.get("PATROL");
//	guard.current = guard.states.get("OBSERVE");
//
//	this.entityManager.addComponent(this.guard, new AttachmentC(this.guard));
//	this.entityManager.getComponent(this.guard, AttachmentC.class).addAttachment(1,
//			assetManager.getModelByName("cone"));


	@Override
	public StateMachineComponent generateComponent()
	{
		StateMachineComponent tmp = new StateMachineComponent();
		return tmp;
	}

}
