<div class="info-section bills">
    <div class="info-header">
        <i class="icon-building"></i>
        <h3>${ ui.message("openhmis.cashier.patient.bill.history").toUpperCase() }</h3>
        <i class="icon-pencil edit-action right" title="${ ui.message("coreapps.edit") }" onclick="location.href='#';"></i>
    </div>
    <div class="info-body">
        <% if (bills.size == 0) { %>
        	${ ui.message("openhmis.cashier.noRecords") }
        <% } else { %>
        	
        <% } %>
    </div>
</div>
