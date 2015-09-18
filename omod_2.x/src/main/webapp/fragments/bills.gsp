<style type="text/css">
	.billDue {
		color: #7b7b7b;
	}
</style>	

<div class="info-section bills">
    <div class="info-header">
        <i class="icon-money"></i>
        <h3>${ ui.message("openhmis.cashier.patient.bill.history").toUpperCase() }</h3>
        <i class="icon-pencil edit-action right" onclick="location.href='/${ui.contextPath()}/patientDashboard.form?patientId=${patientId}#'"></i>
    </div>
    <div class="info-body">
        <% if (bills.size == 0) { %>
        	${ ui.message("openhmis.cashier.noBillsFound") }
        <% } else { %>
        	<ul>
	        	<% bills.each { bill -> %>
		            <li>
		            	<span class="billId"><b><a href='/${ui.contextPath()}/module/openhmis/cashier/bill.form?billUuid=${bill.uuid}'>${bill.billId}</a></b> <i class="icon-double-angle-right"></i> </span>
		            	<span class="billStatus">${bill.status} <i class="icon-double-angle-right"></i> </span>
		            	<span class="billDue">${bill.getTotalPayments()} / ${bill.getTotal()}</span>
		            </li>
	            <% } %>
            </ul>
        <% } %>
    </div>
</div>
