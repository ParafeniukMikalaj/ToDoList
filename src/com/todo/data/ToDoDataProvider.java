package com.todo.data;

import java.util.ArrayList;

import com.todo.entities.*;

public interface ToDoDataProvider extends Closable {
	public void createTask(Task task);

	public Task getTaskById(int id);

	public ArrayList<Task> getAllTasks();

	public void updateTask(Task task);

	public void deleteTask(int id);

	public void createFolder(Folder folder);

	public Folder getFolderById(int id);

	public ArrayList<Folder> getAllFolders();

	public void updateFolder(Folder folder);

	public void deleteFolder(int id);

	public void createUser(User user);

	public User getUserById(int id);

	public ArrayList<User> getAllUsers();

	public void updateUser(User user);

	public void deleteUser(int id);

	public void createPriority(Priority priority);

	public Priority getPriorityById(int id);

	public ArrayList<Priority> getAllPriorities();

	public void updatePriority(Priority priority);

	public void deletePriority(int id);
}
