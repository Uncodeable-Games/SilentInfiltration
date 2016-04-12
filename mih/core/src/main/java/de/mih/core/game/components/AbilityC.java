package de.mih.core.game.components;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tobias on 12.04.2016.
 */
public class AbilityC
{
	private List<Integer> abilityIdList = new ArrayList<>();

	AbilityC(){}

	AbilityC(AbilityC abilityC){
		for (Integer i : abilityC.abilityIdList){
			this.abilityIdList.add(i);
		}
	}
}
