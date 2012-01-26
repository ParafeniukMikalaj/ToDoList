var priorities;
var lastFolder;
var default_priority_id;
var tasks;
var proxy;

$(document).ready(function() {
	wireEvents();	
});

/********************************************************************************
							Helper functions
********************************************************************************/
function showError(error){
	var container = $('#errorTasks');
	container.empty().append(error);
	container.fadeIn('1000');
	setTimeout(function(){
		container.fadeOut(500);
	}, 2500);
	setTimeout(function(){
		container.empty();
	}, 3000);
}

function isDemo(){
	return $('#demo').attr('checked');
}

function isListView(){
	var res = $('input#listView').attr('checked');
	if(res) return true;
	return false;
}

function wireEvents(){
	initTree();	
	
	loadPriorities();
	initPriorities();
	
	loadSettings();
	initSettings();	
	
}

function showErrorIfExist(action, data){
	if(!data){
		showError('action '+action+': unknown error data is null');
		return true;
	}
	if(!data.status){
		showError('action '+action+': unknown error data.status is null');
		return true;
	}
	if(data.status != "ok"){
		var message = '';
		if(data.message)
			message += data.message;
		else
			message += 'message unavailable';
		showError(action+' error with message: '+message);
		return true;
	}
	return false;
	
}

function dataToString(data){
	return 'data='+JSON.stringify(data);
}
/********************************************************************************
							Tree functions
********************************************************************************/
function initTree(){
	$('#treeContainer')
	.jstree({
		'plugins' : [ 'themes', 'ui', 'crrm', 'contextmenu', 'json_data'],
		'json_data' : {
			'ajax' : {
				'url' : '/ToDoList/tasks/getfolders.htm',
				'type': 'POST',
				'data' : function (n) { 
					return 'data=' + JSON.stringify({ 
						parent_id : n.attr ? n.attr('id') : 0 
					}); 
				},
				'success' : function(data, status, xhr) {
					if(!showErrorIfExist('load tasks', data)){				
						var folders = eval(data.data);
						var res = new Array();
						$(folders).each(function(index, element) {
							var folder = 
							{
								'data' : element.description,
								'attr' : {id : element.id},
								'state' : "closed"
							};
							res.push(folder);
						});
						return res;
					}
				}
			}
		}, 
	}).bind('before.jstree', function(e, data){
		if(isDemo()&&(data.func=='create_node'||data.func=='rename_node'||data.func=='delete_node'||data.func=='remove'||data.func=='rename')){
			showError('action '+data.func+' is not allowed in demo mode');
			e.stopImmediatePropagation(); 
			return false;
		}			
	})
	.bind('select_node.jstree', function (e, data) { 
		lastFolder = $(data.rslt.obj).attr('id');
		loadSubTasks(lastFolder); 
	}).bind('rename.jstree', function (e, data, blah) { 
		var newName = data.rslt.new_name;
		var oldName = data.rslt.old_name;
		var id = $(data.rslt.obj).attr('id');
		renameFolder(oldName, newName, id);
	}).bind('create.jstree', function (event, data) { 
		var parent_id = $(data.rslt.obj).parents('[id]').attr('id');
		var name = data.rslt.name;
		createFolder(name, parent_id, data.rslt.obj, data.rlbk);
	}).bind('remove.jstree', function (event, data) { 
		var id = $(data.rslt.obj).attr('id');
		deleteFolder(id, data.rlbk);
	})
	.delegate('a', 'click', function (event, data) { event.preventDefault(); });
}

function createFolder(folderName, parent_id, node, rollback){
	var postData = dataToString({
		name : folderName, 
		"parent_id" : parent_id
	});
	$.ajax({
		url : '/ToDoList/tasks/createfolder.htm',
		type : 'POST',
		data : postData,
		dataType : 'json',
		success : function(data, status, xhr) {
			if(showErrorIfExist('create folder', data))
				$.jstree.rollback(rollback);
			else
				node.attr("id", data.id);
		},
		error : function(xhr, status, error) {
			showError('error in ajax function. action: create folder');
			$.jstree.rollback(rollback);
		}
	});
}

function renameFolder(oldName, newName, id, rollback){
	var postData = dataToString({
		"name" : newName,
		"folder_id" : id
	});
	if(oldName != newName)
	$.ajax({
		url : '/ToDoList/tasks/updatefolder.htm',
		type : 'POST',
		data : postData,
		dataType : 'json',
		success : function(data, status, xhr) {
			if(showErrorIfExist('rename folder', data))
				$.jstree.rollback(rollback);
		}, 
		error : function(xhr, status, error) {
			showError('error in ajax function. action: rename folder');
			$.jstree.rollback(rollback);
		} 
	});
}

