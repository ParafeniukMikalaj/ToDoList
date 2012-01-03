package com.todo.data;

public interface TestableToDoDataProvider extends ToDoDataProvider{
	public void deleteAllUsers();
	public void deleteAllTasks();
	public void deleteAllFolders();
	public void deleteAllPriorities();
}
