package com.todo.logic;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.todo.data.DataProviderFactory;
import com.todo.data.ToDoDataProvider;
import com.todo.entities.*;

@Component
public class UserLogic {
	
	private static ToDoDataProvider provider;

	@Resource(name="dataProviderFactory")
	public void setProviderFactory(DataProviderFactory providerFactory) {
		UserLogic.provider = providerFactory.getDataProvider();
	}
	
	public static void createUser(User user) {
		if(SecurityLogic.usernameAvailable(user.getName())){
			user.setPassword(SecurityLogic.getHash(user.getPassword()));
			provider.createUser(user);
		}
	}

	public static void deleteUser(int userId) {
		ArrayList<Task> tasks = provider.getAllTasks();
		for(Task task : tasks) {
			if(task.getUserId() == userId)
				provider.deleteTask(task.getId());
		}
		ArrayList<Folder> folders = provider.getAllFolders();
		for(Folder folder : folders) {
			if(folder.getUserId() == userId)
				provider.deleteFolder(folder.getId());
		}
		ArrayList<Priority> priorities = provider.getAllPriorities();
		for(Priority priority : priorities) {
			if(priority.getUserId() == userId)
				provider.deletePriority(priority.getId());
		}
	}
	
}
