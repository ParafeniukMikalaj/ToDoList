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

	public static void deleteFolder(int folderId) throws Exception {
		Folder f = provider.getFolderById(folderId);
		
		if(f.getParentId()==0)
			throw new Exception("This is root folder can't be deleted");
		ArrayList<Folder> childs = provider.getSubFolders(folderId);
		for (Folder folder : childs)
			deleteFolder(folder.getId());
		ArrayList<Task> tasks = provider.getSubTasks(folderId);
		for (Task task : tasks)
			provider.deleteTask(task.getId());
		provider.deleteFolder(folderId);
	}
	
}
