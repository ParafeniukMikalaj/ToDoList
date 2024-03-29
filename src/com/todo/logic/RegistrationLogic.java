package com.todo.logic;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Component;

import com.todo.controllers.UserForm;
import com.todo.data.DataProviderFactory;
import com.todo.data.ToDoDataProvider;
import com.todo.entities.Folder;
import com.todo.entities.Priority;
import com.todo.entities.Task;
import com.todo.entities.User;

/**
 * Class defines business logic for registration of user
 */
@Component
public class RegistrationLogic {

	private static ToDoDataProvider provider;
	
	private static StandardPasswordEncoder encoder;

	@Resource(name = "dataProviderFactory")
	public void setProviderFactory(DataProviderFactory providerFactory) {
		RegistrationLogic.provider = providerFactory.getDataProvider();
	}
	
	@Autowired
	public void setEncoder(){
		encoder = new StandardPasswordEncoder();
	}

	/**
	 * Creates some default folders, priorities and tasks for user
	 * @param user {@link com.todo.entities.User} user
	 */
	public static void registerUser(UserForm user) {
		User u = new User();
		u.setName(user.getName());
		u.setEmail(user.getEmail());
		u.setPassword(encoder.encode(user.getPassword()));
		int user_id = provider.createUser(u);
		Priority p = new Priority();
		p.setDescription("default");
		p.setColor("#dddddd");
		p.setUserId(user_id);
		provider.createPriority(p);
		p.setDescription("high");
		p.setColor("#FF0000");
		p.setUserId(user_id);
		provider.createPriority(p);
		p.setColor("#00FF00");
		p.setDescription("low");
		int priority_id = provider.createPriority(p);
		p.setColor("#0000FF");
		p.setDescription("medium");
		provider.createPriority(p);
		Folder f = new Folder();
		f.setDescription("tasks of "+user.getName());
		f.setUserId(user_id);
		f.setParentId(0);
		int folder_id = provider.createFolder(f);
		Task t = new Task();
		t.setPriorityId(priority_id);
		t.setUserId(user_id);
		t.setFolderId(folder_id);
		t.setDescription("learn service");
		t.setCreationDate(new Date());
		Calendar cal = Calendar.getInstance();  
		cal.setTime(new Date());  
		cal.add(Calendar.DAY_OF_YEAR, 1); // <--  
		Date tomorrow = cal.getTime(); 
		t.setExpirationDate(tomorrow);
		t.setX(0);
		t.setY(0);
		provider.createTask(t);		
	}

	/**
	 * wraps provider function to check if user name available
	 * @param userName user name to check
	 * @return true if available, false otherwise
	 */
	public static boolean userAvailable(String userName) {
		return provider.usernameAvailable(userName);
	}

	/**
	 * wraps provider function to check if email available
	 * @param email email to check
	 * @return true if available, false otherwise
	 */
	public static boolean emailAvailable(String email) {
		return provider.emailAvailable(email);
	}

}
