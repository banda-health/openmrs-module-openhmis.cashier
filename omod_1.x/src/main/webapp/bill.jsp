<%@ page import="org.openmrs.module.openhmis.cashier.web.PrivilegeWebConstants" %>
<%@ include file="/WEB-INF/template/include.jsp"%>
<%--
  ~ The contents of this file are subject to the OpenMRS Public License
  ~ Version 2.0 (the "License"); you may not use this file except in
  ~ compliance with the License. You may obtain a copy of the License at
  ~ http://license.openmrs.org
  ~
  ~ Software distributed under the License is distributed on an "AS IS"
  ~ basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
  ~ the License for the specific language governing rights and
  ~ limitations under the License.
  ~
  ~ Copyright (C) OpenHMIS.  All Rights Reserved.
  --%>
<openmrs:require allPrivileges="<%=PrivilegeWebConstants.BILL_PAGE_PRIVILEDGES%>" otherwise="/login.htm" redirect="/module/openhmis/cashier/bill.form" />

<%-- Force our newer jquery version to load first --%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="template/localHeader.jsp"%>
<openmrs:htmlInclude file="/moduleResources/openhmis/cashier/js/screen/bill.js" />
<h2>
	<c:choose>
		<c:when test="${empty bill}">
			<spring:message code="openhmis.cashier.newBill" />
		</c:when>
		<c:when test='${bill.status == "PENDING" }'>
			<spring:message code="openhmis.cashier.editBill" />
			<c:choose>
				<c:when test="${bill.receiptNumber != null}">${bill.receiptNumber}</c:when>
				<c:otherwise>${bill.id}</c:otherwise>
			</c:choose>
		</c:when>
		<c:otherwise>
			<spring:message code="openhmis.cashier.viewBill" />
			<c:choose>
				<c:when test="${bill.receiptNumber != null}">${bill.receiptNumber}</c:when>
				<c:otherwise>${bill.id}</c:otherwise>
			</c:choose>
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
    <c:if test="${!empty bill.adjustmentReason}">
         <span class="heading_annotation"><openmrs:message code="openhmis.cashier.adjustedReason"/>
               &nbsp; ${bill.adjustmentReason}
         </span>
    </c:if>
</h2>
<input type="hidden" id="showAdjustmentReasonField" value="${showAdjustmentReasonField}">
<input type="hidden" id="roundingItemUuid" value="${roundingItemUuid}">
<input type="hidden" id="allowBillAdjustment" value="${allowBillAdjustment}">
<input type="hidden" id="autofillPaymentAmount" value="${autofillPaymentAmount}">

<ul id="bill-info" class="floating">
<c:choose>
	<c:when test="${!empty bill}">
		<li class="cashier"><span class="label"><openmrs:message code="openhmis.cashier.cashier.name"/>:</span> ${bill.cashier.person.personName}</li>
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
			curl([openhmis.url.backboneBase + 'js/openhmis'], function(openhmis) { openhmis.doSelectionHandler(index,data); });
		};</script>
	</div>
</div>

<div id="bill"></div>
<div id="payment"></div>

<input type="submit" id="saveBill" value="<spring:message code="openhmis.cashier.bill.saveBill" />" />
<input type="button" id="postBill" value="<spring:message code="openhmis.cashier.bill.postBill" />" style="display: none;" />
<c:if test="${!empty showPrint && showPrint == true}">
	<input type="button" id="printReceipt" value="<spring:message code="openhmis.cashier.bill.printReceipt" />" style="display: none;" />
</c:if>
<br />

<%@ include file="/WEB-INF/template/footer.jsp"%>