function deleteFolder(id, rollback){
	var postData = dataToString({
		"folder_id" : id
	});
	$.ajax({
		url : '/ToDoList/tasks/deletefolder.htm',
		type : 'POST',
		data : postData,
		dataType : 'json',
		success : function(data, status, xhr) {
			if(showErrorIfExist('delete folder', data))
				$.jstree.rollback(rollback);
		},
		error : function(xhr, status, error) {
			showError("error in ajax function. action: delete folder");
			$.jstree.rollback(rollback);
		} 
	});
}
/********************************************************************************
									Priority functions
********************************************************************************/
function initPriorities(){
	$('#addPriorityTemplate .priorityColor').ColorPicker({
		onSubmit: function(hsb, hex, rgb) {
			  $(this.el).css('background-color', '#'+hex);
		} 
	});	
	$('#addPriorityTemplate .add').click(function(){
		if(isDemo()){
			showError('action create is not allowed in demo mode');
			return;
		}
		var container = $(this).parent();
		var color = $('.priorityColor', container).css('background-color');
		var description = $('input[type=text]', container).val();
		createPriority(description, color);
	});	
	$('#prioritiesPlaceholder').delegate('input[type=submit].remove', 'click', function(){		
		var id = $('input[type=hidden]', $(this).parent()).val();
		if(isDemo()){
			showError('action delete is not allowed in demo mode');
			return;
		}
		deletePriority(id, $(this));
	});
	$('#prioritiesPlaceholder').delegate('.priorityName', 'dblclick', function(){
		if(isDemo()){
			showError('action edit is not allowed in demo mode');
			return;
		}
		var content = $(this).html();
		$(this).empty();
		var input = $('<input type="text" class="minified"></input>');
		input.attr('value', content);
		$(this).append(input);
		input.focus();
		input.blur(function(){
			var container = $(this).parent();
			var content = $(this).attr('value');
			var priority_id = $('input[type=hidden]', container.parent()).attr('value');
			container.empty();
			container.html(content);
			updatePriority(priority_id, content, null);			
		});
	});
}

function getPriorityColor(priority_id){
	var priority = getPriorityById(priority_id);
	if(priority != null)
		return priority.color;
	return "#DDDDDD";	
}

function getPriorityById(priority_id){
	var pos = findPriorityPosition(priority_id);
	if(pos!=null)
		return priorities[pos];
	return null;
}

function findPriorityPosition(priority_id){
	for(var i = 0; i<priorities.length;i++)
		if(priorities[i].id == priority_id)
			return i;
	return null;
}

function deletePriorityById(priority_id){
	var pos = findPriorityPosition(priority_id);
	if(pos!=null)
		priorities.splice(pos, 1);
}

function loadPriorities(){
	$.ajax({
		url : '/ToDoList/tasks/loadpriorities.htm',
		type : 'GET',
		dataType : 'json',
		success : function(data, status, xhr) {
			if(!showErrorIfExist('load priorities', data)){
				priorities = data.data;
				default_priority_id = data.default_priority;
				refreshPriorities();
			}
		}, 
		error : function(xhr, status, error) {
			showError("error occured while saving settings");
		} 
	});
}

function createPriority(description, color){
	var postData = dataToString({
		"description" : description,
		"color" : color
	});
	$.ajax({
		url : '/ToDoList/tasks/createpriority.htm',
		type : 'POST',
		data : postData,
		dataType : 'json',
		success : function(data, status, xhr) {
			if(!showErrorIfExist('create priority', data)){
				var res = eval(data.data);
				var new_priority = {
						id : res.priority_id,
						description : description,
						color : color,
						userId: res.user_id
				};
				priorities.push(new_priority);
				refreshPriorities();
				proxy.refreshTaskPriorities();
			}
		}, 
		error : function(xhr, status, error) {
			showError("error in ajax function. action: create priority");
		} 
	});
}

