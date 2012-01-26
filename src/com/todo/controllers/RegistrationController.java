package com.todo.controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.todo.logic.RegistrationLogic;

/**
 * This class defines web-logic for registration process
 * @author Mikalai
 */
@Controller
public class RegistrationController {

	protected final Log logger = LogFactory.getLog(getClass());
	UserFormValidator validator = null;

	/**
	 * This is auto wired method to setup {@link UserFormValidator}
	 * @param validator
	 */
	@Autowired
	public void setValidator(UserFormValidator validator) {
		this.validator = validator;
	}

	/**
	 * This method responses to get method for /register.htm
	 * @return ModelAndView with {@link UserForm} parameter
	 */
	@RequestMapping(value = "/register.htm", method = RequestMethod.GET)
	public ModelAndView showForm(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		return new ModelAndView("register", "user", new UserForm());
	}

	/**
	 * This method responses to post method for /register.htm
	 * Handles user registration logic
	 * @return success : about view, fail : register view
	 */
	@RequestMapping(value = "/register.htm", method = RequestMethod.POST)
	public ModelAndView handleRegistrationData(
			@ModelAttribute("user") @Valid UserForm user, BindingResult result,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		validator.validate(user, result);
		if (result.hasErrors())
			return new ModelAndView("register", "user", user);
		RegistrationLogic.registerUser(user);		
		return new ModelAndView("about");
	}

	/**
	 * This method responses ajax calls to check if user name is available
	 * Name must be passed as post parameter with "name" key
	 * @return true or false in json format
	 */
	@RequestMapping(value = "/check-users", method = RequestMethod.POST)
	public void checkUserName(@RequestParam("name") String userName,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		boolean bl = RegistrationLogic.userAvailable(userName);
		response.getWriter().append(String.valueOf(bl));
	}

	/**
	 * This method responses ajax calls to check if email is available
	 * Email must be passed as post parameter with "email" key
	 * @return true or false in json format
	 */
	@RequestMapping(value = "/check-email", method = RequestMethod.POST)
	public void checkEmail(@RequestParam("email") String email,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		boolean bl = RegistrationLogic.emailAvailable(email);
		response.getWriter().append(String.valueOf(bl));
	}

}
