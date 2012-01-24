<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
  <head><title>ToDo :: My first application</title></head>
  <body>
    <h1>ToDo - list of tasks</h1>
    <p>This is my test.</p>
    <%-- Redirected because we can't set the welcome page to a virtual URL. --%>
	<c:redirect url="/about.htm"/>
  </body>
</html>