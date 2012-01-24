package com.todo.data;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("dataProviderFactory")
public class DataProviderFactoryBean {
	private ToDoDataProvider provider;
	protected final Log logger = LogFactory.getLog(getClass());
	private String providerClassName;
	
	@Autowired
	public void setProviderClassName(@Value("#{'${dataprovider.default}'}") String providerClassName) {
		logger.debug("Creating data provider");
		if(provider == null){
			Class<?> fc;
			try {
				fc = Class.forName(providerClassName);
				provider = (ToDoDataProvider)fc.newInstance();
			} catch (ClassNotFoundException e) {
				logger.error("error in creating dataprovider", e);
			} catch (InstantiationException e) {
				logger.error("error in creating dataprovider", e);
			} catch (IllegalAccessException e) {
				logger.error("error in creating dataprovider", e);
			}			
		}
	}
	
	public ToDoDataProvider getDataProvider(){
		return provider;
	}
}
