<%--
  ~ The contents of this file are subject to the OpenMRS Public License
  ~ Version 2.0 (the "License"); you may not use this file except in
  ~ compliance with the License. You may obtain a copy of the License at
  ~ http://license.openmrs.org
  ~
  ~ Software distributed under the License is distributed on an "AS IS"
  ~ basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
  ~ License for the specific language governing rights and limitations
  ~ under the License.
  ~
  ~ Copyright (C) OpenMRS, LLC.  All Rights Reserved.
  --%>

<%--@elvariable id="roles" type="java.util.List<org.openmrs.Role>"--%>

<%@ include file="/WEB-INF/template/include.jsp"%>
<openmrs:require allPrivileges="Manage Cashier Metadata" otherwise="/login.htm" redirect="/module/openhmis/cashier/role.form" />
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="template/linksHeader.jsp"%>

<script type="text/javascript">
	function enableDisable() {
		var radioAdd = $j('#addCashierPriv');
		var radioRemove = $j('#removeCashierPriv');
		var radioNew = $j('#newRole');
		var add = $j('#privAdded');
		var remove = $j('#privRemoved');
		var newRole = $j('#newCashierRole');
		
		if (radioAdd.checked) {
			add.disabled=false;
			remove.disabled=true;
			newRole.value='';
			newRole.disabled=true;
		} else if (radioRemove.checked) {
			add.disabled=true;
			remove.disabled=false;
			newRole.value='';
			newRole.disabled=true;
		} else if (radioNew.checked) {
			add.disabled=true;
			remove.disabled=true;
			newRole.disabled=false;
		}
	}
</script>

<spring:hasBindErrors name="cashierRole">
    <openmrs:message code="fix.error"/>
    <div class="error">
        <c:forEach items="${errors.allErrors}" var="error">
            <openmrs:message code="${error.code}" text="${error.defaultMessage}"/><br/>
        </c:forEach>
    </div>
    <br />
</spring:hasBindErrors>

<h2>
	<spring:message code="openhmis.cashier.admin.role" />
</h2>

<p>
<spring:message code="openhmis.cashier.roleCreation.page.instruction" />
</p>

<form method="post">
	<table>
		<tr>
			<td>
				<input id="addCashierPriv" type="radio" value="add" name="role" onClick="enableDisable();" />
				<label for="addCashierPriv"><spring:message code="openhmis.cashier.roleCreation.page.label.add" /></label>
			</td>
			<td>
				<select id="privAdded" name="privAdded">
					<c:forEach items="${roles}" var="role">
						<option value="${role.uuid}">${role.name}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td>
				<input id="removeCashierPriv" type="radio" value="remove" name="role" onClick="enableDisable();" />
				<label for="removeCashierPriv"><spring:message code="openhmis.cashier.roleCreation.page.label.remove" /></label>
			</td>
			<td>
				<select id="privRemoved" name="privRemoved">
					<c:forEach items="${roles}" var="role">
						<option value="${role.uuid}">${role.name}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td>
				<input id="newRole" type="radio" value="new" name="role" checked onClick="enableDisable();" />
				<label for="newRole"><spring:message code="openhmis.cashier.roleCreation.page.label.new" /></label>
			</td>
			<td>
				<input id="newCashierRole" name="newCashierRole" type="text" /> 
			</td>
		</tr>
	</table>
	
<p><input type="submit" value="<openmrs:message code="Role.save"/>"></p>
</form>

<%@ include file="/WEB-INF/template/footer.jsp"%>