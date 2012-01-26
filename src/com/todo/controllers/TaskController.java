package com.todo.controllers;

import java.io.IOException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.todo.data.DataProviderFactory;
import com.todo.data.ToDoDataProvider;
import com.todo.entities.Folder;
import com.todo.entities.Priority;
import com.todo.entities.Task;
import com.todo.entities.User;
import com.todo.logic.FolderLogic;
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
	
	private SimpleDateFormat sdf;
	
	private JSONObject mapTask(Task t) throws JSONException{
		JSONObject obj = new JSONObject();
		obj.put("id", t.getId());
		obj.put("creationDate", sdf.format(t.getCreationDate()));
		obj.put("expirationDate", sdf.format(t.getExpirationDate()));
		obj.put("description", t.getDescription());
		obj.put("x", t.getX());
		obj.put("y", t.getY());
		obj.put("folderId", t.getFolderId());
		obj.put("priorityId", t.getPriorityId());
		obj.put("userId", t.getUserId());
		obj.put("delayedTimes", t.getDelayedTimes());
		return obj;
	}
	
	@Autowired
	public void setSDF(){
		sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	}

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
			return res.toString();
		} 		
	}
	
	@ResponseBody
	@RequestMapping(value = "/loadpriorities.htm", method = RequestMethod.GET, produces="application/json")
	public String loadPriorities(HttpSession session) throws ServletException, IOException, JSONException {
		JSONObject res = new JSONObject();
		try {
			int userId = (Integer) session.getAttribute("user_id");
			ArrayList<Priority> priorities = provider.getPrioritiesForUser(userId);
			int default_id = provider.getDefaultPriorityIdForUser(userId);
			JSONArray array = new JSONArray(priorities);	
			res.put(status, ok);
			res.put("default_priority", default_id);
			res.put(dataString, array);
			return res.toString();
		} catch (JSONException e) {
			res.put(status, error);
			res.put(message, "internal error: "+e.getMessage());
			return res.toString();
		} 	
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
			return res.toString();
		} 
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
			JSONArray array = new JSONArray();	
			for(Task task : tasks)
				array.put(mapTask(task));
			res.put(status, ok);
			res.put(dataString, array.toString());
			return res.toString();
		} catch (JSONException e) {
			res.put(status, error);
			res.put(message, "internal error: "+e.getMessage());
			return res.toString();
		} 		
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
			return res.toString();
		} 
	}

	@ResponseBody
	@RequestMapping(value = "/createtask.htm", method = RequestMethod.POST, produces = "application/json")
	public String createTask(HttpServletRequest request, HttpSession session) throws ServletException,
			IOException, JSONException, ParseException {
		String data = URLDecoder.decode(request.getParameter("data"), "UTF-8");
		JSONObject res = new JSONObject();
		try {
			int userId = (Integer) session.getAttribute("user_id");
			JSONObject obj = new JSONObject(data);
			int priorityId = obj.getInt("priorityId");
			int folderId = obj.getInt("folderId");
			String description = obj.getString("description");
			Date expiration = sdf.parse(obj.getString("expirationDate"));
			
			Task t = new Task();
			t.setUserId(userId);
			t.setDescription(description);
			t.setPriorityId(priorityId);
			t.setDelayedTimes(0);
			t.setFolderId(folderId);
			t.setCreationDate(new Date());
			t.setExpirationDate(expiration);
			
			int id = provider.createTask(t);
			JSONObject post = new JSONObject();
			post.put("userId", userId);
			post.put("taskId", id);
			post.put("delayedTimes", t.getDelayedTimes());
			post.put("creationDate", sdf.format(t.getCreationDate()));
			
			res.put("data", post);
			res.put(status, ok);
			return res.toString();
		} catch (JSONException e) {
			res.put(status, error);
			res.put(message, "internal error: "+e.getMessage());
			return res.toString();
		} 
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
			return res.toString();
		} 
	}

	@ResponseBody
	@RequestMapping(value = "/updatetask.htm", method = RequestMethod.POST, produces = "application/json")
	public String updateTask(HttpServletRequest request, HttpSession session) throws ServletException,
			IOException, JSONException, ParseException {
		String data = URLDecoder.decode(request.getParameter("data"), "UTF-8");
		JSONObject res = new JSONObject();
		try {
			JSONObject obj = new JSONObject(data);
			int taskId = obj.getInt("taskId");
			Task t = provider.getTaskById(taskId);
			if(obj.has("priorityId"))
				t.setPriorityId(obj.getInt("priorityId"));
			if(obj.has("expirationDate"))
				t.setExpirationDate(sdf.parse(obj.getString("expirationDate")));
				t.setDelayedTimes(t.getDelayedTimes()+1);
			if(obj.has("description"))
				t.setDescription(obj.getString("description"));
			if(obj.has("x")){
				double d = obj.getDouble("x");
				t.setX((int) d);
			}
			if(obj.has("y")){
				t.setY((int) obj.getDouble("y"));
			}
			provider.updateTask(t);
			JSONObject post = new JSONObject();
			post.put("description", t.getDescription());
			post.put("delayedTimes", t.getDelayedTimes());
			post.put("priorityId", t.getPriorityId());
			post.put("expirationDate", sdf.format(t.getExpirationDate()));
			post.put("x", t.getX());
			post.put("y", t.getY());
			res.put("data", post);
			res.put(status, ok);
			return res.toString();
		} catch (JSONException e) {
			res.put(status, error);
			res.put(message, "internal error: "+e.getMessage());
			return res.toString();
		} 
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
			return res.toString();
		} 
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
			return res.toString();
		} 
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
			int user_id = (Integer) session.getAttribute("user_id");
			p.setUserId(user_id);
			p.setDescription(obj.getString("description"));
			p.setColor(obj.getString("color"));
			JSONObject value = new JSONObject();			
			int id = provider.createPriority(p);
			value.put("priority_id", id);
			value.put("user_id", user_id);
			res.put("data", value);
			res.put(status, ok);
			return res.toString();
		} catch (JSONException e) {
			res.put(status, error);
			res.put(message, "internal error: "+e.getMessage());
			return res.toString();
		} 
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
				res.put(message, "this is default priority. couldn't be deleted");
			}
			return res.toString();
		} catch (JSONException e) {
			res.put(status, error);
			res.put(message, "internal error: "+e.getMessage());
			return res.toString();
		} 
	}

	@ResponseBody
	@RequestMapping(value = "/deletetask.htm", method = RequestMethod.POST, produces = "application/json")
	public String deleteTask(HttpServletRequest request, HttpSession session) throws ServletException,
			IOException, JSONException {
		String data = URLDecoder.decode(request.getParameter("data"), "UTF-8");
		JSONObject res = new JSONObject();
		try {
			JSONObject obj = new JSONObject(data);
			int taskId = obj.getInt("taskId");
			provider.deleteTask(taskId);
			res.put(status, ok);
			return res.toString();
		} catch (JSONException e) {
			res.put(status, error);
			res.put(message, "internal error: "+e.getMessage());
			return res.toString();
		} 
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
			FolderLogic.deleteFolder(folder_id);
			res.put(status, ok);
			return res.toString();
		} catch (JSONException e) {
			res.put(status, error);
			res.put(message, "internal error: "+e.getMessage());
			return res.toString();
		} catch (Exception e) {
			res.put(status, error);
			res.put(message, e.getMessage());
			return res.toString();
		} 
	}

}
