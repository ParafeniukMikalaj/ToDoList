package com.todo.service;

import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.todo.entities.Task;

public class CreateTaskValidator implements Validator {

	protected final Log logger = LogFactory.getLog(getClass());

	@Override
	public boolean supports(Class clazz) {
		return CreateTask.class.equals(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {

		CreateTask ct = (CreateTask) obj;
		Task t = ct.getTask();
		if (t==null) {
			errors.rejectValue("task.description", "error.blank-description", null,
					"Value required");
			return;
		}
		if (t.getDescription() == null || t.getDescription() == "") {
			errors.rejectValue("task.description", "error.blank-description", null,
					"Value required");
		}
		if (t.getCreationDate().compareTo(Calendar.getInstance().getTime()) > 0) {
			errors.rejectValue("task.expirationDate", "error.creation-in-future",
					null, "Incorrect value");
		}
		if (t.getExpirationDate().compareTo(t.getCreationDate()) < 0) {
			errors.rejectValue("task.expirationDate",
					"error.expiration-before-creation", null, "Incorrect value");
		}

	}

}