function updatePriority(priority_id, description, color){
	var data = new Object();
	data.priority_id = priority_id;
	if(description != null)
		data.description = description;
	if(color != null)
		data.color = color;
	var postData = dataToString(data);
	$.ajax({
		url : '/ToDoList/tasks/updatepriority.htm',
		type : 'POST',
		"data" : postData,
		dataType : 'json',
		success : function(data, status, xhr) {
			if(!showErrorIfExist('update priority', data)){
				priorityData = eval(postData);
				var priority = getPriorityById(priority_id);
				if(description != null)
					priority.description = description;
				if(color != null)
					priority.color = color;
				refreshPriorities();
				proxy.refreshTaskPriorities();
			}
		}, 
		error : function(xhr, status, error) {
			showError("error in ajax function. action: update priority");
		} 
	});
}

function deletePriority(priority_id, node){
	var postData = dataToString({
		"priority_id" : priority_id
	});
	$.ajax({
		url : '/ToDoList/tasks/deletepriority.htm',
		type : 'POST',
		data : postData,
		dataType : 'json',
		success : function(data, status, xhr) {
			if(!showErrorIfExist('delete priority', data)){
				deletePriorityById(priority_id);
				refreshPriorities();
				proxy.refreshTaskPriorities();
			}
		}, 
		error : function(xhr, status, error) {
			showError("error in ajax function. action: delete priority");
		} 
	});
}
/**only shows loaded priorities. no events wired*/
function refreshPriorities(){	
	var container = $('#prioritiesPlaceholder').empty();	
	$.each(priorities, function(key, value) {   
	     var template = $('#priorityTemplate');  
	     container.append(template.clone());
	     var current = $("#priorityTemplate", container);
	     current.removeClass('hidden');
	     var color = this.color;
	     $('.priorityColor', current).css('background-color', color).ColorPicker({
	    	 "color" : color,
	    	 onSubmit : function(hsb, hex, rgb) {
	    		 if (isDemo()) {
	    			 showError('action edit is not allowed in demo mode');
	    			 return false;
	    		 }
	    		 $(this.el).css('background-color', '#' + hex);
	    		 var priority_id = $('input[type=hidden]', $(this.el).parent()).val();
	    		 updatePriority(priority_id, null, '#' + hex);
	    	 }
	     });	
	     $('input[type=hidden]', current).val(this.id);   
	     $('.priorityName', current).empty().append(this.description); 
	     current.removeAttr('id');	     
	});
}
/********************************************************************************
								Settings functions
********************************************************************************/
function initSettings(){
	$('#demo').change(function (){
		var demo = $(this).attr('checked') != null;
		postSettings(demo, null);
	});
	$('input[type=radio]').change(function(){
		if($(this).attr('id')=='listView'){
			postSettings(null, false);
			return;
		}
		if($(this).attr('id')=='flatView'){
			postSettings(null, true);
			return;
		}
	});
}
/**checks if user is in demo mode and calls proxy initialization*/
function loadSettings(){
	$.ajax({
		url : '/ToDoList/tasks/loadsettings.htm',
		type : 'GET',
		dataType : 'json',
		success : function(data, status, xhr) {
			if(!showErrorIfExist('delete priority', data)){
				var res = data.data;
				$('#demo').attr('checked', res.demo);
				if(!res.view)
					$('input#listView').attr('checked', true);
				else
					$('input#flatView').attr('checked', true);
				initProxy(isListView());
				proxy.wireEvents();
			}
		}, 
		error : function(xhr, status, error) {
			showError("error in ajax function. action: load settings");
		} 
	});
}
/**
 * post settings and calls events: initProxy, proxy.wireevents,
 * proxy.refreshTasks, proxy.refreshTasksPriorities
 */
