package de.mih.core.game.components;

import de.mih.core.engine.ecs.component.Component;
import de.mih.core.levedit.Entities.Abstract.Editable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tobias on 12.04.2016.
 */
public class AbilityC extends Component
{
	@Editable("List of AbilityIds")
	private List<Integer> abilityIdList = new ArrayList<>();

	public AbilityC(){}

	public AbilityC(AbilityC abilityC){
		for (Integer i : abilityC.abilityIdList){
			this.abilityIdList.add(i);
		}
	}

	public List<Integer> getAbilityIdList()
	{
		return abilityIdList;
	}
}
