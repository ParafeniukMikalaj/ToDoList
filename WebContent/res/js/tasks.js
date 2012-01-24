var priorities;
var lastFolder;

$(document).ready(function() {
	$('#addTaskTemplate').draggable();
	wireEvents();
	loadSettings();
	initPriorities();
	loadPriorities();
});
function refreshPriorities(){
	$('.prioritySelect').empty();
	var container = $('#prioritiesPlaceholder').empty();	
	$.each(priorities, function(key, value) {   
	     $('.prioritySelect')
	         .append($("<option></option>")
	         .attr("value",this.id)
	         .text(this.description)); 		
		var template = $('#priorityTemplate');  
		container.append(template.clone());
		var current = $("#priorityTemplate", container);
		current.removeClass('hidden');
		var color = this.color;
		$('.priorityColor', current).css('background-color', color).ColorPicker({
			"color" : color,
			"livePreview" : false,
			onSubmit: function(hsb, hex, rgb) {
					if(isDemo()){
						showError('action edit is not allowed in demo');
						return false;
					}
				  $(this.el).css('background-color', '#'+hex);
				  var id = $('#priorityId',$(this.el).parent()).val();
				  var data = 'data='+JSON.stringify({
					  priority_id : id,
					  color : '#'+hex
				  });
				  updatePriority(data);
			}
		});	
		$('#priorityId', current).val(this.id);   
		$('.priorityName', current).empty().append(this.description); 
		current.removeAttr('id');	     
	});
	$('.prioritySelect').each(function(){
		var priority = getPriorityId(this);
		if(priority&&priority!=''){
			priority = parseInt(priority);
			$('option[value='+priority+']', this).attr('selected', true);
			var color = getPriorityColor(priority);
			$('.taskPriority', $(this).parent()).css('background-color', color);
		}
	});
}
function getPriorityId(node){
	return $('#priorityValue',$(node).parent()).val();
}
function getPriorityColor(priority_id){
	for(var i = 0; i<priorities.length;i++)
		if(priorities[i].id == priority_id)
			return priorities[i].color;
		return "#DDDDDD";	
}
function loadPriorities(){
	$.ajax({
		url : '/ToDoList/tasks/loadpriorities.htm',
		type : 'GET',
		dataType : 'json',
		success : function(data, status, xhr) {
			if(data==null || data.status!='ok'){
				if(data.message!=null)
					showError(data.message);
			}
			else {
				priorities = data.data;
				refreshPriorities();
			}
		}, 
		error : function(xhr, status, error) {
			showError("error occured while saving settings");
		}
	});
}
function wireEvents(){
	initSettings();
	initTree();
}
function initColorPicker(node){
	node.ColorPicker();
}
function initTree(){
	$('#treeContainer')
	.jstree({
		'plugins' : [ 'themes', 'ui', 'crrm', 'contextmenu', 'json_data', 'crrm' ],
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
					if(data.status=='ok') {					
						var folders = eval(data.data);
						var res = new Array();
						$(folders).each(function(index, element) {
							var folder = 
							{
								'data' : element.description,
								'metadata' : {id : element.id},
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
			showError('action '+data.func+' can not be performed in demo mode');
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
		var parent_id = data.rslt.parent.data('id');
		var name = data.rslt.name;
		createFolder(name, parent_id, data.rslt.obj, data.rlbk);
	}).bind('remove.jstree', function (event, data) { 
		var id = $(data.rslt.obj).attr('id');
		deleteFolder(id, data.rlbk);
	})
	.delegate('a', 'click', function (event, data) { event.preventDefault(); });
}
function createPriority(postData){
	$.ajax({
		url : '/ToDoList/tasks/createpriority.htm',
		type : 'POST',
		data : postData,
		dataType : 'json',
		success : function(data, status, xhr) {
			if(data&&data.status&&data.status=='ok'){
				loadPriorities();
				refreshPriorities();
			}else {
				if(data&&data.message)
					showError("message");
			}
		}, 
		error : function(xhr, status, error) {
			showError("error occured while saving settings");
		}
	});
}
function updatePriority(postData){
	$.ajax({
		url : '/ToDoList/tasks/updatepriority.htm',
		type : 'POST',
		data : postData,
		dataType : 'json',
		success : function(data, status, xhr) {
			priorityData = eval(postData);
			loadPriorities();
			refreshPriorities();
		}, 
		error : function(xhr, status, error) {
			showError("error occured while saving settings");
		}
	});
}
function deletePriority(id, node){
	var data = new Object();
	data.priority_id = id;
	var postData = 'data='+JSON.stringify(data);
	$.ajax({
		url : '/ToDoList/tasks/deletepriority.htm',
		type : 'POST',
		data : postData,
		dataType : 'json',
		success : function(data, status, xhr) {
			if(data&&data.status=='ok'){
				loadPriorities();
				refreshPriorities();
				loadSubTasks(lastFolder);
			} else {
				if(data&&data.message)
					showError(data.message);
			}
		}, 
		error : function(xhr, status, error) {
			showError("error in ajax function");
		}
	});
}
function initPriorities(){
	$('#addPriorityTemplate .priorityColor').ColorPicker({
		"color" : $('#addPriorityTemplate .priorityColor').css('background-color'),
		"livePreview" : false,
		onSubmit: function(hsb, hex, rgb) {
			  $(this.el).css('background-color', '#'+hex);
		}
	});	
	$('#addPriorityTemplate .add').click(function(){
		if(isDemo()){
			showError('action create is not allowed in demo');
			return;
		}
		var container = $(this).parent();
		var data = new Object();
		var id = $('#priorityId', container).val();
		var color = $('.priorityColor', container).css('background-color');
		var description = $('input[type=text]', container).val();
		data.priority_id = id;
		data.color = color;
		data.description = description;
		var postData = 'data='+JSON.stringify(data);
		createPriority(postData);
	});	
	$('#prioritiesPlaceholder').delegate('input[type=submit].remove', 'click', function(){
		var id = $('#priorityId', $(this).parent()).val();
		if(isDemo()){
			showError('action delete is not allowed in demo');
			return;
		}
		deletePriority(id, $(this));
	});
	$('#prioritiesPlaceholder').delegate('.priorityName', 'dblclick', function(){
		if(isDemo()){
			showError('action edit is not allowed in demo');
			return;
		}
		var content = $(this).html();
		$(this).empty();
		var input = $('<input type="text" class="minified"></input>');
		input.attr('value', content);
		$(this).append(input);
		input.focus();
		input.blur(function(){
			if(isDemo()){
				showError('action delete is not allowed in demo');
				return;
			}
			var container = $(this).parent();
			var content = $(this).attr('value');
			var id = $('#priorityId', container.parent()).attr('value');
			container.empty();
			container.html(content);
			var postData = new Object();
			postData.priority_id = id;
			postData.description = content;
			post = 'data='+JSON.stringify(postData);
			updatePriority(post);
			
		});
	});
}
function initSettings(){
	$('#demo').change(function (){
		var demo = $(this).attr('checked') != null;
		postSettings(demo, null);
	});
	$('input[type=radio]').change(function(){
		if($(this).attr('id')=='listView')
			postSettings(null, false);
		if($(this).attr('id')=='flatView')
			postSettings(null, true);
	});
}
function postSettings(demo, view){
	var postData = 'data=' + JSON.stringify({
		"demo" : demo,
		"view": view
	});
	$.ajax({
		url : '/ToDoList/tasks/updatesettings.htm',
		type : 'POST',
		data : postData,
		dataType : 'json',
		success : function(data, status, xhr) {
			if(data==null || data.status!='ok'){
				if(data.message!=null)
					showError(data.message);
			}
		}, 
		error : function(xhr, status, error) {
			showError("error occured while saving settings");
		}
	});
}
function loadSettings(){
	$.ajax({
		url : '/ToDoList/tasks/loadsettings.htm',
		type : 'GET',
		dataType : 'json',
		success : function(data, status, xhr) {
			if(data==null || data.status!='ok'){
				if(data.message!=null)
					showError(data.message);
			}
			else {
				var res = data.data;
				$('#demo').attr('checked', res.demo);
				if(!res.view)
					$('input#listView').attr('checked', true);
				else
					$('input#flatView').attr('checked', true);
			}
		}, 
		error : function(xhr, status, error) {
			$.jstree.rollback(rollback);
		}
	});
}
function deleteFolder(id, rollback){
	var postData = 'data=' + JSON.stringify({
		"folder_id" : id
	});
	$.ajax({
		url : '/ToDoList/tasks/deletefolder.htm',
		type : 'POST',
		data : postData,
		dataType : 'json',
		success : function(data, status, xhr) {
			if(data==null || data.status!='ok')
				$.jstree.rollback(rollback);
		},
		error : function(xhr, status, error) {
			$.jstree.rollback(rollback);
		}
	});
}
function renameFolder(oldName, newName, id, rollback){
	var postData = 'data=' + JSON.stringify({
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
			if(data==null || data.status!='ok')
				$.jstree.rollback(rollback);
		}, 
		error : function(xhr, status, error) {
			$.jstree.rollback(rollback);
		}
	});
}
function createFolder(folderName, parent_id, node, rollback){
	var postData = 'data=' + JSON.stringify({
		name : folderName, 
		"parent_id" : parent_id
	});
	$.ajax({
		url : '/ToDoList/tasks/createfolder.htm',
		type : 'POST',
		data : postData,
		dataType : 'json',
		success : function(data, status, xhr) {
			if(data==null || data.status!='ok')
				$.jstree.rollback(rollback);
			else
				node.attr("id", data.id);
		},
		error : function(xhr, status, error) {
			$.jstree.rollback(rollback);
		}
	});
}
function loadSubTasks(id){
	var postData = 'data=' + JSON.stringify({
		folder_id : id
	});
	$.ajax({
		url : '/ToDoList/tasks/gettasks.htm',
		type : 'POST',
		data : postData,
		dataType : 'json',
		success : function(data, status, xhr) {
			if(data.status=='ok') {				
				showList(eval(data.data));
			}
			else {
				var container = $('#taskContainer');
				container.empty();
			}
		},
		error : function(xhr, status, error) {
			alert('error');
		}
	});
}
function showList(tasks){
	var container = $('#taskPlaceholder');
	container.empty();			
	if(tasks.length == 0)
		container.append('no tasks for this section');
	$(tasks).each(function(index, element) {
		var template = $('#templateTask');
		container.append(template.clone());
		var current = $("#templateTask", container);
		current.removeClass('hidden');
		current.attr("id", element.id);
		$('#priorityValue', current).val(element.priorityId);
		var descr = $('.taskDescription',current);
		descr.empty().append(element.description);
		var delayed = $('#delay', current);
		delayed.empty();
		delayed.append(element.delayedTimes);
		if(element.delayedTimes>0)
			delayed.removeClass('zero');
	});
	refreshPriorities();
}
function showError(error){
	var container = $('#errorTasks');
	container.empty().append(error).show('slow');
	setTimeout(function(){
		container.empty();
	}, 2000);
}
function isDemo(){
	return $('#demo').attr('checked');
}