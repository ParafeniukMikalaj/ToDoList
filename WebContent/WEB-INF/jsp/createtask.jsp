<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>
<title><fmt:message key="heading" /></title>
<style>
.error {
	color: red;
}
</style>
</head>
<body>
	<h1>
		<fmt:message key="createtask.heading" />
	</h1>
	<form:form method="post" commandName="createTask">
		<table width="95%" bgcolor="f8f8ff" border="0" cellspacing="0"
			cellpadding="5">
			<tr>
				<td align="right" width="20%">Task description:</td>
				<td width="30%"><form:input path="task.description" /></td>
				<td width="70%"><form:errors path="task.description"
						cssClass="error" /></td>
			</tr>
			<tr>
				<td align="right" width="20%">Task expiration date:</td>
				<td width="30%"><form:input path="task.creationDate"
						readonly="true" /></td>
				<td width="70%"><form:errors path="task.expirationDate"
						cssClass="error" /></td>
			</tr>
			<tr>
				<td align="right" width="20%">Task expiration date:</td>
				<td width="30%"><fmt:formatDate pattern="dd/MM/yyyy hh:mm:ss"
						value="${task.expirationDate}" var="date" /> <input type="text"
					name="d" value="<c:out value="${task.expirationDate}"/>" size="20" />
				</td>
				<td width="70%"><form:errors path="task.expirationDate"
						cssClass="error" /></td>
			</tr>

		</table>
		<br>
		<input type="submit" align="center" value="Execute">
	</form:form>
	<a href="<c:url value="hello.htm"/>">Home</a>
</body>
</html>