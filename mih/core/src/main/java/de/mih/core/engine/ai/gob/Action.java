package de.mih.core.engine.ai.gob;

import java.util.ArrayList;
import java.util.Collection;

public class Action
{
	String name;
	double time;
	boolean isStarted = false;
	boolean isFinished = false;
	Discontentment disc;
	ArrayList<ItemComponent> usedItems;
	ArrayList<ItemComponent> generatedItems;

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

	public void reset()
	{
		isStarted = false;
		isFinished = false;
	}

	public String getName()
	{
		return name;
	}

	public double getTime()
	{
		return time;
	}

	public void start()
	{
		isStarted = true;
	}

	public void finish()
	{
		isFinished = true;
	}

	public boolean isStarted()
	{
		return isStarted;
	}

	public boolean isFinished()
	{
		return isFinished;
	}

	public void apply(GobState state, Discontentment changesPerTimeStep)
	{
		for (Discontentment.goalNames goal : Discontentment.goalNames.values())
		{
			state.disc.addGoal(goal, changesPerTimeStep.getGoal(goal) * time + disc.getGoal(goal));
		}
		state.disc.clamp();

		state.removeAll(usedItems);
		
		for (ItemComponent item : generatedItems)
		{
			state.addItem(item);
		}

	}

	public Action clone()
	{
		Action clone = new Action(name, time, disc);
		clone.usedItems = (ArrayList<ItemComponent>) this.usedItems.clone();
		clone.generatedItems = (ArrayList<ItemComponent>) this.usedItems.clone();
		return clone;
	}

	@Override
	public String toString()
	{
		return this.name;
	}
}
