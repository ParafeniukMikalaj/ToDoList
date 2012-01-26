package com.todo.controllers;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * This class defines web-logic for login process
 * @author Mikalai
 */
@Controller
public class LoginController {

	protected final Log logger = LogFactory.getLog(getClass());

	/**
	 * This method responses to get method for /login.htm 
	 * @return "login" - the name of the view
	 */
	@RequestMapping(value = "/login.htm", method = RequestMethod.GET)
	public String showForm() {
		return "login";
	}

	/**
	 * This method responses to get method for /login.htm 
	 * when user login was invalid
	 * @return "login" - the name of the view
	 */
	@RequestMapping(value = "/loginfailure.htm", method = RequestMethod.GET)
	public String loginFailure(ModelMap model) throws ServletException,
			IOException {
		model.addAttribute("error", "true");
		return "login";
	}

	/**
	 * This method responses to get method for /logout.htm
	 * used by Spring Security
	 * @return "about" the name of the view
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping(value = "/logout.htm", method = RequestMethod.GET)
	public String logout() {
		SecurityContextHolder.getContext().setAuthentication(null);
		return "about";
	}

}
