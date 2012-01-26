package com.todo.data;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * This is component class which defines logic
 * of instantiation of data provider
 * @author Mikalai
 *
 */
@Component("dataProviderFactory")
public class DataProviderFactory {
	private ToDoDataProvider provider;
	protected final Log logger = LogFactory.getLog(getClass());

	/**
	 * this is auto wired method which creates class by 
	 * passed to it class name
	 * @param providerClassName class name to instantiate
	 */
	@Autowired
	public void setProviderClassName(
			@Value("#{'${dataprovider.default}'}") String providerClassName) {
		logger.debug("Creating data provider.");
		if (provider == null) {
			Class<?> fc;
			try {
				fc = Class.forName(providerClassName);
				provider = (ToDoDataProvider) fc.newInstance();
			} catch (ClassNotFoundException e) {
				logger.error("error in creating dataprovider", e);
			} catch (InstantiationException e) {
				logger.error("error in creating dataprovider", e);
			} catch (IllegalAccessException e) {
				logger.error("error in creating dataprovider", e);
			}
		}
	}

	public ToDoDataProvider getDataProvider() {
		return provider;
	}
}
