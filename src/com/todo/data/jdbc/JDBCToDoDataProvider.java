package com.todo.data.jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.todo.data.TestableToDoDataProvider;
import com.todo.entities.Folder;
import com.todo.entities.Priority;
import com.todo.entities.Task;
import com.todo.entities.User;

@Component(value="jdbcProvider")
public class JDBCToDoDataProvider implements TestableToDoDataProvider {

	private static JdbcTemplate  jdbcTemplate;

	@Resource(name="dataSource")
	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	private static final class TaskMapper implements RowMapper<Task> {

		public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
			Task task = new Task();
			task.setId(rs.getInt("id"));
			task.setDescription(rs.getString("description"));
			task.setPriorityId(rs.getInt("priority_id"));
			task.setCreationDate(new Date(rs.getLong("creation_date")));
			task.setExpirationDate(new Date(rs.getLong("expiration_date")));
			task.setUserId(rs.getInt("user_id"));
			task.setX(rs.getInt("x"));
			task.setY(rs.getInt("y"));
			task.setDelayedTimes(rs.getInt("delayed_times"));
			task.setFolderId(rs.getInt("folder_id"));
			return task;
		}
	}

	private static final class FolderMapper implements RowMapper<Folder> {

		public Folder mapRow(ResultSet rs, int rowNum) throws SQLException {
			Folder folder = new Folder();
			folder.setId(rs.getInt("id"));
			folder.setDescription(rs.getString("description"));
			folder.setParentId(rs.getInt("parent_id"));
			folder.setUserId(rs.getInt("user_id"));
			return folder;
		}
	}

	private static final class UserMapper implements RowMapper<User> {

		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setId(rs.getInt("id"));
			user.setName(rs.getString("name"));
			user.setEmail(rs.getString("email"));
			user.setPassword(rs.getString("password"));
			user.setDemo(rs.getBoolean("demo"));
			user.setView(rs.getBoolean("view"));
			return user;
		}
	}

	private static final class PriorityMapper implements RowMapper<Priority> {

		public Priority mapRow(ResultSet rs, int rowNum) throws SQLException {
			Priority priority = new Priority();
			priority.setId(rs.getInt("id"));
			priority.setDescription(rs.getString("description"));
			priority.setUserId(rs.getInt("user_id"));
			priority.setColor(rs.getString("color"));
			return priority;
		}
	}

	@Override
	public int createTask(Task task) {
		long creation = task.getCreationDate().getTime();
		long expiration = task.getExpirationDate().getTime();
		String sql = "INSERT INTO Task (description, priority_id, creation_date, expiration_date, user_id, delayed_times, folder_id, x, y)"
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, task.getDescription(),
				task.getPriorityId(), creation, expiration, task.getUserId(),
				task.getDelayedTimes(), task.getFolderId(), task.getX(), task.getY());
		sql = "select max(id) from Task where user_id = ? and  folder_id = ? and priority_id = ?";
		int id = jdbcTemplate.queryForInt(sql, task.getUserId(), task.getFolderId(), task.getPriorityId());
		return id;
	}

	@Override
	public Task getTaskById(int id) {
		String sql = "select * from Task where id = ?";
		return jdbcTemplate.queryForObject(sql, new TaskMapper(), id);
	}
	
	@Override
	public ArrayList<Task> getSubTasks(int user_id, int folder_id) {
		String sql = "select * from Task where user_id = ? and folder_id = ?";
		return new ArrayList<Task>(jdbcTemplate.query(sql,
				new TaskMapper(), user_id, folder_id));
	}

	@Override
	public ArrayList<Task> getAllTasks() {
		String sql = "select * from Task";
		return new ArrayList<Task>(jdbcTemplate.query(sql,
				new TaskMapper()));
	}

	@Override
	public void updateTask(Task task) {
		String sql = "Update Task set description = ?, priority_id = ?, creation_date = ?, expiration_date = ?, user_id = ?, delayed_times = ?, folder_id = ?, x = ?, y = ? where id = ?";
		jdbcTemplate.update(sql, task.getDescription(), task
				.getPriorityId(), task.getCreationDate().getTime(), task
				.getExpirationDate().getTime(), task.getUserId(), task
				.getDelayedTimes(), task.getFolderId(), task.getId(), task.getX(), task.getY());
	}

	@Override
	public void deleteTask(int id) {
		String sql = "delete from Task where id = ?";
		jdbcTemplate.update(sql, id);
	}

	@Override
	public int createFolder(Folder folder) {
		String sql = "INSERT INTO Folder (description, user_id, parent_id) VALUES (?, ?, ?)";
		jdbcTemplate.update(sql, folder.getDescription(),
				folder.getUserId(), folder.getParentId());
		sql = "select max(id) from Folder where user_id = ? and parent_id = ?";
		int id = jdbcTemplate.queryForInt(sql, folder.getUserId(), folder.getParentId());
		return id;

	}

	@Override
	public Folder getFolderById(int id) {
		String sql = "select * from Folder where id = ?";
		return jdbcTemplate.queryForObject(sql, new FolderMapper(), id);
	}
	
	@Override
	public ArrayList<Folder> getSubFolders(int user_id, int parent_id) {
		String sql = "select * from Folder where user_id = ? and parent_id = ?";
		return new ArrayList<Folder>(jdbcTemplate.query(sql,
				new FolderMapper(), user_id, parent_id));
	}

	@Override
	public ArrayList<Folder> getAllFolders() {
		String sql = "select * from Folder";
		return new ArrayList<Folder>(jdbcTemplate.query(sql,
				new FolderMapper()));
	}

	@Override
	public void updateFolder(Folder folder) {
		String sql = "Update Folder set description = ?, user_id = ?, parent_id = ? where id = ?";
		jdbcTemplate.update(sql, folder.getDescription(),
				folder.getUserId(), folder.getParentId(), folder.getId());

	}

	@Override
	public void deleteFolder(int id) {
		String sql = "delete from Folder where id = ?";
		jdbcTemplate.update(sql, id);
	}

	@Override
	public int createUser(User user) {
		String sql = "INSERT INTO User (name, email, password, demo, view) VALUES (?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, user.getName(), user.getEmail(),
				user.getPassword(), user.isDemo(), user.isView());
		sql = "select max(id) from User where name = ? and  password = ?";
		int id = jdbcTemplate.queryForInt(sql, user.getName(), user.getPassword());
		return id;
	}

	@Override
	public User getUserById(int id) {
		String sql = "select * from User where id = ?";
		return jdbcTemplate.queryForObject(sql, new UserMapper(), id);
	}	

	@Override
	public User getUserByName(String name) {
		String sql = "select * from User where name = ?";
		return jdbcTemplate.queryForObject(sql, new UserMapper(), name);
	}
	

	@Override
	public boolean usernameAvailable(String name) {
		String sql = "select count(0) from User where name = ?";
		int count = jdbcTemplate.queryForInt(sql, name);
		if(count == 0)
			return true;
		return false;
	}

	@Override
	public boolean emailAvailable(String email) {
		String sql = "select count(0) from User where email = ?";
		int count = jdbcTemplate.queryForInt(sql, email);
		if(count == 0)
			return true;
		return false;
	}

	@Override
	public ArrayList<User> getAllUsers() {
		String sql = "select * from User";
		return new ArrayList<User>(jdbcTemplate.query(sql,
				new UserMapper()));
	}

	@Override
	public void updateUser(User user) {
		String sql = "Update User set name = ?, email = ?, password = ?, demo = ?, view = ? where id = ?";
		jdbcTemplate.update(sql, user.getName(), user.getEmail(),
				user.getPassword(), user.getId(), user.isDemo(), user.isView());
	}

	@Override
	public void deleteUser(int id) {
		String sql = "delete from User where id = ?";
		jdbcTemplate.update(sql, id);
	}

	@Override
	public int createPriority(Priority priority) {
		String sql = "INSERT INTO Priority (description, user_id, color) VALUES (?, ?, ?)";
		jdbcTemplate.update(sql, priority.getDescription(),
				priority.getUserId(), priority.getColor());
		sql = "select max(id) from Priority where user_id = ?";
		int id = jdbcTemplate.queryForInt(sql, priority.getUserId());
		return id;
	}

	@Override
	public Priority getPriorityById(int id) {
		String sql = "select * from Priority where id = ?";
		return jdbcTemplate.queryForObject(sql, new PriorityMapper(), id);
	}

	@Override
	public ArrayList<Priority> getAllPriorities() {
		String sql = "select * from Priority";
		return new ArrayList<Priority>(jdbcTemplate.query(sql,
				new PriorityMapper()));
	}

	@Override
	public ArrayList<Priority> getPrioritiesForUser(int user_id) {
		String sql = "select * from Priority where user_id = ?";
		return new ArrayList<Priority>(jdbcTemplate.query(sql, new PriorityMapper(), user_id));
	}

	@Override
	public void updatePriority(Priority priority) {
		String sql = "Update Priority set description = ?, user_id = ?, color = ? where id = ?";
		jdbcTemplate.update(sql, priority.getDescription(),
				priority.getUserId(), priority.getId(), priority.getColor());
	}	

	@Override
	public int getDefaultPriorityIdForUser(int user_id) {
		String sql = "select min(id) from Priority where user_id = ?";
		int id = jdbcTemplate.queryForInt(sql, user_id);
		return id;
	}

	@Override
	public void changePriorityToDefault(int priority_id, int default_id) {
		String sql = "Update Priority set id = ? where id = ?";
		jdbcTemplate.update(sql, default_id, priority_id);		
	}

	@Override
	public void deletePriority(int id) {
		String sql = "delete from Priority where id = ?";
		jdbcTemplate.update(sql, id);
	}

	@Override
	public void deleteAllUsers() {
		String sql = "delete from User";
		jdbcTemplate.update(sql);
	}

	@Override
	public void deleteAllTasks() {
		String sql = "delete from Task";
		jdbcTemplate.update(sql);
	}

	@Override
	public void deleteAllFolders() {
		String sql = "delete from Folder";
		jdbcTemplate.update(sql);
	}

	@Override
	public void deleteAllPriorities() {
		String sql = "delete from Priority";
		jdbcTemplate.update(sql);
	}

}