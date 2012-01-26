package com.todo.data;

/**
 * This interface extends {@link ToDoDataProvider}
 * and adds some additional features which all testable providers 
 * should maintain
 * @author Mikalai
 *
 */
public interface TestableToDoDataProvider extends ToDoDataProvider{
	
	/**
	 * method for deletion of all users
	 */
	public void deleteAllUsers();
	/**
	 * method for deletion of all tasks
	 */
	public void deleteAllTasks();
	/**
	 * method for deletion of all folders
	 */
	public void deleteAllFolders();
	/**
	 * method for deletion of all priorities
	 */
	public void deleteAllPriorities();
	
}
