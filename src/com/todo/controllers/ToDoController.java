package com.todo.controllers;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.todo.entities.*;
import com.todo.data.DataProviderFactory;
import com.todo.data.ToDoDataProvider;

public class ToDoController implements Controller {

	protected final Log logger = LogFactory.getLog(getClass());

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String now = (new java.util.Date()).toString();
		logger.info("returning hello view with " + now);
		ToDoDataProvider provider = DataProviderFactory.getInstance()
				.getDataProvider();
		//ArrayList<Task> tasks = provider.getAllTasks();
		ArrayList<Task> tasks = new ArrayList<Task>();

		logger.info("retrieving tasks. count =  " + tasks.size());
		Map<String, Object> myModel = new HashMap<String, Object>();
		myModel.put("now", now);
		myModel.put("tasks", tasks); 
		
		return new ModelAndView("hello", "model", myModel);
	}

}