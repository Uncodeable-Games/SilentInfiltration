package de.mih.core.game.player;

import java.util.ArrayList;

import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.game.MiH;

public class Player {
	
	EntityManager entityM;
	
	public String name;
	public int id;
	
	
	public static ArrayList<Integer> selectedunits = new ArrayList<Integer>();
	
	public Player(String n,int i, EntityManager em) {
		name = n;
		id = i;
		
	}
	
	public void selectUnit(int entity){
		selectedunits.add(entity);
	
	}
}
