package com.todo.controllers.tests;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;

import com.todo.controllers.ToDoController;

import junit.framework.TestCase;

public class ToDoControllerTests extends TestCase {

	protected final Log logger = LogFactory.getLog(getClass());

	public void testHandleRequestView() throws Exception {
		ToDoController controller = new ToDoController();
		ModelAndView modelAndView = controller.handleRequest(null, null);
		assertEquals("hello", modelAndView.getViewName());
		assertNotNull(modelAndView.getModel());
		Map<?, ?> modelMap = (Map<?, ?>) modelAndView.getModel().get("model");
        String nowValue = (String) modelMap.get("now");;
		assertNotNull(nowValue);
		Object tasks = modelMap.get("tasks");
		assertNotNull(tasks);

	}
}