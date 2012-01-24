package com.todo.controllers;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.todo.data.DataProviderFactory;
import com.todo.data.ToDoDataProvider;
import com.todo.entities.Folder;
import com.todo.entities.Priority;
import com.todo.entities.Task;
import com.todo.entities.User;
import com.todo.logic.PriorityLogic;

@Controller
@RequestMapping(value = "/tasks")
public class TaskController {

	private static ToDoDataProvider provider;
	private static String status = "status";
	private static String ok = "ok";
	private static String error = "error";
	private static String dataString = "data";
	private static String message = "message";

	@Resource(name = "dataProviderFactory")
	public void setProviderFactory(DataProviderFactory providerFactory) {
		TaskController.provider = providerFactory.getDataProvider();
	}

	@RequestMapping(value = "/home.htm", method = RequestMethod.GET)
	public String getView(HttpServletRequest request, HttpSession session) throws ServletException,
			IOException {
		String userName = request.getUserPrincipal().getName();
		User u = provider.getUserByName(userName);
		session.setAttribute("user_id", u.getId());
		return "tasks";
	}

	@ResponseBody
	@RequestMapping(value = "/loadsettings.htm", method = RequestMethod.GET, produces="application/json")
	public String loadSettings(HttpSession session) throws ServletException, IOException, JSONException {
		JSONObject res = new JSONObject();
		try {
			int userId = (Integer) session.getAttribute("user_id");
			User u = provider.getUserById(userId);
			JSONObject settings = new JSONObject();	
			settings.put("demo", u.isDemo());
			settings.put("view", u.isView());
			res.put(status, ok);
			res.put(dataString, settings);
			return res.toString();
		} catch (JSONException e) {
			res.put(status, error);
			res.put(message, "internal error: "+e.getMessage());
			e.printStackTrace();
		} 
		return null;		
	}
	
	@ResponseBody
	@RequestMapping(value = "/loadpriorities.htm", method = RequestMethod.GET, produces="application/json")
	public String loadPriorities(HttpSession session) throws ServletException, IOException, JSONException {
		JSONObject res = new JSONObject();
		try {
			int userId = (Integer) session.getAttribute("user_id");
			ArrayList<Priority> priorities = provider.getPrioritiesForUser(userId);
			JSONArray array = new JSONArray(priorities);	
			res.put(status, ok);
			res.put(dataString, array);
			return res.toString();
		} catch (JSONException e) {
			res.put(status, error);
			res.put(message, "internal error: "+e.getMessage());
			e.printStackTrace();
		} 
		return null;		
	}
	
	@ResponseBody
	@RequestMapping(value = "/updatesettings.htm", method = RequestMethod.POST, produces = "application/json")
	public String updateSettings(HttpServletRequest request, HttpSession session) throws ServletException,
			IOException, JSONException {
		String data = URLDecoder.decode(request.getParameter("data"), "UTF-8");
		JSONObject res = new JSONObject();
		try {
			int userId = (Integer) session.getAttribute("user_id");
			User u = provider.getUserById(userId);
			JSONObject obj = new JSONObject(data);
			if(obj.has("view")&&!obj.isNull("view"))				
				u.setView(obj.getBoolean("view"));
			if(obj.has("demo")&&!obj.isNull("demo"))
				u.setDemo(obj.getBoolean("demo"));
			provider.updateUser(u);	
			res.put(status, ok);
			return res.toString();
		} catch (JSONException e) {
			res.put(status, error);
			res.put(message, "internal error: "+e.getMessage());
			e.printStackTrace();
		} 
		return null;
	}
	
	@ResponseBody
	@RequestMapping(value = "/gettasks.htm", method = RequestMethod.POST, produces="application/json")
	public String getTasks(HttpServletRequest request, HttpSession session) throws ServletException, IOException, JSONException {
		String data = URLDecoder.decode(request.getParameter("data"), "UTF-8");
		JSONObject res = new JSONObject();
		try {
			int userId = (Integer) session.getAttribute("user_id");
			JSONObject obj = new JSONObject(data);
			ArrayList<Task> tasks = provider.getSubTasks(userId, obj.getInt("folder_id"));
			JSONArray array = new JSONArray(tasks);			
			res.put(status, ok);
			res.put(dataString, array.toString());
			return res.toString();
		} catch (JSONException e) {
			res.put(status, error);
			res.put(message, "internal error: "+e.getMessage());
			e.printStackTrace();
		} 
		return null;		
	}

	@ResponseBody
	@RequestMapping(value = "/getfolders.htm", method = RequestMethod.POST, produces = "application/json")
	public String getFolders(HttpServletRequest request, HttpSession session) throws ServletException,
			IOException, JSONException {
		String data = URLDecoder.decode(request.getParameter("data"), "UTF-8");
		JSONObject res = new JSONObject();
		try {
			int userId = (Integer) session.getAttribute("user_id");
			JSONObject obj = new JSONObject(data);
			ArrayList<Folder> folders = provider.getSubFolders(userId, obj.getInt("parent_id"));
			JSONArray array = new JSONArray(folders);			
			res.put(status, ok);
			res.put(dataString, array.toString());
			return res.toString();
		} catch (JSONException e) {
			res.put(status, error);
			res.put(message, "internal error: "+e.getMessage());
			e.printStackTrace();
		} 
		return null;
	}

	@ResponseBody
	@RequestMapping(value = "/createtasks.htm", method = RequestMethod.POST, consumes = "application/json")
	public String createTask(@RequestBody String body) throws ServletException,
			IOException {
		return "tasks";
	}

