package com.todo.data.hibernate.tests;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.todo.data.ToDoProviderTests;
import com.todo.data.hibernate.HibernateToDoDataProvider;

public class HibernateToDoDataProviderTests extends ToDoProviderTests {

	@Override
	protected void initProvider() {
		ApplicationContext context = null;
		context = new FileSystemXmlApplicationContext(
				"WebContent/WEB-INF/todo-servlet.xml");
		BeanFactory beanFactory = context;
		SessionFactory factory = (SessionFactory) beanFactory
				.getBean("hibernateSessionFactory");
		HibernateToDoDataProvider provider = new HibernateToDoDataProvider();
		provider.setSessionFactory(factory);
		this.provider = provider;
	}

}
