package de.mih.core.game.player;

import java.util.ArrayList;

import com.badlogic.gdx.InputAdapter;

import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.game.MiH;
import de.mih.core.game.components.AttachmentC;
import de.mih.core.game.components.SelectableC;
import de.mih.core.game.input.InGameInput;
import de.mih.core.game.player.input.contextmenu.CircularContextMenu;

public class Player {
	
	EntityManager entityM;
	
	public String name;
	public int id;
	
	CircularContextMenu contextMenu;
	
	public ArrayList<Integer> selectedunits = new ArrayList<Integer>();
	
	public Player(String n,int i, EntityManager em) {
		name = n;
		id = i;
		entityM = em;
		
	}
	public boolean isSelectionEmpty()
	{
		return selectedunits.isEmpty();
	}
	public void selectUnit(int entity){
		selectedunits.add(entity);
		entityM.getComponent(entity, SelectableC.class).selected = true;
	}
	
	public void clearSelection(){
		for(Integer entity : selectedunits)
		{
			if(entityM.hasComponent(entity, SelectableC.class))
			{
				entityM.getComponent(entity, SelectableC.class).selected = false;
			}
			if (entityM.hasComponent(entity, AttachmentC.class)){
				entityM.removeComponent(entity, entityM.getComponent(entity, AttachmentC.class));
			}
		}
		selectedunits.clear();
	}
}
