package com.todo.data.jdbc;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.todo.data.ToDoProviderTests;
import com.todo.data.jdbc.JDBCToDoDataProvider;

/**
 * JDBC based implementation of initialize method of
 * {@link com.todo.data.ToDoProviderTests}
 * @author Mikalai
 */
public class JDBCProviderTests extends ToDoProviderTests {

	@Override
	protected void initProvider() {
		ApplicationContext context = null;
		context = new FileSystemXmlApplicationContext(
				"WebContent/WEB-INF/todo-data-access.xml");
		BeanFactory beanFactory = context;
		JDBCToDoDataProvider provider = (JDBCToDoDataProvider) beanFactory
				.getBean("jdbcProvider");
		this.provider = provider;
	}

}
