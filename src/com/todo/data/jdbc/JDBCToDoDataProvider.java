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

/**
 * JDBC based implementation of {@link com.todo.data.ToDoDataProvider}
 * @author Mikalai
 */
@Component(value="jdbcProvider")
public class JDBCToDoDataProvider implements TestableToDoDataProvider {

	private static JdbcTemplate  jdbcTemplate;

	@Resource(name="dataSource")
	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	/**
	 * class specific for jdbc to map {@link com.todo.entities.Task}
	 * @author Mikalai
	 */
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

	/**
	 * class specific for jdbc to map {@link com.todo.entities.Folder}
	 * @author Mikalai
	 */
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

	/**
	 * class specific for jdbc to map {@link com.todo.entities.User}
	 * @author Mikalai
	 */
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

	/**
	 * class specific for jdbc to map {@link com.todo.entities.Priority}
	 * @author Mikalai
	 */
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

	/**{@inheritDoc}*/
	@Override
	public Task getTaskById(int id) {
		String sql = "select * from Task where id = ?";
		return jdbcTemplate.queryForObject(sql, new TaskMapper(), id);
	}
	
	/**{@inheritDoc}*/
	@Override
	public ArrayList<Task> getSubTasks(int folder_id) {
		String sql = "select * from Task where folder_id = ?";
		return new ArrayList<Task>(jdbcTemplate.query(sql,
				new TaskMapper(), folder_id));
	}

	/**{@inheritDoc}*/
	@Override
	public void updateTask(Task task) {
		String sql = "Update Task set description = ?, priority_id = ?, creation_date = ?, expiration_date = ?, user_id = ?, delayed_times = ?, folder_id = ?, x = ?, y = ? where id = ?";
		jdbcTemplate.update(sql, task.getDescription(), task
				.getPriorityId(), task.getCreationDate().getTime(), task
				.getExpirationDate().getTime(), task.getUserId(), task
				.getDelayedTimes(), task.getFolderId(), task.getId(), task.getX(), task.getY());
	}

	/**{@inheritDoc}*/
	@Override
	public void deleteTask(int id) {
		String sql = "delete from Task where id = ?";
		jdbcTemplate.update(sql, id);
	}

	/**{@inheritDoc}*/
	@Override
	public int createFolder(Folder folder) {
		String sql = "INSERT INTO Folder (description, user_id, parent_id) VALUES (?, ?, ?)";
		jdbcTemplate.update(sql, folder.getDescription(),
				folder.getUserId(), folder.getParentId());
		sql = "select max(id) from Folder where user_id = ? and parent_id = ?";
		int id = jdbcTemplate.queryForInt(sql, folder.getUserId(), folder.getParentId());
		return id;

	}

	/**{@inheritDoc}*/
	@Override
	public Folder getFolderById(int id) {
		String sql = "select * from Folder where id = ?";
		return jdbcTemplate.queryForObject(sql, new FolderMapper(), id);
	}
	
	/**{@inheritDoc}*/
	@Override
	public ArrayList<Folder> getSubFolders(int parent_id) {
		String sql = "select * from Folder where parent_id = ?";
		return new ArrayList<Folder>(jdbcTemplate.query(sql,
				new FolderMapper(), parent_id));
	}

	/**{@inheritDoc}*/
	@Override
	public void updateFolder(Folder folder) {
		String sql = "Update Folder set description = ?, user_id = ?, parent_id = ? where id = ?";
		jdbcTemplate.update(sql, folder.getDescription(),
				folder.getUserId(), folder.getParentId(), folder.getId());

	}

	/**{@inheritDoc}*/
	@Override
	public void deleteFolder(int id) {
		String sql = "delete from Folder where id = ?";
		jdbcTemplate.update(sql, id);
	}

