<%@ page import="org.openmrs.module.openhmis.cashier.api.util.PrivilegeConstants" %>
<%@ page import="org.openmrs.module.openhmis.cashier.web.CashierWebConstants" %>
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
  ~
  --%>

<%--@elvariable id="roles" type="java.util.List<org.openmrs.Role>"--%>

<%@ include file="/WEB-INF/template/include.jsp" %>
<openmrs:require allPrivileges="<%=PrivilegeConstants.TASK_MANAGE_CASHIER_METADATA %>" otherwise="/login.htm"
                 redirect="/module/openhmis/cashier/cashierRole.page"/>

<openmrs:htmlInclude file='<%= request.getContextPath() + CashierWebConstants.MODULE_RESOURCE_ROOT + "css/style.css" %>'/>
<openmrs:htmlInclude
		file='<%= request.getContextPath() + CashierWebConstants.MODULE_COMMONS_RESOURCE_ROOT + "css/css_2.x/style2x.css" %>'/>
<openmrs:htmlInclude
		file='<%= request.getContextPath() + CashierWebConstants.MODULE_COMMONS_RESOURCE_ROOT + "styles/bootstrap.css" %>'/>
<%@ include file="/WEB-INF/view/module/openhmis/commons/template/common/customizedHeader.jsp" %>
<%@ include file="template/localHeader.jsp" %>
<%@ include file="template/customizedRoleCreationLinksHeader.jsp" %>

<script type="text/javascript">
	function enableDisable() {
		var radioAdd = $j('#addPriv');
		var radioRemove = $j('#removePriv');
		var radioNew = $j('#newRole');
		var add = $j('#addToRole');
		var remove = $j('#removeFromRole');
		var newRole = $j('#newRoleName');

		if (radioAdd.checked) {
			add.disabled = false;
			remove.disabled = true;
			newRole.value = '';
			newRole.disabled = true;
		} else if (radioRemove.checked) {
			add.disabled = true;
			remove.disabled = false;
			newRole.value = '';
			newRole.disabled = true;
		} else if (radioNew.checked) {
			add.disabled = true;
			remove.disabled = true;
			newRole.disabled = false;
		}
	}
</script>

<spring:hasBindErrors name="cashierRole">
	<openmrs:message code="fix.error" htmlEscape="false"/>
	<div class="error">
		<c:forEach items="${errors.allErrors}" var="error">
			<openmrs:message code="${error.code}" text="${error.defaultMessage}"/><br/>
		</c:forEach>
	</div>
	<br/>
</spring:hasBindErrors>

<div id="body-wrapper">
	<h2>
		<spring:message code="openhmis.cashier.admin.role"/>
	</h2>
	<p>
		<spring:message code="openhmis.cashier.roleCreation.page.instruction"/>
	</p>
	<form method="post">
		<table class="table table-striped table-bordered removeBold">
			<tr>
				<td>
					<input id="addPriv" type="radio" value="add" name="action" onClick="enableDisable();"/>
					<label class="removeBold" for="addPriv"><spring:message code="openhmis.cashier.roleCreation.page.label.add"/></label>
				</td>
				<td>
					<select id="addToRole" name="addToRole" class="dropdown form-control">
						<c:forEach items="${roles}" var="role">
							<option value="${role.uuid}">${role.name}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td>
					<input id="removePriv" type="radio" value="remove" name="action" onClick="enableDisable();"/>
					<label class="removeBold" for="removePriv"><spring:message code="openhmis.cashier.roleCreation.page.label.remove"/></label>
				</td>
				<td>
					<select id="removeFromRole" name="removeFromRole" class="dropdown form-control">
						<c:forEach items="${roles}" var="role">
							<option value="${role.uuid}">${role.name}</option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td>
					<input id="newRole" type="radio" value="new" name="action" checked onClick="enableDisable();"/>
					<label class="removeBold" for="newRole"><spring:message code="openhmis.cashier.roleCreation.page.label.new"/></label>
				</td>
				<td>
					<input id="newRoleName" name="newRoleName" type="text" class="form-control"/>
				</td>
			</tr>
		</table>
		<p><input class="submitButton confirm right" type="submit" value="<openmrs:message code="openhmis.cashier.roleCreation.page.role.save"/>"></p>
	</form>
</div>
