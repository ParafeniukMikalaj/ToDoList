package com.todo.logic;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.todo.data.DataProviderFactory;
import com.todo.data.ToDoDataProvider;
import com.todo.entities.Folder;
import com.todo.entities.Task;

/**
 * Class defines business logic for managing folders 
 * which can not be provided by {@link com.todo.data.ToDoDataProvider}
 * @author Mikalai
 */
@Component
public class FolderLogic {

	private static ToDoDataProvider provider;

	@Resource(name = "dataProviderFactory")
	public void setProviderFactory(DataProviderFactory providerFactory) {
		FolderLogic.provider = providerFactory.getDataProvider();
	}

	/**
	 * Removes the folder if it is not the root folder
	 * and all of sub tasks and sub folders
	 * @param folderId folder id to delete
	 * @throws Exception occurs when trying to delete root folder 
	 */
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
