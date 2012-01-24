			<%@ include file="/WEB-INF/jsp/include.jsp"%>
			<div class="login">
				<fieldset>
					<form:form id="registrationForm" method="POST" modelAttribute="user">
						<table>
							<tr>
								<td><span class="loginSpan">Login</span></td>
								<td><form:input id="name" path="name" /></td>
								<td class="status"><form:errors path="name" cssClass="error"  element="label"/></td>
							</tr>
							<tr>
								<td><span class="loginSpan">Email</span></td>
								<td><form:input id="email" path="email" /></td>
								<td class="status"><form:errors path="email" cssClass="error"  element="label"/></td>
							</tr>
							<tr>
								<td><span class="loginSpan">Password</span></td>
								<td><form:password id="password" path="password" /></td>
								<td class="status"><form:errors path="password"
										cssClass="error" element="label" /></td>
							</tr>
							<tr>
								<td><span class="loginSpan">Confirmation</span></td>
								<td><form:password id="confirmation" path="confirmation" /></td>
								<td class="status"><form:errors path="confirmation"
										cssClass="error"  element="label"/></td>
							</tr>
							<tr>
								<td colspan="2"><input type="submit" id="register"
									value="Submit" /></td>
							</tr>
						</table>
					</form:form>
				</fieldset>
			</div>