<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="template/localHeader.jsp"%>

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
	<table class="bill">
		<thead>
			<tr>
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

<script id="patient-details-template" type="text/template">
	<b class="boxHeader"><spring:message code="openhmis.cashier.patientDetails" /></b>
	<div class="box">
		<table class="form">
			<tbody>
				<tr>
					<th><spring:message code="openhmis.cashier.patientName" />: </th><td><? patient.get('personName') ?></td>
					<th><spring:message code="openhmis.cashier.patientIdentifier" />: </th><td><? patient.get('identifier') ?></td>
					<td><a class="editBillPatient" href="#"><spring:message code="openhmis.cashier.editBillPatient" /></a></td>
				</tr>
			</tbody>
		</table>
	</div>
	<p></p>
</script>

<script id="line-item-template" type="text/template">
		<td><input type="text" value="<? item.get('description') ?>" class="item-description" /></td>
		<td><input type="text" value="<? item.get('quantity') ?>" class="item-quantity" /></td>
		<td><input type="text" value="<? item.get('price') ?>" class="item-price" /></td>
		<td class="end"><input type="text" value="<? item.getTotal() ?>" class="item-total" /></td>
</script>