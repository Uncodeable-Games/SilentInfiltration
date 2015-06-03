package de.silentinfiltration.engine.ai.behaviourtree;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import de.silentinfiltration.engine.exceptions.ComponentNotFoundEx;


public class ParentTaskController extends TaskController {

	static List<ParentTask> allTasks = new ArrayList<ParentTask>();
	
	public Vector<Task> subtasks;

	public Task curTask;

	public ParentTaskController(Task task) {
		super(task);
		this.subtasks = new Vector<Task>();
		this.curTask = null;
		allTasks.add((ParentTask) task);
		
	}

	public void Add(Task task) {
		subtasks.add(task);
		
	}


	public void Reset() {
		super.Reset();
		this.curTask = subtasks.firstElement();
	}
	
	public static void updateTasks() throws ComponentNotFoundEx{
		for (ParentTask curTask : allTasks){
			curTask.DoAction();
		}
	}
}
