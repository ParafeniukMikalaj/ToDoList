package com.todo.data.hibernate.tests;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.todo.data.ToDoProviderTests;
import com.todo.data.hibernate.HibernateToDoDataProvider;

public class HibernateToDoDataProviderTests extends ToDoProviderTests{

	@Override
	protected void initProvider() {
		Resource res = new FileSystemResource("WebContent/WEB-INF/todo-servlet.xml");
		XmlBeanFactory context = new XmlBeanFactory(res);
		SessionFactory factory = (SessionFactory)context.getBean("hibernateSessionFactory", SessionFactory.class);
		HibernateToDoDataProvider provider = new HibernateToDoDataProvider();
		provider.setSessionFactory(factory);
	}

}
