package com.todo.data.hibernate;

import java.util.ArrayList;

import org.hibernate.SessionFactory;
//import org.springframework.orm.hibernate3.HibernateTemplate;

import com.todo.data.TestableToDoDataProvider;
import com.todo.entities.Folder;
import com.todo.entities.Priority;
import com.todo.entities.Task;
import com.todo.entities.User;

public class HibernateToDoDataProvider implements TestableToDoDataProvider {
	
	//private HibernateTemplate hibernateTemplate;
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
       // this.hibernateTemplate = new HibernateTemplate(sessionFactory);
    }

	@Override
	public void createTask(Task task) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Task getTaskById(int id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ArrayList<Task> getAllTasks() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateTask(Task task) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteTask(int id) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void createFolder(Folder folder) {
		throw new UnsupportedOperationException();

	}

	@Override
	public Folder getFolderById(int id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ArrayList<Folder> getAllFolders() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateFolder(Folder folder) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void deleteFolder(int id) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void close() {
		throw new UnsupportedOperationException();

	}

	@Override
	public void createUser(User user) {
		sessionFactory.getCurrentSession().save(user);
	}

	@Override
	public User getUserById(int id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ArrayList<User> getAllUsers() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void updateUser(User user) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void deleteUser(int id) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void createPriority(Priority priority) {
		throw new UnsupportedOperationException();

	}

	@Override
	public Priority getPriorityById(int id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ArrayList<Priority> getAllPriorities() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void updatePriority(Priority priority) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void deletePriority(int id) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void deleteAllUsers() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAllTasks() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAllFolders() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAllPriorities() {
		// TODO Auto-generated method stub
		
	}

}
