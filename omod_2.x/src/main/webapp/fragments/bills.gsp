<style type="text/css">
	.billDue {
		color: #00463f;
	}
	
	.billDate {
		color: #7b7b7b;
	}
</style>	

<div class="info-section bills">
    <div class="info-header">
        <i class="icon-money"></i>
        <h3>${ ui.message("openhmis.cashier.patient.bill.history").toUpperCase() }</h3>
        <i class="icon-plus edit-action right" title='${ ui.message("openhmis.cashier.addBill") }' onclick="location.href='/${ui.contextPath()}/openhmis.cashier/cashierBill/entities.page#/?patientUuid=${param.patientId[0]}'"></i>
    </div>
    <div class="info-body">
        <% if (bills.size == 0) { %>
        	${ ui.message("openhmis.cashier.noBillsFound") }
        <% } else { %>
        	<ul>
	        	<% bills.each { bill -> %>
		            <li>
		            	<span class="billId"><b><a href='/${ui.contextPath()}/openhmis.cashier/cashierBill/entities.page#/${bill.uuid}'>${ (bill.receiptNumber == null || bill.receiptNumber.equals("")) ? bill.billId : bill.receiptNumber }</a></b> <i class="icon-double-angle-right"></i> </span>
		            	<span class="billStatus">${bill.status} <i class="icon-double-angle-right"></i> </span>
		            	<span class="billDue">${ (bill.status.name().equals("POSTED")) ? ui.message("openhmis.cashier.bill.due") + ": " + (bill.getTotal() - bill.getTotalPayments()) : ui.message("openhmis.cashier.bill.total") + ": " + bill.getTotal() } <i class="icon-double-angle-right"></i> </span>
		            	<span class="billDate">${bill.getLastUpdated()}</span>
		            </li>
	            <% } %>
            </ul>
        <% } %>
    </div>
</div>
