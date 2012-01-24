package com.todo.entities;

public class Priority {
	
	private int id;
	private String description;
	private int userId;
	private String color;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	@Override
	public String toString() {
		return "Priority [id=" + id + ", description=" + description
				+ ", userId=" + userId + ", color=" + color + "]";
	}

}
