<%@ include file="/WEB-INF/jsp/include.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><tiles:getAsString name="title" /></title>
<link rel="stylesheet" href="/ToDoList/res/Styles/Site.css"
	type="text/css" />
<tiles:insertAttribute name="themes" ignore="true" />
</head>
<body>
	<div class="placeholder"></div>
	<div id="central">
		<div id="header">
			<img class="headerImage" alt="ToDo logo"
				src="/ToDoList/res/images/todo.jpg">
			<div id="title" align="center">ToDo List</div>
			<div class="loginContainer">
				<div class="loginButton">
					<c:choose>
						<c:when test="${not empty pageContext.request.userPrincipal}">
     						Welcome <b>${pageContext.request.userPrincipal.name}</b>
							<a href="/ToDoList/logout.htm"> [LogOut]</a>
						</c:when>
						<c:otherwise>
							<a href="/ToDoList/login.htm">[LogIn]</a>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
			<div id="linkContainer" class="headerLinks">
				<a id="tasks" href="/ToDoList/tasks/home.htm">Tasks</a> <a
					id="about" href="/ToDoList/about.htm">About</a> <a id="register"
					href="/ToDoList/register.htm">Registration</a> <a id="login"
					href="/ToDoList/login.htm">Login</a>
			</div>

		</div>
		<div class="body">
			<tiles:insertAttribute name="body" ignore="true" />
		</div>
	</div>
</body>
<script type="text/javascript" src="/ToDoList/res/js/jQuery/jquery-1.7.1.min.js"></script>
<tiles:insertAttribute name="script" ignore="true" />
<script type="text/javascript" src="/ToDoList/res/js/highlight.js"></script>
</html>