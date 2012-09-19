<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<script id="curl" type="text/javascript" src="/openmrs/moduleResources/openhmis/cashier/js/curl.js"></script>
<script type="text/javascript" src="/openmrs/moduleResources/openhmis/cashier/js/screen/departments.js"></script>
<%@ include file="template/localHeader.jsp"%>

<h2>
	<spring:message code="openhmis.cashier.admin.departments" />
</h2>

<%@ include file="template/genericAddEdit.jsp"%>