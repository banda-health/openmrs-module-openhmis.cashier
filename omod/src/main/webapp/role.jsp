<%--@elvariable id="roles" type="java.util.List<org.openmrs.Role>"--%>

<%@ include file="/WEB-INF/template/include.jsp"%>
<openmrs:require allPrivileges="Manage Cashier Metadata" otherwise="/login.htm" redirect="/module/openhmis/cashier/role.form" />
<%@ include file="/WEB-INF/template/header.jsp"%>

<h2>
	<spring:message code="openhmis.cashier.admin.role" />
</h2>

<p>
Instructions
</p>

<form:form >
	<label for="existingRole">Update Existing Role</label>
	<input id="existingRole" type="radio" value="existing" />
	<div>
		<select id="selectedRole">
			<c:forEach items="${roles}" var="role">
				<option value="${role.uuid}">${role.name}</option>
			</c:forEach>
		</select>
	</div>

	<label for="newRole">Create New Role</label>
	<input id="newRole" type="radio" value="new" />
	<div><input type="text" id="newRoleName"></div>

	<input type="submit">
</form:form>

<%@ include file="/WEB-INF/template/footer.jsp"%>