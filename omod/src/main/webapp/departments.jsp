<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<script type="text/javascript" src="/openmrs/moduleResources/openhmis/cashier/js/underscore.js"></script>
<script type="text/javascript" src="/openmrs/moduleResources/openhmis/cashier/js/backbone.js"></script>
<script type="text/javascript" src="/openmrs/moduleResources/openhmis/cashier/js/backbone-forms.js"></script>
<script type="text/javascript" src="/openmrs/moduleResources/openhmis/cashier/js/sprintf-0.7-beta1.js"></script>
<script type="text/javascript" src="/openmrs/moduleResources/openhmis/cashier/js/util.js"></script>
<script type="text/javascript" src="/openmrs/moduleResources/openhmis/cashier/js/i18n/english.js"></script>
<script type="text/javascript" src="/openmrs/moduleResources/openhmis/cashier/js/view/generic.js"></script>
<script type="text/javascript" src="/openmrs/moduleResources/openhmis/cashier/js/model/department.js"></script>
<script type="text/javascript">
$(function() {
	openhmis.startAddEditScreen(openhmis.Department, "/department", {
		listFields: ['name', 'description']
	});
});
</script>
<%@ include file="template/localHeader.jsp"%>

<h2>
	<spring:message code="openhmis.cashier.admin.departments" />
</h2>

<%@ include file="template/genericAddEdit.jsp"%>