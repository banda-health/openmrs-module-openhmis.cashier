<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<script type="text/javascript" src="/openmrs/moduleResources/openhmis/cashier/js/underscore.js"></script>
<script type="text/javascript" src="/openmrs/moduleResources/openhmis/cashier/js/backbone.js"></script>
<script type="text/javascript" src="/openmrs/moduleResources/openhmis/cashier/js/util.js"></script>
<script type="text/javascript" src="/openmrs/moduleResources/openhmis/cashier/js/i18n/english.js"></script>
<script type="text/javascript" src="/openmrs/moduleResources/openhmis/cashier/js/model/lineItem.js"></script>
<script type="text/javascript" src="/openmrs/moduleResources/openhmis/cashier/js/model/patient.js"></script>
<script type="text/javascript" src="/openmrs/moduleResources/openhmis/cashier/js/view/lineItem.js"></script>
<script type="text/javascript" src="/openmrs/moduleResources/openhmis/cashier/js/view/bill.js"></script>
<script type="text/javascript" src="/openmrs/moduleResources/openhmis/cashier/js/view/patient.js"></script>

<%@ include file="template/localHeader.jsp"%>

<h2>
	<spring:message code="openhmis.cashier.newBill" />
</h2>

<script type="text/javascript">
	$(function() {
		var patientView = new openhmis.PatientView();
		patientView.setElement($('#patient-view'));
		patientView.render();
		window.doSelectionHandler = patientView.takeRawPatient;
		
		var billView = new openhmis.BillView({ model: new openhmis.LineItemCollection() });
		billView.setElement($('#bill'));
		billView.render();
	});
</script>

<div id="patient-view">
	<div id="patient-details" style="display: none;">
	</div>
	<div id="find-patient" style="display: none;">
		<openmrs:portlet id="findPatient" url="findPatient" parameters="size=full|hideAddNewPatient=true|showIncludeVoided=false" />
	</div>
</div>
<div class="box">
	<table class="bill display">
		<thead>
			<tr>
				<td class="item-actions end"></td>
				<th class="item-description"><spring:message code="openhmis.cashier.item.description" /></th>
				<th class="item-quantity"><spring:message code="openhmis.cashier.item.quantity" /></th>
				<th class="item-price"><spring:message code="openhmis.cashier.item.price" /></th>
				<th class="item-total end"><spring:message code="openhmis.cashier.item.total" /></th>
			</tr>
		</thead>
		<tbody id="bill">
		</tbody>
	</table>
</div>

<%@ include file="/WEB-INF/template/footer.jsp"%>

<script id="line-item-template" type="text/template">
		<td class="item-actions end"><? if (item.collection !== undefined) { ?><input type="image" src="/openmrs/images/trash.gif" title="<spring:message code="openhmis.cashier.bill.removeTitle" />" class="remove"/><? } ?></td>
		<td><input type="text" value="<?= (val = item.get('description')) === undefined ? "" : val ?>" class="item-description" /></td>
		<td><input type="text" value="<?= (val = item.get('quantity')) === undefined ? "" : val ?>" class="item-quantity" /></td>
		<td><input type="text" value="<?= (val = item.get('price')) === undefined ? "" : val ?>" class="item-price" /></td>
		<td class="end"><input type="text" value="<?= (val = item.getTotal()) === undefined ? "" : val ?>" class="item-total" /></td>
</script>