function postSettings(demo, view){
	var postData = dataToString({
		"demo" : demo,
		"view": view
	});
	$.ajax({
		url : '/ToDoList/tasks/updatesettings.htm',
		type : 'POST',
		data : postData,
		dataType : 'json',
		success : function(data, status, xhr) {
			if(!showErrorIfExist('post settings', data)){
				if(view!=null){
					proxy = initProxy(isListView());
					proxy.wireEvents();
					proxy.refreshTasks();
					proxy.refreshTaskPriorities();	
				}
			}
			
		}, 
		error : function(xhr, status, error) {
			showError("error in ajax function. action: post settings");
		}
	});
}
/********************************************************************************
								Task functions
********************************************************************************/
function initProxy(isListView){
	proxy = new Object();
	proxy.list = isListView;
	proxy.listAddTemplateSelector = '#addListTaskTemplate';
	proxy.floatAddTemplateSelector = '#addFloatTask';
	$(proxy.listAddTemplateSelector).addClass('hidden');
	$(proxy.floatAddTemplateSelector).addClass('hidden');
	if(isListView == true){
		proxy.addTemplateSelector = proxy.listAddTemplateSelector;
		proxy.rootContainerSelector = '#taskContainer';
		proxy.allSelector = '#taskPlaceholder';
		proxy.templateTaskSelector = '#templateListTask';
		proxy.taskSelector = '.task';
		proxy.idSelector = '.taskId';
		proxy.descriptionSelector = '.taskDescription';
		proxy.creationSelector = '.creationDate';
		proxy.expirationSelector = '.expirationDate';
		proxy.xSelector = '';
		proxy.ySelector = '';
		proxy.delayedSelector = '.delayed';
		proxy.delayedZeroClass = 'zero';
		proxy.priorityIdSelector = '.priorityValue';
		proxy.prioritySelector = '.prioritySelect';
		proxy.priorityColor = '.taskPriorityColor';
		proxy.deleteSelector = 'input[type=submit].remove';	
		proxy.editInputClass = 'addListDescription';
		
		proxy.addColorSelector = '.taskPriorityColor';
		proxy.addDescriptionSelector = '.addTaskDescription';		
		proxy.addPriorityIdSelector = 'input[type]=hidden.priorityValue';
		proxy.addPrioritySelector = '.prioritySelect';
		proxy.addExpirationSelector = '.expirationDate';
		proxy.submitButtonSelector = 'input[type=submit].add';
	
	} else {
		proxy.addTemplateSelector = proxy.floatAddTemplateSelector;
		proxy.rootContainerSelector = '#taskContainer';
		proxy.allSelector = '#taskPlaceholder';
		proxy.templateTaskSelector = '#templateFloatTask';
		proxy.taskSelector = '.flatTask';
		proxy.idSelector = '.taskId';
		proxy.descriptionSelector = '.flatDescription';
		proxy.creationSelector = '.creationDateFlat';
		proxy.expirationSelector = '.expirationDateFlat';
		proxy.xSelector = '.x';
		proxy.ySelector = '.y';
		proxy.delayedSelector = '.delayedFlat';
		proxy.delayedZeroClass = 'zeroFlat';
		proxy.priorityIdSelector = '.priorityValue';
		proxy.prioritySelector = '.flatPrioritySelect';
		proxy.priorityColor = '.flatTaskPriorityColor';
		proxy.deleteSelector = 'input[type=submit].flatRemove';	
		proxy.editInputClass = 'addFloatDescription';
		
		proxy.addColorSelector = '.flatTaskPriorityColor';
		proxy.addDescriptionSelector = '#addFloatDescription';		
		proxy.addPriorityIdSelector = 'input[type]=hidden.priorityValue';
		proxy.addPrioritySelector = '.flatPrioritySelect';
		proxy.addExpirationSelector = '.expirationDateFlat';
		proxy.submitButtonSelector = 'input[type=submit].flatAdd';
	}
	proxy.getRootContainer = function(){
		return $(proxy.rootContainerSelector);
	};
	proxy.getTasksContainer = function(){
		return $(proxy.allSelector);
	};
	proxy.getTasksContainer().empty();
	proxy.cloneTemplate = function(){
		var container = proxy.getRootContainer();
		var tasks = proxy.getTasksContainer();
		var template = $(proxy.templateTaskSelector, container).clone();
		tasks.append(template);
		return template;
	};
	proxy.getTaskByControl = function(node){
		return $(node).parents(proxy.taskSelector);
	};
	proxy.getAddTemplateByControl = function(node){
		return $(node).parent(proxy.addTemplateSelector);
	};
	proxy.setCoordinates = function(node, x, y){
		node.css('left', x);
		node.css('top', y);
	};
	
	/**refreshes priorities of all shown tasks*/
	proxy.refreshTaskPriorities = function(){
		$(proxy.prioritySelector).empty();
		$.each(priorities, function(key, value) {   
		     $(proxy.prioritySelector).append($("<option></option>").attr("value",this.id).text(this.description)); 		
		});
		$(proxy.prioritySelector).each(function(){
			var task = proxy.getTaskByControl(this);
			var priority_id = $(proxy.priorityIdSelector, task).val();
			if (priority_id && priority_id != '') {
				priority_id = parseInt(priority_id);
				if (getPriorityById(priority_id) == null) {
					priority_id = default_priority_id;
					$(proxy.prioritySelector, task).val(default_priority_id);
				}
				$('option[value=' + priority_id + ']', this).attr('selected', true);
				var color = getPriorityColor(priority_id);
				$(proxy.priorityColor, task).css('background-color', color);
			}
		});
	};
	
	proxy.refreshTasks = function(){
		if(!tasks)return;
		var taskContainer = proxy.getTasksContainer();
		
		taskContainer.empty();			
		if(tasks.length == 0)
			taskContainer.append('no tasks for this section');
		$(tasks).each(function(index, element) {
			proxy.cloneTemplate();
			var current = $(proxy.templateTaskSelector, taskContainer);
			current.removeClass('hidden');
			current.removeAttr('id');
			$(proxy.idSelector, current).val(element.id);
			$(proxy.priorityIdSelector, current).val(element.priorityId);
			var descr = $(proxy.descriptionSelector,current);
			descr.empty().append(element.description);
			var delayed = $(proxy.delayedSelector, current);
			delayed.empty();
			delayed.append(element.delayedTimes);
			if(element.delayedTimes>0)
				delayed.removeClass(proxy.delayedZeroClass);
			var creationDate = $(proxy.creationSelector, current);
			creationDate.empty();
			creationDate.append(element.creationDate);
			var expirationDate = $(proxy.expirationSelector, current);
			expirationDate.val(element.expirationDate);
			expirationDate.datetimepicker({
				firstDay: 1,
				minDate: new Date(),
				dateFormat : 'dd/mm/yy',
				timeFormat: 'hh:mm',
		        onClose: function(date) {
		        	var task = proxy.getTaskByControl(this);
					var taskId = $(proxy.idSelector, task).val();
					updateTask(taskId, null, null, date);	
		        }
			});
			if(!proxy.list){
				proxy.setCoordinates(current, element.x, element.y);
				$(current).draggable({
					stop: function(event, ui) { 						
						var task = $(this);
						var taskId = $(proxy.idSelector, task).val();
						var x = ui.position.left;
						var y = ui.position.top;
						updateTask(taskId, null, null, null, x, y);
					}
				});
				
			}
		});
	};
	/**wires all needed events after initialization*/
	proxy.wireEvents = function(){
		var addTemplate = $(proxy.addTemplateSelector);
		addTemplate.removeClass('hidden');
		var addButton = $(proxy.submitButtonSelector, addTemplate);
		addButton.click(function(){
			var description = $(proxy.addDescriptionSelector, addTemplate).val();
			var priorityId = $(proxy.addPriorityIdSelector, addTemplate).val();
			var expirationDate = $(proxy.addExpirationSelector, addTemplate).val();
			$(proxy.addDescriptionSelector, addTemplate).val('Enter description');
			$(proxy.addExpirationSelector, addTemplate).val('Enter date');
			createTask(description, expirationDate, priorityId, lastFolder);
		});
		$(proxy.addExpirationSelector, addTemplate).datetimepicker({
			firstDay: 1,
			minDate: new Date(),
			dateFormat : 'dd/mm/yy',
			timeFormat: 'hh:mm'
	    });
		$(proxy.addPrioritySelector, addTemplate).change(function(){
			var priority_id = $('option:selected', this).val();
			var priorityIdContainer = $(proxy.addPriorityIdSelector, addTemplate);
			priorityIdContainer.val(priority_id);
			var colorContainer = $(proxy.addColorSelector, addTemplate);
			colorContainer.css('background-color', getPriorityColor(priority_id));	
			}
		);
		$(proxy.getRootContainer()).delegate(proxy.deleteSelector, 'click', function () { 
			var task = proxy.getTaskByControl(this);
			var id = $(proxy.idSelector, task).val();
			deleteTask(id);
		});
		$(proxy.getTasksContainer()).delegate(proxy.prioritySelector, 'change', function () { 
			var task = proxy.getTaskByControl(this);
			var priorityId = $('option:selected', this).val();
			var taskId = $(proxy.idSelector, task).val();
			updateTask(taskId, priorityId);			
		});
		$(proxy.getRootContainer()).delegate(proxy.descriptionSelector, 'dblclick', function () { 
			var content = $(this).html();
			$(this).empty();
			var input = $('<textarea></textarea>');
			input.attr('value', content);
			input.addClass(proxy.editInputClass);
			$(this).append(input);
			input.focus();
			input.blur(function(){
				var container = $(this).parent().parent();
				var content = $(this).attr('value');
				var task = proxy.getTaskByControl(this);
				var taskId = $(proxy.idSelector, task).val();
				container.empty();
				container.html(content);
				updateTask(taskId, null, content);			
			});					
		});
		
	};
	return proxy;
}

