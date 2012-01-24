package com.todo.logic;

import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.todo.data.DataProviderFactory;
import com.todo.data.ToDoDataProvider;
import com.todo.entities.Task;

@Component
public class TaskLogic {

	private static ToDoDataProvider provider;

	@Resource(name = "dataProviderFactory")
	public void setProviderFactory(DataProviderFactory providerFactory) {
		TaskLogic.provider = providerFactory.getDataProvider();
	}

	public static ArrayList<Task> getUserTasks(int userId, Date fromDate,
			Date toDate) {
		ArrayList<Task> filteredTasks = new ArrayList<Task>();
		ArrayList<Task> tasks = provider.getAllTasks();
		for (Task task : tasks) {
			if (task.getUserId() == userId
					&& task.getCreationDate().after(fromDate)
					&& task.getCreationDate().before(toDate))
				filteredTasks.add(task);
		}
		return filteredTasks;
	}

	public static void createNewTask(Task task) {
		provider.createTask(task);
	}

	public static void updateTask(Task task) {
		provider.updateTask(task);
	}

	public static void deleteTask(int id) {
		provider.deleteTask(id);
	}

}
