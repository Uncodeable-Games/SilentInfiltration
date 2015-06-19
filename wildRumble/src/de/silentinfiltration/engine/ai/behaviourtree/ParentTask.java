package de.silentinfiltration.engine.ai.behaviourtree;

import de.silentinfiltration.engine.exceptions.ComponentNotFoundEx;

public abstract class ParentTask extends Task {

	ParentTaskController control;

	public ParentTask(Blackboard bb) {
		super(bb);
		CreateController();
	}

	private void CreateController() {
		this.control = new ParentTaskController(this);
	}

	public TaskController GetControl() {
		return control;
	}

	public boolean CheckConditions() {
		return control.subtasks.size() > 0;
	}

	public abstract void ChildSucceeded();

	public abstract void ChildFailed();

	public void DoAction(double dt) throws ComponentNotFoundEx{
		if (control.Finished()) {

			return;
		}
		if (control.curTask == null) {

			return;
		}

		if (!control.curTask.GetControl().Started()) {

			control.curTask.GetControl().SafeStart();
		} else if (control.curTask.GetControl().Finished()) {

			control.curTask.GetControl().SafeEnd();
			if (control.curTask.GetControl().Succeeded()) {
				this.ChildSucceeded();
			}
			if (control.curTask.GetControl().Failed()) {
				this.ChildFailed();
			}
		} else {
			control.curTask.DoAction(dt);
		}
	}

	public void End() {
	}

	public void Start() {
		control.curTask = control.subtasks.firstElement();
		if (control.curTask == null) {
		}
	}
}
