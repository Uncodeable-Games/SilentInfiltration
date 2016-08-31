package de.mih.core.engine.ai.gob;

import java.util.ArrayList;
import java.util.Collection;

public class Action {
	String name;
	double time;
	boolean isStarted = false;
	boolean isFinished = false;
	Discontentment disc;
	ArrayList<ItemComponent> usedItems;
	ArrayList<ItemComponent> generatedItems;
	
	//TODO: merken, bewegung zu objekt, kann auch eine dynamische action sein
	// d.h. wenn sich ein entity irgendwo hinbewegt (item spender bzw)
	// muss diese action jedesmal neu verfügbar gemacht werden
	public Action(String name, double time, Discontentment disc)
	{
		this.name = name;
		this.time = time;
		this.disc = disc;
		usedItems = new ArrayList<>();
		generatedItems = new ArrayList<>();
	}
	
	public void addUsedItem(ItemComponent item)
	{
		usedItems.add(item);
	}
	
	public void addGeneratedItem(ItemComponent item)
	{
		generatedItems.add(item);
	}
	
	public String getName() { return name; }
	public double getTime() { return time; }	
	
	public void start()
	{
		isStarted = true;
	}
	
	public void finish()
	{
		isFinished = true;
	}
	
	public boolean isStarted() { return isStarted; }
	
	public boolean isFinished() { return isFinished; }
	
	public void apply(GobState state, Discontentment changesPerTimeStep)
	{
		for(Discontentment.goalNames goal : Discontentment.goalNames.values())
		{
			state.disc.addGoal(goal, changesPerTimeStep.getGoal(goal) * time + disc.getGoal(goal));
		}
		state.disc.clamp();
		//TODO manipulate items
		state.removeAll(usedItems);
		state.addItems((Collection<ItemComponent>) generatedItems.clone());
	}
	
	public Action clone()
	{
		Action clone = new Action(name,time,disc);
		clone.usedItems = this.usedItems;
		clone.generatedItems = this.usedItems;
		return clone;
	}
}
