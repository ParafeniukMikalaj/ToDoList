package com.todo.data.jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;

import com.todo.data.TestableToDoDataProvider;
import com.todo.entities.Folder;
import com.todo.entities.Priority;
import com.todo.entities.Task;
import com.todo.entities.User;

@Repository
public class JDBCToDoDataProvider implements TestableToDoDataProvider {

	private static SimpleJdbcTemplate simpleJdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		simpleJdbcTemplate = new SimpleJdbcTemplate(dataSource);
	}

	private DataSource getDataSourceTest() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/todo");
		dataSource.setUsername("root");
		dataSource.setPassword("mysql");
		return dataSource;
	}

	public void SetTestDataSource() {
		setDataSource(getDataSourceTest());
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
			task.setDelayedTimes(rs.getInt("delayed_times"));
			task.setFolderId(rs.getInt("folder_id"));
			task.setX(rs.getInt("x"));
			task.setX(rs.getInt("y"));
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
			return user;
		}
	}

	private static final class PriorityMapper implements RowMapper<Priority> {

		public Priority mapRow(ResultSet rs, int rowNum) throws SQLException {
			Priority priority = new Priority();
			priority.setId(rs.getInt("id"));
			priority.setDescription(rs.getString("description"));
			priority.setUserId(rs.getInt("user_id"));
			return priority;
		}
	}

	@Override
	public void createTask(Task task) {
		long creation = task.getCreationDate().getTime();
		long expiration = task.getExpirationDate().getTime();
		String sql = "INSERT INTO Task (description, priority_id, creation_date, expiration_date, user_id, delayed_times, folder_id)"
				+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
		simpleJdbcTemplate.update(sql, task.getDescription(),
				task.getPriorityId(), creation, expiration, task.getUserId(),
				task.getDelayedTimes(), task.getFolderId());
	}

	@Override
	public Task getTaskById(int id) {
		String sql = "select * from Task where id = ?";
		return simpleJdbcTemplate.queryForObject(sql, new TaskMapper(), id);
	}

	@Override
	public ArrayList<Task> getAllTasks() {
		String sql = "select * from Task";
		return new ArrayList<Task>(simpleJdbcTemplate.query(sql,
				new TaskMapper()));
	}

	@Override
	public void updateTask(Task task) {
		String sql = "Update Task set description = ?, priority_id = ?, creation_date = ?, expiration_date = ?, user_id = ?, delayed_times = ?, folder_id = ? where id = ?";
		simpleJdbcTemplate.update(sql, task.getDescription(), task
				.getPriorityId(), task.getCreationDate().getTime(), task
				.getExpirationDate().getTime(), task.getUserId(), task
				.getDelayedTimes(), task.getFolderId(), task.getId());
	}

	@Override
	public void deleteTask(int id) {
		String sql = "delete from Task where id = ?";
		simpleJdbcTemplate.update(sql, id);
	}

	@Override
	public void createFolder(Folder folder) {
		String sql = "INSERT INTO Folder (description, user_id, parent_id) VALUES (?, ?, ?)";
		simpleJdbcTemplate.update(sql, folder.getDescription(),
				folder.getUserId(), folder.getParentId());

	}

	@Override
	public Folder getFolderById(int id) {
		String sql = "select * from Folder where id = ?";
		return simpleJdbcTemplate.queryForObject(sql, new FolderMapper(), id);
	}

	@Override
	public ArrayList<Folder> getAllFolders() {
		String sql = "select * from Folder";
		return new ArrayList<Folder>(simpleJdbcTemplate.query(sql,
				new FolderMapper()));
	}

	@Override
	public void updateFolder(Folder folder) {
		String sql = "Update Folder set description = ?, user_id = ?, parent_id = ? where id = ?";
		simpleJdbcTemplate.update(sql, folder.getDescription(),
				folder.getUserId(), folder.getParentId(), folder.getId());

	}

	@Override
	public void deleteFolder(int id) {
		String sql = "delete from Folder where id = ?";
		simpleJdbcTemplate.update(sql, id);
	}

	@Override
	public void close() {
		/*do nothing, because spring manages connections*/
	}

	@Override
	public void createUser(User user) {
		String sql = "INSERT INTO User (name, email, password) VALUES (?, ?, ?)";
		simpleJdbcTemplate.update(sql, user.getName(), user.getEmail(),
				user.getPassword());
	}

	@Override
	public User getUserById(int id) {
		String sql = "select * from User where id = ?";
		return simpleJdbcTemplate.queryForObject(sql, new UserMapper(), id);
	}

	@Override
	public ArrayList<User> getAllUsers() {
		String sql = "select * from User";
		return new ArrayList<User>(simpleJdbcTemplate.query(sql,
				new UserMapper()));
	}

	@Override
	public void updateUser(User user) {
		String sql = "Update User set name = ?, email = ?, password = ? where id = ?";
		simpleJdbcTemplate.update(sql, user.getName(), user.getEmail(),
				user.getPassword(), user.getId());
	}

	@Override
	public void deleteUser(int id) {
		String sql = "delete from User where id = ?";
		simpleJdbcTemplate.update(sql, id);
	}

	@Override
	public void createPriority(Priority priority) {
		String sql = "INSERT INTO Priority (description, user_id) VALUES (?, ?)";
		simpleJdbcTemplate.update(sql, priority.getDescription(),
				priority.getUserId());

	}

	@Override
	public Priority getPriorityById(int id) {
		String sql = "select * from Priority where id = ?";
		return simpleJdbcTemplate.queryForObject(sql, new PriorityMapper(), id);
	}

	@Override
	public ArrayList<Priority> getAllPriorities() {
		String sql = "select * from Priority";
		return new ArrayList<Priority>(simpleJdbcTemplate.query(sql,
				new PriorityMapper()));
	}

	@Override
	public void updatePriority(Priority priority) {
		String sql = "Update Priority set description = ?, user_id = ? where id = ?";
		simpleJdbcTemplate.update(sql, priority.getDescription(),
				priority.getUserId(), priority.getId());
	}

	@Override
	public void deletePriority(int id) {
		String sql = "delete from Priority where id = ?";
		simpleJdbcTemplate.update(sql, id);
	}

	@Override
	public void deleteAllUsers() {
		String sql = "delete from User";
		simpleJdbcTemplate.update(sql);

	}

	@Override
	public void deleteAllTasks() {
		String sql = "delete from Task";
		simpleJdbcTemplate.update(sql);

	}

	@Override
	public void deleteAllFolders() {
		String sql = "delete from Folder";
		simpleJdbcTemplate.update(sql);

	}

	@Override
	public void deleteAllPriorities() {
		String sql = "delete from Priority";
		simpleJdbcTemplate.update(sql);

	}

}