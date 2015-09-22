package de.mih.core.game.player.inventory;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;

import de.mih.core.engine.ecs.EntityManager;
import de.mih.core.game.components.StatsC;
import de.mih.core.game.player.Interaction;

public class Item {

	public String name;
	public String icon;
	public ArrayList<String> stats = new ArrayList<String>();
	public int stacks;
	
	public Item(String name, String icon, int stacks){
		this.name = name;
		this.icon = icon;
		this.stacks = stacks;
	}
}
