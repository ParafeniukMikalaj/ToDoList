package com.todo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AboutController {
	
	@RequestMapping(value = "/about.htm", method = RequestMethod.GET)
	public String showForm( ){
		return "about";
	}
	
}
