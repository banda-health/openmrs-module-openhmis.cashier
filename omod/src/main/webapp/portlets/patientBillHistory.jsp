<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:hasPrivilege privilege="Manage Cashier Bills">
    &nbsp;<a href="<openmrs:contextPath />/module/openhmis/cashier/bill.form?patientUuid=${patient.uuid}"><openmrs:message code="openhmis.cashier.addBill" /></a>
    <br />
    <br />
</openmrs:hasPrivilege>
<openmrs:htmlInclude file="/scripts/jquery/dataTables/css/dataTables_jui.css"/>
<openmrs:htmlInclude file="/scripts/jquery/dataTables/js/jquery.dataTables.min.js"/>

<div class="boxHeader">Bills</div>
<div class="box">
    <table id="billTable" class="display" cellspacing="0"></table>
</div>

<script type="text/javascript">
	jQuery(document).ready(function() {
		var $ = jQuery;
		$("#billTable").dataTable({
			aaData: [
				<c:forEach var="bill" items="${bills}" varStatus="varStatus">
				<c:if test="${varStatus.index > 0}">,
				</c:if>[
					"${bill.dateCreated}",
					'<a href="<openmrs:contextPath />/module/openhmis/cashier/bill.form?billUuid=${bill.uuid}">${bill.receiptNumber}</a>',
							"${bill.status}",
							"${bill.amountPaid}",
							"${bill.total}"
				]
				</c:forEach>
			],
			aaSorting: [[0,'desc']],
			aoColumns: [
				{ sTitle: "<openmrs:message code="openhmis.cashier.bill.createdOn" />" },
				{ sTitle: "<openmrs:message code="openhmis.cashier.bill.receiptNumber" />" },
				{ sTitle: "<openmrs:message code="openhmis.cashier.bill.status" />" },
				{ sTitle: "<openmrs:message code="openhmis.cashier.bill.totalPaid" />" },
				{ sTitle: "<openmrs:message code="openhmis.cashier.bill.totalAmount" />" }
			]
		});
	});
</script>