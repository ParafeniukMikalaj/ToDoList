package com.todo.data;

public class DataProviderFactory {
	private static DataProviderFactory instance;
	private ToDoDataProvider provider;

	private DataProviderFactory() {

	}

	public static DataProviderFactory getInstance() {
		if (instance == null)
			instance = new DataProviderFactory();
		return instance;
	}

	public ToDoDataProvider getDataProvider() {
		DataProviderHelper helper = new DataProviderHelper();
		String className = helper.getDefaultProvider();
		try {
			Class<?> fc = Class.forName(className);
			if (provider != null)
				provider.close();
			provider = (ToDoDataProvider) fc.newInstance();
			return provider;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

}
