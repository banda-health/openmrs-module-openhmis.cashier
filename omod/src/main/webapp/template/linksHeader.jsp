<%@ include file="/WEB-INF/template/include.jsp"%>

<ul id="menu">
	<li class="first">
		<a href="${pageContext.request.contextPath}/admin"><openmrs:message code="admin.title.short"/></a>
	</li>
	<openmrs:hasPrivilege privilege="Manage Roles">
		<li <c:if test='<%= request.getRequestURI().contains("cashier/role") %>'>class="active"</c:if>>
			<a href="${pageContext.request.contextPath}/module/openhmis/cashier/role.form">
				<spring:message code="openhmis.cashier.admin.role"/>
			</a>
		</li>
	</openmrs:hasPrivilege>
	<openmrs:hasPrivilege privilege="Manage Cashier Metadata">
		<li <c:if test='<%= request.getRequestURI().contains("cashier/departments") %>'>class="active"</c:if>>
			<a href="${pageContext.request.contextPath}/module/openhmis/cashier/departments.form">
				<spring:message code="openhmis.cashier.admin.departments"/>
			</a>
		</li>
	</openmrs:hasPrivilege>
	<openmrs:hasPrivilege privilege="Manage Cashier Metadata">
		<li <c:if test='<%= request.getRequestURI().contains("cashier/cashPoints") %>'>class="active"</c:if>>
			<a href="${pageContext.request.contextPath}/module/openhmis/cashier/cashPoints.form">
				<spring:message code="openhmis.cashier.admin.cashPoints"/>
			</a>
		</li>
	</openmrs:hasPrivilege>
	<openmrs:hasPrivilege privilege="Manage Cashier Metadata">
		<li <c:if test='<%= request.getRequestURI().contains("cashier/paymentModes") %>'>class="active"</c:if>>
			<a href="${pageContext.request.contextPath}/module/openhmis/cashier/paymentModes.form">
				<spring:message code="openhmis.cashier.admin.paymentModes"/>
			</a>
		</li>
	</openmrs:hasPrivilege>
	<openmrs:hasPrivilege privilege="Manage Cashier Metadata">
		<li <c:if test='<%= request.getRequestURI().contains("cashier/items") %>'>class="active"</c:if>>
			<a href="${pageContext.request.contextPath}/module/openhmis/cashier/items.form">
				<spring:message code="openhmis.cashier.admin.items"/>
			</a>
		</li>
	</openmrs:hasPrivilege>
	<openmrs:hasPrivilege privilege="Manage Cashier Metadata">
		<li <c:if test='<%= request.getRequestURI().contains("cashier/admin/receiptNumberGenerator") %>'>class="active"</c:if>>
			<a href="${pageContext.request.contextPath}/module/openhmis/cashier/admin/receiptNumberGenerator.form">
				<spring:message code="openhmis.cashier.admin.receiptNumberGenerator"/>
			</a>
		</li>
	</openmrs:hasPrivilege>
</ul>