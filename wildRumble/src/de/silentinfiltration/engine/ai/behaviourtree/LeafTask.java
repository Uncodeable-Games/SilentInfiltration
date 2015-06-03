package de.silentinfiltration.engine.ai.behaviourtree;

public abstract class LeafTask extends Task {

	protected TaskController control;
	
	public LeafTask(Blackboard blackboard) {
		super(blackboard);
		CreateController();
	}
	
	private void CreateController()
	{
		this.control = new TaskController(this);
	}

	public TaskController GetControl()
	{
		return this.control;
	}
	
}
