<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
  <head><title>Hello :: Spring Application</title></head>
  <body>
  	<h1><fmt:message key="heading"/></h1>
    <p><fmt:message key="greeting"/> <c:out value="${model.now}"/></p>
    <c:forEach items="${model.tasks}" var="task">
      <c:out value="${task.description}"/> <i>$<c:out value="${task.creationDate}"/></i><br><br>
    </c:forEach>
    <p>Greetings, it is now <c:out value="${now}"/></p>
    <a href="<c:url value="createtask.htm"/>">Create new task</a>
  </body>
</html>