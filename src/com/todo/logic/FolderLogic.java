package com.todo.logic;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.todo.data.DataProviderFactory;
import com.todo.data.ToDoDataProvider;
import com.todo.entities.Folder;
import com.todo.entities.Task;

@Component
public class FolderLogic {

	private static ToDoDataProvider provider;

	@Resource(name = "dataProviderFactory")
	public void setProviderFactory(DataProviderFactory providerFactory) {
		FolderLogic.provider = providerFactory.getDataProvider();
	}

	public static void createNewFolder(Folder folder) {
		provider.createFolder(folder);
	}

	public static void updateFolder(Folder folder) {
		provider.updateFolder(folder);
	}

	public static ArrayList<Task> getTasksForFolder(int folderId) {
		ArrayList<Task> tasks = provider.getAllTasks();
		ArrayList<Task> result = new ArrayList<Task>();
		for (Task task : tasks) {
			if (task.getFolderId() == folderId)
				result.add(task);
		}
		return result;
	}

	public static ArrayList<Folder> getChildFolders(int folderId) {
		ArrayList<Folder> folders = provider.getAllFolders();
		ArrayList<Folder> result = new ArrayList<Folder>();
		for (Folder folder : folders) {
			if (folder.getParentId() == folderId)
				result.add(folder);
		}
		return result;
	}

	public static void deleteFolder(int folderId) {
		ArrayList<Folder> childs = getChildFolders(folderId);
		for (Folder folder : childs)
			deleteFolder(folder.getId());
		ArrayList<Task> tasks = getTasksForFolder(folderId);
		for (Task task : tasks)
			TaskLogic.deleteTask(task.getId());
	}
	
}
