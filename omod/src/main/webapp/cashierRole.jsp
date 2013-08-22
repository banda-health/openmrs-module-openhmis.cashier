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
		
		if(radioAdd.checked) {
			add.disabled=false;
			remove.disabled=true;
			newRole.value='';
			newRole.disabled=true;
			
		}
		else if(radioRemove.checked) {
			add.disabled=true;
			remove.disabled=false;
			newRole.value='';
			newRole.disabled=true;
			
		}
		else if(radioNew.checked) {
			add.disabled=true;
			remove.disabled=true;
			newRole.disabled=false;
			
		}
		else {}
	}

</script>

<style>
 th { text-align: left; }
</style>

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

<form method="post" >
	<table>
		<tr>
			<td><input id="addCashierPriv" type="radio" value="add" name="role" onClick="enableDisable();" /></td>
			<th><spring:message code="openhmis.cashier.roleCreation.page.label.add" /></th>
			<td>
				<select id="privAdded" name="privAdded">
					<c:forEach items="${roles}" var="role">
						<option value="${role.uuid}">${role.name}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td><input id="removeCashierPriv" type="radio" value="remove" name="role" onClick="enableDisable();" /></td>
			<th><spring:message code="openhmis.cashier.roleCreation.page.label.remove" /></th>
			<td>
				<select id="privRemoved" name="privRemoved">
					<c:forEach items="${roles}" var="role">
						<option value="${role.uuid}">${role.name}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td><input id="newRole" type="radio" value="new" name="role" checked onClick="enableDisable();" /></td>
			<th><spring:message code="openhmis.cashier.roleCreation.page.label.new" /></th>
			<td>
				<input id="newCashierRole" name="newCashierRole" type="text" /> 
			</td>
		</tr>
	</table>
	
<p><input type="submit" value="<openmrs:message code="Role.save"/>"></p>
</form>

<%@ include file="/WEB-INF/template/footer.jsp"%>