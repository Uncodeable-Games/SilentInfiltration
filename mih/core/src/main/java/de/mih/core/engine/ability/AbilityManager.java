package de.mih.core.engine.ability;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tobias on 12.04.2016.
 */
public class AbilityManager
{
	private Map<Integer,Ability>      idMapping     = new HashMap<>();

	public Ability getAbilityById(int id){
		return idMapping.get(id);
	}

	public void addAbility(Ability ability){
		idMapping.put(ability.getId(),ability);
	}
}
