package com.todo.data.hibernate;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import com.todo.data.TestableToDoDataProvider;
import com.todo.entities.Folder;
import com.todo.entities.Priority;
import com.todo.entities.Task;
import com.todo.entities.User;

/**
 * Hibernate based implementation of {@link com.todo.data.ToDoDataProvider}
 * @author Mikalai
 */
@Component(value = "hibernateProvider")
@SuppressWarnings("unchecked")
public class HibernateToDoDataProvider implements TestableToDoDataProvider {

	private static SessionFactory sessionFactory;

	@Resource(name = "hibernateSessionFactory")
	public void setSessionFactory(SessionFactory sessionFactory) {
		HibernateToDoDataProvider.sessionFactory = sessionFactory;
	}

	/**{@inheritDoc}*/
	@Override
	public int createTask(Task task) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			session.save(task);
			Query query = session.createQuery(
					"select max(id) from Task where user_id = :user_id and folder_id = :folder_id")
					.setInteger("user_id", task.getUserId())
					.setInteger("folder_id", task.getFolderId());
			int id = ((Integer) query.uniqueResult()).intValue();
			session.getTransaction().commit();
			return id;
		} catch (HibernateException e) {
			session.getTransaction().rollback();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return 0;
	}

	/**{@inheritDoc}*/
	@Override
	public Task getTaskById(int id) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			return (Task) session.get(Task.class, id);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	/**{@inheritDoc}*/
	@Override
	public ArrayList<Task> getSubTasks(int folder_id) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			Query query = session
					.createQuery(
							"from Task where folder_id = :folder_id")
					.setInteger("folder_id", folder_id);
			return new ArrayList<Task>(query.list());
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	/**{@inheritDoc}*/
	@Override
	public void updateTask(Task task) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			session.update(task);
			session.getTransaction().commit();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	/**{@inheritDoc}*/
	@Override
	public void deleteTask(int id) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			session.createQuery(
					"delete from Task where id = " + String.valueOf(id))
					.executeUpdate();
			session.getTransaction().commit();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	/**{@inheritDoc}*/
	@Override
	public int createFolder(Folder folder) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			session.save(folder);
			Query query = session.createQuery(
					"select max(id) from Folder where user_id = :user_id")
					.setInteger("user_id", folder.getUserId());
			int id = ((Integer) query.uniqueResult()).intValue();
			session.getTransaction().commit();
			return id;
		} catch (HibernateException e) {
			session.getTransaction().rollback();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return 0;
	}

	/**{@inheritDoc}*/
	@Override
	public Folder getFolderById(int id) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			return (Folder) session.get(Folder.class, id);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	/**{@inheritDoc}*/
	@Override
	public ArrayList<Folder> getSubFolders(int parent_id) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			Query query = session
					.createQuery(
							" from Folder where parent_id = :parent_id")
					.setInteger("parent_id", parent_id);
			return new ArrayList<Folder>(query.list());
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	/**{@inheritDoc}*/
	@Override
	public void updateFolder(Folder folder) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			session.update(folder);
			session.getTransaction().commit();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	/**{@inheritDoc}*/
	@Override
	public void deleteFolder(int id) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			session.createQuery(
					"delete from Folder where id = " + String.valueOf(id))
					.executeUpdate();
			session.getTransaction().commit();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

	}

	/**{@inheritDoc}*/
	@Override
	public int createUser(User user) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			session.save(user);
			Query query = session.createQuery(
					"select max(id) from User where name = :name and password = :password")
					.setString("name", user.getName())
					.setString("password", user.getPassword());
			int id = ((Integer) query.uniqueResult()).intValue();
			session.getTransaction().commit();
			return id;
		} catch (HibernateException e) {
			session.getTransaction().rollback();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return 0;
	}

	/**{@inheritDoc}*/
	@Override
	public User getUserById(int id) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			return (User) session.get(User.class, id);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	/**{@inheritDoc}*/
	@Override
	public User getUserByName(String name) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Query query = session.createQuery("from User where name = :name")
					.setString("name", name);
			List<User> users = query.list();
			if (users.size() == 0)
				return null;
			return users.get(0);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	/**{@inheritDoc}*/
	@Override
	public boolean usernameAvailable(String name) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Query query = session.createQuery(
					"select count(name) from User where name = :name")
					.setString("name", name);
			int count = ((Long) query.uniqueResult()).intValue();
			if (count == 0)
				return true;
			return false;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	/**{@inheritDoc}*/
	@Override
	public boolean emailAvailable(String email) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Query query = session.createQuery(
					"select count(name) from User where email = :email")
					.setString("email", email);
			int count = ((Long) query.uniqueResult()).intValue();
			if (count == 0)
				return true;
			return false;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	/**{@inheritDoc}*/
	@Override
	public void updateUser(User user) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			session.update(user);
			session.getTransaction().commit();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	/**{@inheritDoc}*/
	@Override
	public void deleteUser(int id) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			sessionFactory
					.openSession()
					.createQuery(
							"delete from User where id = " + String.valueOf(id))
					.executeUpdate();
			session.getTransaction().commit();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

	}

	/**{@inheritDoc}*/
	@Override
	public int createPriority(Priority priority) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			session.save(priority);
			Query query = session.createQuery(
					"select max(id) from Priority where user_id = :user_id")
					.setInteger("user_id", priority.getUserId());
			int id = ((Integer) query.uniqueResult()).intValue();
			session.getTransaction().commit();
			return id;
		} catch (HibernateException e) {
			session.getTransaction().rollback();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return 0;
	}

	/**{@inheritDoc}*/
	@Override
	public Priority getPriorityById(int id) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			return (Priority) session.get(Priority.class, id);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	/**{@inheritDoc}*/
	@Override
	public ArrayList<Priority> getPrioritiesForUser(int user_id) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Query query = session
					.createQuery(
							"from Priority where user_id = :user_id")
					.setInteger("user_id", user_id);
			return new ArrayList<Priority>(query.list());
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	/**{@inheritDoc}*/
	@Override
	public void updatePriority(Priority priority) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			session.update(priority);
			session.getTransaction().commit();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}	

	/**{@inheritDoc}*/
	@Override
	public int getDefaultPriorityIdForUser(int user_id) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			Query query = session.createQuery(
					"select min(id) from Priority where user_id = :user_id")
					.setInteger("user_id", user_id);
			int id = ((Integer) query.uniqueResult()).intValue();
			session.getTransaction().commit();
			return id;
		} catch (HibernateException e) {
			session.getTransaction().rollback();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return 0;
	}

	/**{@inheritDoc}*/
	@Override
	public void changePriorityToDefault(int priority_id, int default_id) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			Query query = session.createQuery(
					"update Task set priority_id = :default_id where priority_id = :priority_id")
					.setInteger("priority_id", priority_id)
					.setInteger("default_id", default_id);
			query.executeUpdate();
			session.getTransaction().commit();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}		
	}

	/**{@inheritDoc}*/
	@Override
	public void deletePriority(int id) {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			sessionFactory
					.openSession()
					.createQuery(
							"delete from Priority where id = "
									+ String.valueOf(id)).executeUpdate();
			session.getTransaction().commit();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	/**{@inheritDoc}*/
	@Override
	public void deleteAllUsers() {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			session.createQuery("delete from User").executeUpdate();
			session.getTransaction().commit();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	/**{@inheritDoc}*/
	@Override
	public void deleteAllTasks() {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			session.createQuery("delete from Task").executeUpdate();
			session.getTransaction().commit();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	/**{@inheritDoc}*/
	@Override
	public void deleteAllFolders() {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			session.createQuery("delete from Folder").executeUpdate();
			session.getTransaction().commit();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	/**{@inheritDoc}*/
	@Override
	public void deleteAllPriorities() {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			session.createQuery("delete from Priority").executeUpdate();
			session.getTransaction().commit();
		} catch (HibernateException e) {
			session.getTransaction().rollback();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

}
