package com.todo.controllers;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.todo.entities.User;

public class UserForm extends User {
	
	@NotEmpty
	@Length(min = 5, message="Need at least 5 symbols")
	private String confirmation;

	public String getConfirmation() {
		return confirmation;
	}

	public void setConfirmation(String confirmation) {
		this.confirmation = confirmation;
	}
}
