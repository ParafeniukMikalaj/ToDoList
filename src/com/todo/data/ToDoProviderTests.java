package com.todo.data;

import java.util.ArrayList;
import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.todo.entities.Folder;
import com.todo.entities.Priority;
import com.todo.entities.Task;
import com.todo.entities.User;
import junit.framework.TestCase;

public abstract class ToDoProviderTests extends TestCase {
	protected TestableToDoDataProvider provider;

	protected abstract void initProvider();

	protected final Log logger = LogFactory.getLog(getClass());

	public void testUserTable() throws Exception {
		try {
			initProvider();
			assertNotNull(provider);
			provider.deleteAllUsers();
			User u = new User();
			u.setName("test123");
			u.setEmail("test@gmail.com");
			u.setPassword("qwerty");
			provider.createUser(u);
			provider.createUser(u);
			assertTrue(provider.usernameAvailable("test1231"));
			assertTrue(!provider.usernameAvailable("test123"));
			assertTrue(provider.emailAvailable("test@gmail.com1"));
			assertTrue(!provider.emailAvailable("test@gmail.com"));
			ArrayList<User> users = provider.getAllUsers();
			assertNotNull(users);
			assertTrue(users.size() == 2);
			User newUser = provider.getUserById(users.get(0).getId());
			assertNotNull(newUser);
			assertTrue(newUser.getName().equals(u.getName()));
			assertTrue(newUser.getEmail().equals(u.getEmail()));
			assertTrue(newUser.getPassword().equals(u.getPassword()));
			newUser.setName("test21");
			provider.createUser(newUser);
			newUser.setName("test31");
			provider.updateUser(newUser);
			provider.deleteAllUsers();

		} catch (Exception e) {
			logger.error("exception occured while testing user table:    "
					+ e.getMessage());
			assertNotNull(null);
		}
	}

	public void testFolderTable() throws Exception {
		try {
			initProvider();
			assertNotNull(provider);
			provider.deleteAllFolders();
			Folder f = new Folder();
			f.setDescription("home");

			int id = provider.createFolder(f);
			logger.info("first folder id = "+id);
			id = provider.createFolder(f);
			logger.info("second folder id = "+id);
			ArrayList<Folder> folders = provider.getAllFolders();
			assertNotNull(folders);
			assertTrue(folders.size() == 2);
			ArrayList<Folder> subFolder = provider.getSubFolders(0, 0);
			assertNotNull(subFolder);
			assertTrue(subFolder.size() == 2);
			Folder newFolder = provider.getFolderById(folders.get(0).getId());
			assertNotNull(newFolder);
			assertTrue(newFolder.getParentId() == f.getParentId());
			assertTrue(newFolder.getDescription().equals(f.getDescription()));
			assertTrue(newFolder.getUserId() == f.getUserId());
			newFolder.setDescription("private");
			provider.createFolder(newFolder);
			newFolder.setDescription("public");
			provider.updateFolder(newFolder);
			provider.deleteAllFolders();
		} catch (Exception e) {
			logger.error("exception occured while testing user table:    "
					+ e.getMessage());
			assertNotNull(null);
		}
	}

	public void testPriorityTable() throws Exception {
		try {
			initProvider();
			assertNotNull(provider);
			provider.deleteAllPriorities();
			Priority p = new Priority();
			p.setDescription("hign");
			p.setColor("#ffffff");

			int id = provider.createPriority(p);
			logger.info("first priority id = "+id);
			id = provider.createPriority(p);
			logger.info("second priority id = "+id);
			ArrayList<Priority> priorities = provider.getAllPriorities();
			assertNotNull(priorities);
			assertTrue(priorities.size() == 2);
			Priority newPriority = provider.getPriorityById(priorities.get(0)
					.getId());
			assertNotNull(newPriority);
			assertTrue(newPriority.getDescription().equals(p.getDescription()));
			assertTrue(newPriority.getUserId() == p.getUserId());
			newPriority.setDescription("low");
			provider.createPriority(newPriority);
			newPriority.setDescription("normal");
			provider.updatePriority(newPriority);
			provider.deleteAllPriorities();
		} catch (Exception e) {
			logger.error("exception occured while testing user table:    "
					+ e.getMessage());
			assertNotNull(null);
		}
	}

	public void testTaskTable() throws Exception {
		try {
			initProvider();
			assertNotNull(provider);

			provider.deleteAllTasks();
			provider.deleteAllPriorities();
			provider.deleteAllFolders();
			provider.deleteAllUsers();
			
			

			User u = new User();
			u.setName("test1");
			u.setEmail("test@gmail.com");
			u.setPassword("qwerty");

			Priority p = new Priority();
			p.setDescription("hign");

			Folder f = new Folder();
			f.setDescription("home");

			provider.createUser(u);
			provider.createFolder(f);
			provider.createPriority(p);
			
			ArrayList<User> users = provider.getAllUsers();
			ArrayList<Priority> priorities = provider.getAllPriorities();
			ArrayList<Folder> folders = provider.getAllFolders();

			int folder_id = folders.get(0).getId();
			int usert_id = users.get(0).getId();
			int priority_id = priorities.get(0).getId();

			Task t = new Task();
			t.setDescription("learn spring");
			t.setCreationDate(Calendar.getInstance().getTime());
			t.setExpirationDate(Calendar.getInstance().getTime());
			t.setPriorityId(priority_id);
			t.setUserId(usert_id);
			t.setFolderId(folder_id);
			t.setX(111);
			t.setY(222);
			int id = provider.createTask(t);
			logger.info("first task id = "+id);
			id = provider.createTask(t);
			logger.info("second task id = "+id);
			
			ArrayList<Task> tasks = provider.getAllTasks();
			assertNotNull(tasks);
			assertTrue(tasks.size() == 2);
			ArrayList<Task> subTasks = provider.getSubTasks(usert_id, folder_id);
			assertNotNull(subTasks);
			assertTrue(subTasks.size() == 2);
			Task newTask = provider.getTaskById(tasks.get(0).getId());
			assertNotNull(newTask);
			newTask.setDescription("make test project");
			provider.createTask(newTask);
			newTask.setDescription("learn ioc");
			provider.updateTask(newTask);

			provider.deleteAllTasks();
			provider.deleteAllPriorities();
			provider.deleteAllFolders();
			provider.deleteAllUsers();
		} catch (Exception e) {
			logger.error("exception occured while testing user table:    "
					+ e.getMessage());
			assertNotNull(null);
		}
	}
	
}
