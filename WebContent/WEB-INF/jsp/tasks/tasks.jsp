		<div id="errorTasks" class="errorTask"></div>
		<div class="tasks">
			<div id="tree" class="treeContainer">
				<div class="tasksHeader">tree</div>
				<div id="treeContainer"></div>
			</div>
			<div id="taskContainer" class="taskContainer">
				<div class="tasksHeader">Tasks</div>
				
				<div id="templateListTask" class="task hidden">
					<div class="taskPriorityColor"></div>
					<div class="taskDescription">task</div>
					<input type="hidden" class="taskId"> 
					<input type="hidden" class="priorityValue"> 
					<input type="submit" class="remove right" value="" />				
					<select	class="prioritySelect right">	</select>
					<div class="delayed zero right">1</div>
					<input type="text" class="expirationDate right" readonly="readonly">
					<div class="creationDate right">2012/02/25 10:28</div>
				</div>
				
				<div id="templateFloatTask" class="flatTask hidden">
					<input type="hidden" class="taskId"/>
					<input type="hidden" class="x">
					<input type="hidden" class="y">
					<input type="hidden" class="priorityValue"> 					
					<div class="flatHeader">
						<div class="flatTaskPriorityColor"></div>
						<select class="flatPrioritySelect"></select>
						<input type="submit" class="flatRemove right" value="">
						<div class="delayedFlat zeroFlat">1</div>
					</div>
					<div class="flatDescription">
						test
					</div>
					<div class="creationDateFlat center-margin">2012/02/25 10:28</div>
					<input type="text" class="expirationDateFlat" readonly="readonly">						
				</div>
				
				<div id="taskPlaceholder"></div>
				
				<div id="addListTaskTemplate" class="task add hidden">
					<div class="taskPriorityColor"></div>
					<input type="text" class="addTaskDescription" value="Enter description"/>
					<input type="hidden" class="priorityValue"> 
					<input type="submit" class="add" value="" />				
					<select	class="prioritySelect right">	</select>
					<input type="text" class="expirationDate right" readonly="readonly" value="Enter date">			
				</div>				
				
				<div id="addFloatTask" class="flatTask flatAdd hidden">
					<input type="hidden" class="priorityValue"> 					
					<div class="flatAddHeader">
						<div class="flatTaskPriorityColor"></div>
						<select class="flatPrioritySelect"></select>
						<input type="submit" class="flatAdd right" value="">
					</div>
					<div class="flatAddDescription">
						<textarea id="addFloatDescription"></textarea>
					</div>
					<input type="text" class="expirationDateFlat" readonly="readonly">						
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
						<input type="hidden">
						<div class="priorityColor"></div>
						<div class="priorityName"></div>
						<input type="submit" class="remove right" value="" />
					</div>
					<div id="prioritiesPlaceholder"></div>
					<div id="addPriorityTemplate" class="priorityContainer add">
						<input type="hidden">
						<div class="priorityColor"></div>
						<div class="priorityName">
							<input type="text" value="Enter description" class="minified"></input>
						</div>
						<input type="submit" class="add right" value="" />
					</div>
				</div>
			</div>
		</div>