<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<script type="text/javascript" src="/openmrs/moduleResources/openhmis/cashier/js/underscore.js"></script>
<script type="text/javascript" src="/openmrs/moduleResources/openhmis/cashier/js/backbone.js"></script>
<script type="text/javascript" src="/openmrs/moduleResources/openhmis/cashier/js/backbone-forms.js"></script>
<script type="text/javascript" src="/openmrs/moduleResources/openhmis/cashier/js/sprintf-0.7-beta1.js"></script>
<script type="text/javascript" src="/openmrs/moduleResources/openhmis/cashier/js/util.js"></script>
<script type="text/javascript" src="/openmrs/moduleResources/openhmis/cashier/js/i18n/english.js"></script>
<script type="text/javascript" src="/openmrs/moduleResources/openhmis/cashier/js/model/department.js"></script>
<script type="text/javascript" src="/openmrs/moduleResources/openhmis/cashier/js/model/patient.js"></script>
<script type="text/javascript" src="/openmrs/moduleResources/openhmis/cashier/js/view/generic.js"></script>
<script type="text/javascript">
$(function() {
	var departmentCollection = new openhmis.GenericCollection([], {
		url: "/department",
		model: openhmis.Department
	});
	var addEditView = new openhmis.GenericAddEditView({ collection: departmentCollection });
	addEditView.setElement($('#add-edit-form'));
	addEditView.render();
	var listView = new openhmis.GenericListView({
			model: departmentCollection,
			addEditView: addEditView
	});
	departmentCollection.on('reset', listView.render);
	listView.setElement($('#existing-form'));
	departmentCollection.fetch();
	
});
</script>
<%@ include file="template/localHeader.jsp"%>

<h2>
	<spring:message code="openhmis.cashier.admin.departments" />
</h2>

<%@ include file="template/genericAddEdit.jsp"%>