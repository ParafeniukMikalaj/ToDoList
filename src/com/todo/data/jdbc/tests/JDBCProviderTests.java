package com.todo.data.jdbc.tests;

import com.todo.data.ToDoProviderTests;
import com.todo.data.jdbc.JDBCToDoDataProvider;

public class JDBCProviderTests extends ToDoProviderTests {

	@Override
	protected void initProvider() {
		JDBCToDoDataProvider instanceOfProvider = new JDBCToDoDataProvider();
		instanceOfProvider.SetTestDataSource();
		provider = instanceOfProvider;
	}

}
