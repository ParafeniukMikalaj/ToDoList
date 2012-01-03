package com.todo.data;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

import org.springframework.core.io.support.PropertiesLoaderUtils;

public class DataProviderHelper {
	private Properties prop = null;
	private String defaultStringName = "default";
	private String fileName = "../../dataprovider.properties";

	private Properties getProperties() {
		if (prop != null)
			return prop;
		prop = new Properties();
		try {
			prop = PropertiesLoaderUtils.loadAllProperties(fileName);
		} catch (FileNotFoundException e) {
			System.err.println("File dataprovider.properties not found");
			return null;
		} catch (IOException e) {
			System.err
					.println("IOException while trying to load info about data providers");
			return null;
		}
		return prop;
	}

	public ArrayList<String> getProvidersNames() {
		getProperties();
		ArrayList<String> names = new ArrayList<String>();
		Enumeration<Object> em = prop.keys();
		while (em.hasMoreElements())
			names.add(em.nextElement().toString());
		if (names.contains(defaultStringName))
			names.remove(defaultStringName);
		return names;
	}

	public String getDefaultProvider() {
		getProperties();
		String name = prop.getProperty(defaultStringName);
		return prop.getProperty(name);
	}

}
