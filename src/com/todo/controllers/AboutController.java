package com.todo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * This class will response to /about.htm request
 * @author Mikalai
 */
@Controller
public class AboutController {
	
	/**
	 * Response to get method on /about.htm
	 * @return "about" - the name of the view
	 */
	@RequestMapping(value = "/about.htm", method = RequestMethod.GET)
	public String showForm( ){
		return "about";
	}
	
}
