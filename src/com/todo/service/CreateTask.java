package com.todo.service;

import com.todo.entities.Task;

public class CreateTask {
	private Task task;

	public Task getTask() {
		if (task == null)
			task = new Task();
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

}