	@ResponseBody
	@RequestMapping(value = "/createfolder.htm", method = RequestMethod.POST, produces = "application/json")
	public String createFolder(HttpServletRequest request, HttpSession session) throws ServletException,
			IOException, JSONException {
		String data = URLDecoder.decode(request.getParameter("data"), "UTF-8");
		JSONObject res = new JSONObject();
		try {
			int userId = (Integer) session.getAttribute("user_id");
			JSONObject obj = new JSONObject(data);
			int parent_id = obj.getInt("parent_id");
			String name = obj.getString("name");
			
			Folder f = new Folder();
			f.setParentId(parent_id);
			f.setUserId(userId);
			f.setDescription(name);
			int id = provider.createFolder(f);		
			res.put(status, ok);
			res.put("id", id);
			return res.toString();
		} catch (JSONException e) {
			res.put(status, error);
			res.put(message, "internal error: "+e.getMessage());
			e.printStackTrace();
		} 
		return null;
	}

	@ResponseBody
	@RequestMapping(value = "/updatetask.htm", method = RequestMethod.POST, consumes = "application/json")
	public String updateTask(@RequestBody String body) throws ServletException,
			IOException {
		return "tasks";
	}

	@ResponseBody
	@RequestMapping(value = "/updatefolder.htm", method = RequestMethod.POST, produces = "application/json")
	public String updateFolder(HttpServletRequest request, HttpSession session) throws ServletException,
			IOException, JSONException {
		String data = URLDecoder.decode(request.getParameter("data"), "UTF-8");
		JSONObject res = new JSONObject();
		try {
			JSONObject obj = new JSONObject(data);
			int folder_id = obj.getInt("folder_id");
			Folder f = provider.getFolderById(folder_id);
			if(obj.has("name"))
				f.setDescription(obj.getString("name"));
			if(obj.has("parent_id"))
				f.setParentId(obj.getInt("parent_id"));
			provider.updateFolder(f);	
			res.put(status, ok);
			return res.toString();
		} catch (JSONException e) {
			res.put(status, error);
			res.put(message, "internal error: "+e.getMessage());
			e.printStackTrace();
		} 
		return null;
	}
	
	@ResponseBody
	@RequestMapping(value = "/updatepriority.htm", method = RequestMethod.POST, produces = "application/json")
	public String updatePriority(HttpServletRequest request, HttpSession session) throws ServletException,
			IOException, JSONException {
		String data = URLDecoder.decode(request.getParameter("data"), "UTF-8");
		JSONObject res = new JSONObject();
		try {
			JSONObject obj = new JSONObject(data);
			int priority_id = obj.getInt("priority_id");
			Priority p = provider.getPriorityById(priority_id);
			if(obj.has("description"))
				p.setDescription(obj.getString("description"));
			if(obj.has("color"))
				p.setColor(obj.getString("color"));
			provider.updatePriority(p);	
			res.put(status, ok);
			return res.toString();
		} catch (JSONException e) {
			res.put(status, error);
			res.put(message, "internal error: "+e.getMessage());
			e.printStackTrace();
		} 
		return null;
	}
	
	@ResponseBody
	@RequestMapping(value = "/createpriority.htm", method = RequestMethod.POST, produces = "application/json")
	public String createPriority(HttpServletRequest request, HttpSession session) throws ServletException,
			IOException, JSONException {
		String data = URLDecoder.decode(request.getParameter("data"), "UTF-8");
		JSONObject res = new JSONObject();
		try {
			JSONObject obj = new JSONObject(data);
			Priority p = new Priority();
			p.setUserId((Integer) session.getAttribute("user_id"));
			p.setDescription(obj.getString("description"));
			p.setColor(obj.getString("color"));
			provider.createPriority(p);
			res.put(status, ok);
			return res.toString();
		} catch (JSONException e) {
			res.put(status, error);
			res.put(message, "internal error: "+e.getMessage());
			e.printStackTrace();
		} 
		return null;
	}
	
	@ResponseBody
	@RequestMapping(value = "/deletepriority.htm", method = RequestMethod.POST, produces = "application/json")
	public String deletePriority(HttpServletRequest request, HttpSession session) throws ServletException,
			IOException, JSONException {
		String data = URLDecoder.decode(request.getParameter("data"), "UTF-8");
		JSONObject res = new JSONObject();
		try {
			JSONObject obj = new JSONObject(data);
			int userId = (Integer) session.getAttribute("user_id");
			int priority_id = obj.getInt("priority_id");
			if(PriorityLogic.deletePriority(priority_id, userId))
				res.put(status, ok);
			else{
				res.put(status, error);
				res.put(message, "this is default prioority. couldn't be deleted");
			}
			return res.toString();
		} catch (JSONException e) {
			res.put(status, error);
			res.put(message, "internal error: "+e.getMessage());
			e.printStackTrace();
		} 
		return null;
	}

	@ResponseBody
	@RequestMapping(value = "/deletetasks.htm", method = RequestMethod.POST, consumes = "application/json")
	public String deleteTask(@RequestBody String body) throws ServletException,
			IOException {
		return "tasks";
	}

	@ResponseBody
	@RequestMapping(value = "/deletefolder.htm", method = RequestMethod.POST, produces = "application/json")
	public String deleteFolder(HttpServletRequest request, HttpSession session) throws ServletException,
			IOException, JSONException {
		String data = URLDecoder.decode(request.getParameter("data"), "UTF-8");
		JSONObject res = new JSONObject();
		try {
			JSONObject obj = new JSONObject(data);
			int folder_id = obj.getInt("folder_id");
			provider.deleteFolder(folder_id);
			res.put(status, ok);
			return res.toString();
		} catch (JSONException e) {
			res.put(status, error);
			res.put(message, "internal error: "+e.getMessage());
			e.printStackTrace();
		} 
		return null;
	}

}
