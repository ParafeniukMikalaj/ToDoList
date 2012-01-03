package com.todo.controllers;

import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.todo.entities.Task;
import com.todo.service.CreateTask;

public class CreateTaskController extends SimpleFormController {

	/** Logger for this class and subclasses */
	protected final Log logger = LogFactory.getLog(getClass());

	public ModelAndView onSubmit(Object command) throws ServletException {

		CreateTask ct = (CreateTask) command;
		Task t = ct.getTask();

		logger.info("returning from PriceIncreaseForm view to "
				+ getSuccessView());

		return new ModelAndView(new RedirectView(getSuccessView()));
	}

	protected Object formBackingObject(HttpServletRequest request)
			throws ServletException {
		CreateTask ct = new CreateTask();
		return ct;
	}

}