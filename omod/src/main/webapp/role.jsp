<%@ page import="org.openmrs.module.openhmis.cashier.api.util.PrivilegeConstants" %>
<%--
  ~ The contents of this file are subject to the OpenMRS Public License
  ~ Version 2.0 (the "License"); you may not use this file except in
  ~ compliance with the License. You may obtain a copy of the License at
  ~ http://license.openmrs.org
  ~
  ~ Software distributed under the License is distributed on an "AS IS"
  ~ basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
  ~ the License for the specific language governing rights and
  ~ limitations under the License.
  ~
  ~ Copyright (C) OpenHMIS.  All Rights Reserved.
  --%>

<%--@elvariable id="roles" type="java.util.List<org.openmrs.Role>"--%>

<%@ include file="/WEB-INF/template/include.jsp"%>
<openmrs:require allPrivileges="<%=PrivilegeConstants.MANAGE_METADATA%>" otherwise="/login.htm" redirect="/module/openhmis/cashier/role.form" />
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="template/linksHeader.jsp"%>

<h2>
	<spring:message code="openhmis.cashier.admin.role" />
</h2>

<p>
Please pick a role that you want the Cashier role to inherit from:
</p>

<!-- error name cashier_role has been made up, not sure if we need to declare it elsewhere-->
<spring:hasBindErrors name="cashier_role">
    <openmrs:message code="fix.error" htmlEscape="false"/>
    <div class="error">
        <c:forEach items="${errors.allErrors}" var="error">
            <openmrs:message code="${error.code}" text="${error.defaultMessage}"/><br/>
        </c:forEach>
    </div>
    <br />
</spring:hasBindErrors>

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
	<div>
		<input id="newRoleName" type="text" />
		<select id="newRole">
			<c:forEach items="${roles}" var="role">
				<option value="${role.uuid}">${role.name}</option>
			</c:forEach>
		</select>
	</div>

	<input type="submit">
</form:form>

<%@ include file="/WEB-INF/template/footer.jsp"%>