function getTaskById(taskId){
	var pos = findTaskPosition(taskId);
	if(pos!=null)
		return tasks[pos];
	return null;
}

function findTaskPosition(taskId){
	for(var i = 0; i<tasks.length;i++)
		if(tasks[i].id == taskId)
			return i;
	return null;
}

function deleteTaskById(taskId){
	var pos = findTaskPosition(taskId);
	if(pos!=null)
		tasks.splice(pos, 1);
}

function createTask(description, expirationDate, priorityId, folderId){
	if(isDemo()){
		showError('action create is not allowed in demo mode');
		return;
	}
	var postData = dataToString({
		"priorityId" : priorityId,
		"folderId" : folderId,
		"description" : description,
		"expirationDate" : expirationDate, 
		"x" : 250,
		"y" : 250
	});
	$.ajax({
		url : '/ToDoList/tasks/createtask.htm',
		type : 'POST',
		data : postData,
		dataType : 'json',
		success : function(data, status, xhr) {
			if(!showErrorIfExist('create task', data)){
				var res = data.data;
				var task = {
					"expirationDate": expirationDate,
					"description" : description,
					"priorityId" : priorityId,
					"creationDate" : res.creationDate,
					"x" : 250,
					"y" : 250,
					"folderId" : folderId,
					"userId" : res.userId,
					"delayedTimes" : res.delayedTimes,
					"id" : res.taskId
				};
				tasks.push(task);
				proxy.refreshTasks();
				proxy.refreshTaskPriorities();
			}
		},
		error : function(xhr, status, error) {
			showError("error in ajax function. action: load tasks");
		} 
	});
	
}
function loadSubTasks(folderId){
	var postData = dataToString({
		folder_id : folderId
	});
	$.ajax({
		url : '/ToDoList/tasks/gettasks.htm',
		type : 'POST',
		data : postData,
		dataType : 'json',
		success : function(data, status, xhr) {
			if(!showErrorIfExist('delete priority', data)){
				tasks = eval(data.data);
				proxy.refreshTasks();
				proxy.refreshTaskPriorities();
			}
		},
		error : function(xhr, status, error) {
			showError("error in ajax function. action: load tasks");
		} 
	});
}
function updateTask(taskId, priorityId, description, expirationDate, x, y){
	if(isDemo()){
		showError('action update is not allowed in demo mode');
		return;
	}
	var data = new Object();
	data.taskId = taskId;
	if(priorityId != null)
		data.priorityId = priorityId;
	if(expirationDate != null)
		data.expirationDate = expirationDate;
	if(description != null)
		data.description = description;
	if(x != null)
		data.x = x;
	if(y != null)
		data.y = y;
	var postData = dataToString(data);
	$.ajax({
		url : '/ToDoList/tasks/updatetask.htm',
		type : 'POST',
		data : postData,
		dataType : 'json',
		success : function(data, status, xhr) {
			if(!showErrorIfExist('update task', data)){
				var res = data.data;
				var task = getTaskById(parseInt(taskId));
				task.expirationDate = res.expirationDate;
				task.description = res.description;
				task.priorityId = res.priorityId;
				task.delayedTimes = res.delayedTimes;
				task.x = parseInt(res.x);
				task.y = parseInt(res.y);
				proxy.refreshTasks();
				proxy.refreshTaskPriorities();
			}
		},
		error : function(xhr, status, error) {
			showError("error in ajax function. action: load tasks");
		} 
	});
}
function deleteTask(taskId){
	if(isDemo()){
		showError('action delete is not allowed in demo mode');
		return;
	}
	var postData = dataToString({
		"taskId" : taskId
	});
	$.ajax({
		url : '/ToDoList/tasks/deletetask.htm',
		type : 'POST',
		data : postData,
		dataType : 'json',
		success : function(data, status, xhr) {
			if(!showErrorIfExist('delete priority', data)){
				deleteTaskById(taskId);
				proxy.refreshTasks();
				proxy.refreshTaskPriorities();
			}
		},
		error : function(xhr, status, error) {
			showError("error in ajax function. action: load tasks");
		} 
	});
}
