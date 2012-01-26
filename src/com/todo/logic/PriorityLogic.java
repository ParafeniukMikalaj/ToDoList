package com.todo.logic;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.todo.data.DataProviderFactory;
import com.todo.data.ToDoDataProvider;

/**
 * Class defines business logic for managing priorities 
 * which can not be provided by {@link com.todo.data.ToDoDataProvider}
 * @author Mikalai
 */
@Component
public class PriorityLogic {

	private static ToDoDataProvider provider;

	@Resource(name = "dataProviderFactory")
	public void setProviderFactory(DataProviderFactory providerFactory) {
		PriorityLogic.provider = providerFactory.getDataProvider();
	}

	/**
	 * Provider custom logic for deleting priority
	 * finds all tasks this deleted priority, changes theirs priority to 
	 * default, and only then performs deletion
	 * @param priorityId priority id
	 * @param userId user id
	 * @return true if success false otherwise
	 */
	public static boolean deletePriority(int priorityId, int userId) {
		int defaultId = provider.getDefaultPriorityIdForUser(userId);
		if(priorityId==defaultId)
			return false;
		provider.changePriorityToDefault(priorityId, defaultId);
		provider.deletePriority(priorityId);
		return true;
	}
	
}
