package com.todo.logic;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.todo.data.DataProviderFactory;
import com.todo.data.ToDoDataProvider;
import com.todo.entities.Priority;

@Component
public class PriorityLogic {

	private static ToDoDataProvider provider;

	@Resource(name = "dataProviderFactory")
	public void setProviderFactory(DataProviderFactory providerFactory) {
		PriorityLogic.provider = providerFactory.getDataProvider();
	}

	public static void createPriority(Priority priority) {
		provider.createPriority(priority);
	}

	public static ArrayList<Priority> getPrioritiesForUser(int userId) {
		ArrayList<Priority> priorities = provider.getAllPriorities();
		ArrayList<Priority> filtered = new ArrayList<Priority>();
		for (Priority priority : priorities)
			if (priority.getUserId() == userId)
				filtered.add(priority);
		return filtered;
	}

	public static void updatePriority(Priority priority) {
		provider.updatePriority(priority);
	}

	public static boolean deletePriority(int priorityId, int userId) {
		int defaultId = provider.getDefaultPriorityIdForUser(userId);
		if(priorityId==defaultId)
			return false;
		provider.changePriorityToDefault(priorityId, defaultId);
		provider.deletePriority(priorityId);
		return true;
	}
	
}
