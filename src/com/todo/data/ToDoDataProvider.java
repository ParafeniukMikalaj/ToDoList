package com.todo.data;

import java.util.ArrayList;

import com.todo.entities.*;

public interface ToDoDataProvider{

	public int createTask(Task task);

	public Task getTaskById(int id);
	
	public ArrayList<Task> getSubTasks(int folder_id);

	public ArrayList<Task> getAllTasks();

	public void updateTask(Task task);

	public void deleteTask(int id);

	public int createFolder(Folder folder);

	public Folder getFolderById(int id);

	public ArrayList<Folder> getSubFolders(int parent_id);
	
	public ArrayList<Folder> getAllFolders();

	public void updateFolder(Folder folder);

	public void deleteFolder(int id);

	public int createUser(User user);

	public User getUserById(int id);

	public User getUserByName(String name);

	public boolean usernameAvailable(String name);

	public boolean emailAvailable(String email);

	public ArrayList<User> getAllUsers();

	public void updateUser(User user);

	public void deleteUser(int id);

	public int createPriority(Priority priority);

	public Priority getPriorityById(int id);

	public ArrayList<Priority> getAllPriorities();
	
	public ArrayList<Priority> getPrioritiesForUser(int user_id);

	public void updatePriority(Priority priority);
	
	public int getDefaultPriorityIdForUser(int user_id);
	
	public void changePriorityToDefault(int priority_id, int default_id);

	public void deletePriority(int id);

}
