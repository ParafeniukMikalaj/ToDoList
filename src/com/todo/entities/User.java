package com.todo.entities;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * entity class which specifies user
 * @author Mikalai
 */
public class User {
	
	private int id;

	@NotEmpty(message = "Required")
	@Length(min = 5, message = "Need at least 5 symbols")
	private String name;

	@NotEmpty(message = "Required")
	@Email(message = "Please enter a valid email")
	private String email;

	@NotEmpty(message = "Required")
	@Length(min = 5, message = "Need at least 5 symbols")
	private String password;
	
	private boolean view;
	
	private boolean demo;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}	
	
	
	public boolean isView() {
		return view;
	}

	public void setView(boolean view) {
		this.view = view;
	}

	public boolean isDemo() {
		return demo;
	}

	public void setDemo(boolean demo) {
		this.demo = demo;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email
				+ ", password=" + password + ", view=" + view + ", demo="
				+ demo + "]";
	}

}
