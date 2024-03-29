package com.todo.entities;

/**
 * entity class which specifies folder
 * @author Mikalai
 */
public class Folder {

	private int id;
	private int parentId;
	private int userId;
	private String description;

	public Folder() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}	

	@Override
	public String toString() {
		return "Folder [id=" + id + ", parentId=" + parentId + ", userId="
				+ userId + ", description=" + description + "]";
	}

}
