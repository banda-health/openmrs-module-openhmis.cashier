<%@ page import="org.openmrs.module.openhmis.cashier.web.CashierWebConstants" %>
<%@ page import="org.openmrs.module.openhmis.cashier.web.PrivilegeWebConstants" %>
<%@ include file="/WEB-INF/template/include.jsp"%>

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
<ul id="menu">
	<li class="first">
		<a href="${pageContext.request.contextPath}/admin"><openmrs:message code="admin.title.short"/></a>
	</li>
	<openmrs:hasPrivilege privilege="Manage Roles">
		<li <c:if test='<%= request.getRequestURI().contains("cashier/cashierRole") %>'>class="active"</c:if>>
			<a href="${pageContext.request.contextPath}/module/openhmis/cashier/cashierRole.form">
				<spring:message code="openhmis.cashier.admin.role"/>
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
		<li <c:if test='<%= request.getRequestURI().contains("cashier/admin/receiptNumberGenerator") %>'>class="active"</c:if>>
			<a href="${pageContext.request.contextPath}/module/openhmis/cashier/admin/receiptNumberGenerator.form">
				<spring:message code="openhmis.cashier.admin.receiptNumberGenerator"/>
			</a>
		</li>
	</openmrs:hasPrivilege>
	<openmrs:hasPrivilege privilege="<%= PrivilegeWebConstants.SETTING_PAGE_PRIVILEGE %>">
		<li <c:if test='<%= request.getRequestURI().contains("cashier/cashierSettings") %>'>class="active"</c:if>>
			<a href="${pageContext.request.contextPath}/module/openhmis/cashier/cashierSettings.form">
				<spring:message code="openhmis.cashier.admin.cashierSettings"/>
			</a>
		</li>
	</openmrs:hasPrivilege>
</ul>
