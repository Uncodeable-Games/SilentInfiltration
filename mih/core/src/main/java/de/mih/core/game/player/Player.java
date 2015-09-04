package de.mih.core.game.player;

import java.util.ArrayList;

import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.game.MiH;
import de.mih.core.game.components.SelectableC;

public class Player {
	
	EntityManager entityM;
	
	public String name;
	public int id;
	
	
	public static ArrayList<Integer> selectedunits = new ArrayList<Integer>();
	
	public Player(String n,int i, EntityManager em) {
		name = n;
		id = i;
		
	}
	public boolean isSelectionEmpty()
	{
		return selectedunits.isEmpty();
	}
	public void selectUnit(int entity){
		selectedunits.add(entity);
	
	}
	
	public void clearSelection(){
		for(Integer entity : selectedunits)
		{
			if(entityM.hasComponent(entity, SelectableC.class))
			{
				entityM.getComponent(entity, SelectableC.class).selected = false;
			}
		}
		selectedunits.clear();
	}
}
