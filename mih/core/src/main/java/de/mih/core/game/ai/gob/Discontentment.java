package de.mih.core.game.ai.gob;

public class Discontentment implements Comparable<Discontentment>{
	public enum goalNames {
		FUN, SLEEP, HUNGER, //TOILET, SANITY
	};
	public static final double MAX = 100;
	public static final double MIN = 0;
	public static final int NUM_GOALS = 5;
	
	double[] goals = new double[NUM_GOALS];
	
	public Discontentment() { };
	public Discontentment(Discontentment copy)
	{
		this.goals = copy.getGoals().clone();
	}
	
	public double getTotal()
	{
		double total = 0;
		for(int i = 0; i < NUM_GOALS; i++)
		{
			total += goals[i] * goals[i];
		}
		return total;
	}
	
	public double[] getGoals()
	{
		return goals;
	}
	
	public void clamp()
	{
		for(goalNames name : goalNames.values())
		{
			double value = goals[name.ordinal()];
			goals[name.ordinal()] = value < MIN ? MIN : value > MAX ? MAX : value;
		}
	}
	
	public void addGoal(Discontentment.goalNames goal, double value)
	{
		int index = goal.ordinal();
		double newValue = goals[index] + value;
		goals[index] = newValue;
	}
	
	public double getGoal(goalNames goal)
	{
		return goals[goal.ordinal()];
	}

	@Override
	public int compareTo(Discontentment other) {
		double otherTotal = other.getTotal();
		double thisTotal = this.getTotal();
		if(otherTotal >= thisTotal)
		{
			return -1;
		}
		else if(thisTotal > otherTotal)
		{
			return 1;
		}
		return 0;
	}
	
	public String toString()
	{
		String result = "disc: ";
		for(goalNames name : goalNames.values())
		{
			result += name.name() + ": " + goals[name.ordinal()] + " ";
		}
		return result;
	}
}
