package com.todo.controllers;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.todo.controllers.UserForm;
import com.todo.logic.RegistrationLogic;

@Component
public class UserFormValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return UserForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		UserForm form = (UserForm)obj;
		if(!form.getPassword().equals(form.getConfirmation()))
			errors.rejectValue("confirmation","confirmation.notEquals", "doesn't match");
		if(!RegistrationLogic.emailAvailable(form.getEmail()))
			errors.rejectValue("email","email.notAvailable", "Email is already in use");
		if(!RegistrationLogic.userAvailable(form.getName()))
			errors.rejectValue("name","name.notAvailable", "Username is already in use");
	}	

}
