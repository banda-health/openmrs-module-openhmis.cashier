<%@ include file="/WEB-INF/template/include.jsp"%>
<openmrs:require allPrivileges="Manage Cashier Bills, View Cashier Bills" otherwise="/login.htm" redirect="/module/openhmis/cashier/bill.form" />
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="template/localHeader.jsp"%>
<openmrs:htmlInclude file="/moduleResources/openhmis/cashier/js/screen/bill.js" />

<h2>
	<c:choose>
		<c:when test="${empty bill}">
			<spring:message code="openhmis.cashier.newBill" />
		</c:when>
		<c:otherwise>
			<spring:message code="openhmis.cashier.editBill" /> ${bill.receiptNumber}	
		</c:otherwise>
	</c:choose>
</h2>

<div id="patient-view">
	<div id="patient-details" style="display: none;">
	</div>
	<div id="find-patient" style="display: none;">
		<openmrs:portlet id="findPatient" url="findPatient" parameters="size=full|hideAddNewPatient=true|showIncludeVoided=false" />
		<!-- Make sure that the global "doSelectionHandler" is hijacked -->
		<script type="text/javascript">window.doSelectionHandler = function(index, data) {
			curl(['openhmis'], function(openhmis) { openhmis.doSelectionHandler(index,data); });
		};</script>
	</div>
</div>

<div id="bill"></div>
<div id="payment" class="box"></div>

<input type="submit" id="saveBill" value="Save Bill" />

<%@ include file="/WEB-INF/template/footer.jsp"%>

<script id="line-item-template" type="text/template">
		<td class="item-actions end"><? if (item.collection !== undefined) { ?><input type="image" src="/openmrs/images/trash.gif" title="<spring:message code="openhmis.cashier.bill.removeTitle" />" class="remove"/><? } ?></td>
		<td><input type="text" value="<?= (val = item.get('description')) === undefined ? "" : val ?>" class="item-description" /></td>
		<td><input type="text" value="<?= (val = item.get('quantity')) === undefined ? "" : val ?>" class="item-quantity" /></td>
		<td><input type="text" value="<?= (val = item.get('price')) === undefined ? "" : val ?>" class="item-price" /></td>
		<td class="end"><input type="text" value="<?= (val = item.getTotal()) === undefined ? "" : val ?>" class="item-total" /></td>
</script>