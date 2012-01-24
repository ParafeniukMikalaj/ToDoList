<div id="errorTasks" class="errorTask"></div>
<div class="tasks">
	<div id="tree" class="treeContainer">
		<div class="tasksHeader">tree</div>
		<div id="treeContainer"></div>
	</div>
	<div id="taskContainer" class="taskContainer">
		<div class="tasksHeader">Tasks</div>
		<div id="templateTask" class="task hidden">
			<div id="taskPriority" class="taskPriority"></div>
			<div class="taskDescription">task</div>
			<input type="hidden" id="priorityValue"> 
			<input type="submit" class="remove" value="" />
			<select
				id="priorities" class="prioritySelect">

			</select>
			<div id="delay" class="delayed zero">1</div>
		</div>
		<div id="taskPlaceholder"></div>
		<div id="addTaskTemplate" class="task add">
			<div id="taskPriority" class="taskPriority"></div>
			<div class="taskAddDescription">
				<input type="text" value="Enter description"></input>
			</div>

			<input type="submit" class="add" value="" /> <select
				class="prioritySelect">
				<option>test</option>
			</select>
		</div>
	</div>
	<div id="settingsContainer" class="settingsContainer">
		<div class="tasksHeader">Settings</div>
		<div>
			<input type="checkbox" id="demo">demo
		</div>
		<div class="left">
			<input type="radio" id="listView" name="view" class="left">list
			<input type="radio" id="flatView" name="view">desktop
		</div>
		<div id="prioritiesContainer">
			<div id="priorityTemplate" class="priorityContainer hidden">
				<input type="hidden" id="priorityId">
				<div class="priorityColor">
				</div>
				<div class="priorityName">
					test
				</div>
				<input type="submit" class="remove" value="" />
			</div>
			<div id="prioritiesPlaceholder"></div>
			<div id="addPriorityTemplate" class="priorityContainer add">
				<input type="hidden" id="priorityId">
				<div class="priorityColor">
				</div>
				<div class="priorityName">
					<input type="text" value="Enter description" class="minified"></input>
				</div>
				<input type="submit" class="add" value="" />
			</div>
		</div>
	</div>
</div>