	/**{@inheritDoc}*/
	@Override
	public int createUser(User user) {
		String sql = "INSERT INTO User (name, email, password, demo, view) VALUES (?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, user.getName(), user.getEmail(),
				user.getPassword(), user.isDemo(), user.isView());
		sql = "select max(id) from User where name = ? and  password = ?";
		int id = jdbcTemplate.queryForInt(sql, user.getName(), user.getPassword());
		return id;
	}

	/**{@inheritDoc}*/
	@Override
	public User getUserById(int id) {
		String sql = "select * from User where id = ?";
		return jdbcTemplate.queryForObject(sql, new UserMapper(), id);
	}	

	/**{@inheritDoc}*/
	@Override
	public User getUserByName(String name) {
		String sql = "select * from User where name = ?";
		return jdbcTemplate.queryForObject(sql, new UserMapper(), name);
	}
	
	/**{@inheritDoc}*/
	@Override
	public boolean usernameAvailable(String name) {
		String sql = "select count(0) from User where name = ?";
		int count = jdbcTemplate.queryForInt(sql, name);
		if(count == 0)
			return true;
		return false;
	}

	/**{@inheritDoc}*/
	@Override
	public boolean emailAvailable(String email) {
		String sql = "select count(0) from User where email = ?";
		int count = jdbcTemplate.queryForInt(sql, email);
		if(count == 0)
			return true;
		return false;
	}

	/**{@inheritDoc}*/
	@Override
	public void updateUser(User user) {
		String sql = "Update User set name = ?, email = ?, password = ?, demo = ?, view = ? where id = ?";
		jdbcTemplate.update(sql, user.getName(), user.getEmail(),
				user.getPassword(), user.getId(), user.isDemo(), user.isView());
	}

	/**{@inheritDoc}*/
	@Override
	public void deleteUser(int id) {
		String sql = "delete from User where id = ?";
		jdbcTemplate.update(sql, id);
	}

	/**{@inheritDoc}*/
	@Override
	public int createPriority(Priority priority) {
		String sql = "INSERT INTO Priority (description, user_id, color) VALUES (?, ?, ?)";
		jdbcTemplate.update(sql, priority.getDescription(),
				priority.getUserId(), priority.getColor());
		sql = "select max(id) from Priority where user_id = ?";
		int id = jdbcTemplate.queryForInt(sql, priority.getUserId());
		return id;
	}

	/**{@inheritDoc}*/
	@Override
	public Priority getPriorityById(int id) {
		String sql = "select * from Priority where id = ?";
		return jdbcTemplate.queryForObject(sql, new PriorityMapper(), id);
	}

	/**{@inheritDoc}*/
	@Override
	public ArrayList<Priority> getPrioritiesForUser(int user_id) {
		String sql = "select * from Priority where user_id = ?";
		return new ArrayList<Priority>(jdbcTemplate.query(sql, new PriorityMapper(), user_id));
	}

	/**{@inheritDoc}*/
	@Override
	public void updatePriority(Priority priority) {
		String sql = "Update Priority set description = ?, user_id = ?, color = ? where id = ?";
		jdbcTemplate.update(sql, priority.getDescription(),
				priority.getUserId(), priority.getId(), priority.getColor());
	}	

	/**{@inheritDoc}*/
	@Override
	public int getDefaultPriorityIdForUser(int user_id) {
		String sql = "select min(id) from Priority where user_id = ?";
		int id = jdbcTemplate.queryForInt(sql, user_id);
		return id;
	}

	/**{@inheritDoc}*/
	@Override
	public void changePriorityToDefault(int priority_id, int default_id) {
		String sql = "Update Priority set id = ? where id = ?";
		jdbcTemplate.update(sql, default_id, priority_id);		
	}

	/**{@inheritDoc}*/
	@Override
	public void deletePriority(int id) {
		String sql = "delete from Priority where id = ?";
		jdbcTemplate.update(sql, id);
	}

	/**{@inheritDoc}*/
	@Override
	public void deleteAllUsers() {
		String sql = "delete from User";
		jdbcTemplate.update(sql);
	}

	/**{@inheritDoc}*/
	@Override
	public void deleteAllTasks() {
		String sql = "delete from Task";
		jdbcTemplate.update(sql);
	}

	/**{@inheritDoc}*/
	@Override
	public void deleteAllFolders() {
		String sql = "delete from Folder";
		jdbcTemplate.update(sql);
	}

	/**{@inheritDoc}*/
	@Override
	public void deleteAllPriorities() {
		String sql = "delete from Priority";
		jdbcTemplate.update(sql);
	}

}