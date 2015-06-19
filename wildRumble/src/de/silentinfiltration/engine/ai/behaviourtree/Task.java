package de.silentinfiltration.engine.ai.behaviourtree;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

import de.silentinfiltration.engine.exceptions.ComponentNotFoundEx;


public abstract class Task {

	protected Blackboard bb;


	public Task(Blackboard blackboard) {
		this.bb = blackboard;
	}


	public abstract boolean CheckConditions();


	public abstract void Start() throws ComponentNotFoundEx;


	public abstract void End() throws ComponentNotFoundEx;


	public abstract void DoAction(double dt) throws ComponentNotFoundEx;

	public abstract TaskController GetControl();
}
