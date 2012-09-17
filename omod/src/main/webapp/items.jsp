<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<script type="text/javascript" src="/openmrs/moduleResources/openhmis/cashier/js/curl.js"></script>
<script type="text/javascript" src="/openmrs/moduleResources/openhmis/cashier/js/screen/items.js"></script>
<%@ include file="template/localHeader.jsp"%>

<h2>
	<spring:message code="openhmis.cashier.admin.items" />
</h2>

<%@ include file="template/genericAddEdit.jsp"%>