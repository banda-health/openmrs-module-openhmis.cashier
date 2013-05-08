<%@ include file="/WEB-INF/template/include.jsp"%>
<openmrs:require allPrivileges="Manage Cashier Items, View Cashier Items" otherwise="/login.htm" redirect="/module/openhmis/cashier/items.form" />
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="template/localHeader.jsp"%>
<openmrs:htmlInclude file="/moduleResources/openhmis/cashier/js/screen/items.js" />

<h2>
	<spring:message code="openhmis.cashier.admin.items" />
</h2>