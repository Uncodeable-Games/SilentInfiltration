package de.mih.core.engine.ai.gob;

import java.util.Collection;
import java.util.HashMap;

public class GobState {
	public Discontentment disc;
	protected HashMap<String, ItemComponent> possessedItems = new HashMap<>();
	
	public GobState() { 
		disc = new Discontentment();}
	public GobState(GobState copy) {
		disc = new Discontentment(copy.disc);
		possessedItems = (HashMap<String, ItemComponent>) copy.possessedItems.clone();
	}
	
	public void addItem(ItemComponent item)
	{
		possessedItems.put(item.itemName, item);
	}
	
	public ItemComponent getItem(String name)
	{
		return possessedItems.get(name);
	}
	
	public void addItems(Collection<ItemComponent> items)
	{
		for(ItemComponent item : items)
		{
			possessedItems.put(item.itemName, item);
		}
	}
	
	public void removeAll(Collection<ItemComponent> items)
	{
		for(ItemComponent item : items)
		{
			this.removeItem(item);
		}
	}
	public Collection<ItemComponent> getItems()
	{
		return possessedItems.values();
	}
	public void removeItem(ItemComponent item) {
		if(possessedItems.containsKey(item.itemName))
			possessedItems.remove(item.itemName);
	}
}
