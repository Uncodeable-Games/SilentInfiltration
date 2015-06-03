package de.silentinfiltration.engine.ai.behaviourtree;

import de.silentinfiltration.engine.exceptions.ComponentNotFoundEx;


public class TaskController {

	private boolean done;

	private boolean sucess;

	private boolean started;

	private Task task;


	public TaskController(Task task) {
		SetTask(task);
		Initialize();
	}


	private void Initialize() {
		this.done = false;
		this.sucess = true;
		this.started = false;
	}


	public void SetTask(Task task) {
		this.task = task;
	}


	public void SafeStart() throws ComponentNotFoundEx {
		this.started = true;
		task.Start();
	}


	public void SafeEnd() throws ComponentNotFoundEx {
		this.done = false;
		this.started = false;
		task.End();
	}


	public void FinishWithSuccess() {
		this.sucess = true;
		this.done = true;
	}


	protected void FinishWithFailure() {
		this.sucess = false;
		this.done = true;
	}

	public boolean Succeeded() {
		return this.sucess;
	}


	public boolean Failed() {
		return !this.sucess;
	}


	public boolean Finished() {
		return this.done;
	}


	public boolean Started() {
		return this.started;
	}


	public void Reset() {
		this.done = false;
	}
}
