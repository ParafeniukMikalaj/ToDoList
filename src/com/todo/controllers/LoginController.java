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
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

	protected final Log logger = LogFactory.getLog(getClass());

	@RequestMapping(value = "/login.htm", method = RequestMethod.GET)
	public ModelAndView showForm() throws ServletException, IOException {
		return new ModelAndView("login");
	}

	@RequestMapping(value = "/loginfailure.htm", method = RequestMethod.GET)
	public String loginFailure(ModelMap model) throws ServletException,
			IOException {
		model.addAttribute("error", "true");
		return "login";
	}

	@RequestMapping(value = "/logout.htm", method = RequestMethod.GET)
	public String logout() throws ServletException, IOException {
		SecurityContextHolder.getContext().setAuthentication(null);
		return "about";
	}

}
