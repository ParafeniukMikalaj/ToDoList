package com.todo.entities;

import java.util.Calendar;
import java.util.Date;

public class Task {
	private int id;
	private int userId;
	private int priorityId;
	private int delayedTimes;
	private int folderId;
	private int x;
	private int y;
	private String description;
	private Date creationDate;
	private Date expirationDate;

	public Task() {
		description = "<initial value>";
		creationDate = new Date(Calendar.getInstance().getTimeInMillis());
		expirationDate = (Date) creationDate.clone();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(int priorityId) {
		this.priorityId = priorityId;
	}

	public int getDelayedTimes() {
		return delayedTimes;
	}

	public void setDelayedTimes(int delayedTimes) {
		this.delayedTimes = delayedTimes;
	}

	public int getFolderId() {
		return folderId;
	}

	public void setFolderId(int folderId) {
		this.folderId = folderId;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public void setCreationDate(long miliseconds) {
		this.creationDate = new Date(miliseconds);
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public void setExpirationDate(long miliseconds) {
		this.expirationDate = new Date(miliseconds);
	}

}
