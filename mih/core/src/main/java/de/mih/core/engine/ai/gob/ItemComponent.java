package de.mih.core.engine.ai.gob;

import java.util.ArrayList;

import de.mih.core.engine.ecs.component.Component;

//Item als Komponente sodass es in einem Entity-Component-System genutzt werden kann
public class ItemComponent extends Component implements Comparable<ItemComponent>{
	public String itemName;
	public ArrayList<Action> usableActions = new ArrayList<>();
	
	@Override
	public int compareTo(ItemComponent other)
	{
		return itemName.compareTo(other.itemName);
	}
	
	public String toString() { return itemName; }
}
