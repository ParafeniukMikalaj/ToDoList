package com.todo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ToDoController{
		
	protected final Log logger = LogFactory.getLog(getClass());	
    @RequestMapping("/main.htm")
	public String handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
    	return "home";
	}

}