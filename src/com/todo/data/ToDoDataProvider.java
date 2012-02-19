package com.todo.data;

import java.util.ArrayList;

import com.todo.entities.*;

public interface ToDoDataProvider{

	/**
	 * create {@link Task} and return id
	 * @param task task to create
	 * @return generated id
	 */
	public int createTask(Task task);

	/**
	 * get task by id
	 * @param id task id
	 * @return {@link Task}
	 */
	public Task getTaskById(int id);
	
	/**
	 * get all sub tasks of some folder
	 * @param folder_id folder id
	 * @return Collection of {@link Task}
	 */
	public ArrayList<Task> getSubTasks(int folder_id);

	/**
	 * update task
	 * @param task - {@link Task} instance
	 */
	public void updateTask(Task task);

	/**
	 * delete task by id
	 * @param id id of {@link Task} to delete
	 */
	public void deleteTask(int id);
	
	/**
	 * create folder
	 * @param folder {@link Folder} instance
	 * @return
	 */
	public int createFolder(Folder folder);

	/**
	 * get {@link Folder} by id 
	 * @param id id of folder
	 * @return {@link Folder} instance
	 */
	public Folder getFolderById(int id);

	/**
	 * get all sub folders of some folder
	 * @param parent_id id of parent folder
	 * @param user_d id of parent folder
	 * @return collection of {@link Folder}
	 */
	public ArrayList<Folder> getSubFolders(int parent_id, int user_id);

	/**
	 * update Folder
	 * @param folder {@link Folder} instance
	 */
	public void updateFolder(Folder folder);

	/**
	 * delete {@link Folder} by id
	 * @param id id of folder
	 */
	public void deleteFolder(int id);

	/**
	 * Create {@link User}
	 * @param user {@link User} instance
	 * @return generated id
	 */
	public int createUser(User user);

	/**
	 * get user by id
	 * @param id user id
	 * @return {@link User} instance
	 */
	public User getUserById(int id);

	/**
	 * get user by name. used in security
	 * @param name user name
	 * @return {@link User} instance
	 */
	public User getUserByName(String name);

	/**
	 * check if user name is available
	 * @param name user name
	 * @return true/false
	 */
	public boolean usernameAvailable(String name);

	/**
	 * check if email is available
	 * @param email email
	 * @return true/false
	 */
	public boolean emailAvailable(String email);

	/**
	 * update user
	 * @param user {@link User} instance to update
	 */
	public void updateUser(User user);

	/**
	 * delete {@link User} by id
	 * @param id id of User
	 */
	public void deleteUser(int id);

	/**
	 * create {@link Priority}
	 * @param priority {@link Priority} instance
	 * @return generated id
	 */
	public int createPriority(Priority priority);

	/**
	 * get {@link Priority} by id
	 * @param id id of priority
	 * @return {@link Priority} instance
	 */
	public Priority getPriorityById(int id);
	
	/**
	 * get all priorities of user
	 * @param user_id id of user
	 * @return collection of {@link Priority}
	 */
	public ArrayList<Priority> getPrioritiesForUser(int user_id);

	/**
	 * update {@link Priority}
	 * @param priority {@link Priority} instance
	 */
	public void updatePriority(Priority priority);
	
	/**
	 * get default {@link Priority} for user (minimal id}
	 * @param user_id user id
	 * @return id of default priority
	 */
	public int getDefaultPriorityIdForUser(int user_id);
	
	/**
	 * change priorities of tasks which contain priority_id to default priority
	 * @param priority_id
	 * @param default_id
	 */
	public void changePriorityToDefault(int priority_id, int default_id);

	/**
	 * delete {@link Priority} by id
	 * @param id priority id
	 */
	public void deletePriority(int id);

}
