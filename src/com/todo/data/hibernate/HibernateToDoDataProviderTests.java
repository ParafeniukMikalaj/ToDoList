package com.todo.data.hibernate;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.todo.data.ToDoProviderTests;
import com.todo.data.hibernate.HibernateToDoDataProvider;

/**
 * Hibernate based implementation of init method of
 * {@link com.todo.data.ToDoProviderTests}
 * @author Mikalai
 */
public class HibernateToDoDataProviderTests extends ToDoProviderTests {

	@Override
	protected void initProvider() {
		ApplicationContext context = null;
		context = new FileSystemXmlApplicationContext(
				"WebContent/WEB-INF/todo-data-access.xml");
		BeanFactory beanFactory = context;
		this.provider = (HibernateToDoDataProvider)beanFactory.getBean("hibernateProvider");
	}

}
