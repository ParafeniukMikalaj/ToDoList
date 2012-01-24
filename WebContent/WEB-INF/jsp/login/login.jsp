
<%@ include file="/WEB-INF/jsp/include.jsp"%>
<div class="login">
	<c:if test="${not empty error}">
		<div class="errorblock">
			Your login attempt was not successful, try again.<br /> Caused :
			${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
		</div>
	</c:if>
	<fieldset>
		<form name='f' action="<c:url value='j_spring_security_check' />"
			method='POST'>
			<table>
				<tr>
					<td><span class="loginSpan">Login</span></td>
					<td><input name="j_username" value="" /></td>
				</tr>
				<tr>
					<td><span class="loginSpan">Password</span></td>
					<td><input type="password" name="j_password" /></td>
				</tr>
				<tr>
					<td colspan="2"><input type="submit" id="login" value="Submit" /></td>
				</tr>
			</table>
		</form>
	</fieldset>
</div>