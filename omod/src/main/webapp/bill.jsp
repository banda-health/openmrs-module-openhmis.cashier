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
		<c:when test='${bill.status == "PENDING" }'>
			<spring:message code="openhmis.cashier.editBill" />	${bill.receiptNumber}
		</c:when>
		<c:otherwise>
			<spring:message code="openhmis.cashier.viewBill" /> ${bill.receiptNumber}	
		</c:otherwise>
	</c:choose>
	<c:if test="${!empty billAdjusted }">
		<span class="heading_annotation"><openmrs:message code="openhmis.cashier.adjustmentOf" /> <a href="bill.form?billUuid=${billAdjusted.uuid}">${billAdjusted.receiptNumber}</a></span>
	</c:if>
	<c:if test="${!empty adjustedBy}">
			<span class="heading_annotation"><openmrs:message code="openhmis.cashier.adjustedBy" />
			<c:forEach var="bill" items="${adjustedBy}" varStatus="row">
				<c:if test="${row.index > 0}">, </c:if>
				<a href="bill.form?billUuid=${bill.uuid}">${bill.receiptNumber}</a> 
			</c:forEach>
			</span>
	</c:if>
</h2>
<ul id="bill-info" class="floating">
<c:choose>
	<c:when test="${!empty bill}">
		<li class="cashier"><span class="label"><openmrs:message code="openhmis.cashier.cashier.name"/>:</span> ${bill.cashier}</li>
		<li class="date"><span class="label"><openmrs:message code="openhmis.cashier.date"/>: </span> ${bill.dateCreated}</li>		
	</c:when>
	<c:otherwise>
		<li class="cashier"><span class="label"><openmrs:message code="openhmis.cashier.cashier.name"/>:</span> ${user.person.personName}</li>
	</c:otherwise>
</c:choose>	
	<li class="cashPoint${timesheet != null ? " timesheet" : "" }">
		<c:if test="${!empty cashPoint}">
		<span class="label"><openmrs:message code="openhmis.cashier.cashPoint.name"/>:</span>
			${cashPoint}
		</c:if>
	</li>
</ul>
<div class="clear"></div>

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
<input type="button" id="postBill" value="Post Bill" style="display: none;" />
<input type="button" id="printReceipt" value="Print Receipt" style="display: none;" />
<br />

<%@ include file="/WEB-INF/template/footer.jsp"%>

<script id="line-item-template" type="text/template">
		<td class="item-actions end"><? if (item.collection !== undefined) { ?><input type="image" src="/openmrs/images/trash.gif" title="<spring:message code="openhmis.cashier.bill.removeTitle" />" class="remove"/><? } ?></td>
		<td><input type="text" value="<?= (val = item.get('description')) === undefined ? "" : val ?>" class="item-description" /></td>
		<td><input type="text" value="<?= (val = item.get('quantity')) === undefined ? "" : val ?>" class="item-quantity" /></td>
		<td><input type="text" value="<?= (val = item.get('price')) === undefined ? "" : val ?>" class="item-price" /></td>
		<td class="end"><input type="text" value="<?= (val = item.getTotal()) === undefined ? "" : val ?>" class="item-total" /></td>
</script>