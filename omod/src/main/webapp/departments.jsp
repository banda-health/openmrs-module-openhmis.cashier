<%@ include file="/WEB-INF/template/include.jsp"%>
<openmrs:require allPrivileges="Manage Cashier Metadata, View Cashier Metadata" otherwise="/login.htm" redirect="/module/openhmis/cashier/departments.form" />
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="template/localHeader.jsp"%>
<%@ include file="template/linksHeader.jsp"%>
<openmrs:htmlInclude file="/moduleResources/openhmis/cashier/js/screen/departments.js" />

<h2>
	<spring:message code="openhmis.cashier.admin.departments" />
</h2>
<%@ include file="/WEB-INF/template/footer.jsp" %>