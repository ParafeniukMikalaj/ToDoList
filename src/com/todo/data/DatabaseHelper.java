package com.todo.data;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.springframework.core.io.support.PropertiesLoaderUtils;

public class DatabaseHelper {
	private Properties prop = null;
	private static SimpleDateFormat sdf = null;
	private String filename = "../../database.properties";

	private Properties getProperties() {
		if (prop != null)
			return prop;
		prop = new Properties();
		try {
			prop = PropertiesLoaderUtils
					.loadAllProperties(filename);
		} catch (FileNotFoundException e) {
			System.err.println("File database.properties not found");
			return null;
		} catch (IOException e) {
			System.err
					.println("IOException while trying to load connection string");
			return null;
		}
		return prop;
	}

	public String getConnectionString() {
		return getProperties().getProperty("database.connectionString");
	}

	public String getUserName() {
		return getProperties().getProperty("database.userName");
	}

	public String getUserPassword() {
		return getProperties().getProperty("database.userPassword");
	}

	public static String dateToString(Date date) {
		java.util.Date dt = new java.util.Date();
		if (sdf == null)
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(dt);
	}
	
	public static java.sql.Date convertToSql(java.util.Date date) {
		return new java.sql.Date(date.getTime());
	}
}
