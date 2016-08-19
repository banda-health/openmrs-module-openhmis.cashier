<%@ taglib prefix="x_rt" uri="http://java.sun.com/jstl/xml_rt" %>
<%@ page import="org.openmrs.module.openhmis.cashier.api.SequentialReceiptNumberGenerator.GroupingType" %>
<%@ page import="org.openmrs.module.openhmis.cashier.api.SequentialReceiptNumberGenerator.SequenceType" %>
<%@ page import="org.openmrs.module.openhmis.cashier.web.CashierWebConstants" %>

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

<%--@elvariable id="generator" type="org.openmrs.module.openhmis.cashier.api.model.SequentialReceiptNumberGeneratorModel"--%>

<%@ include file="/WEB-INF/template/include.jsp" %>

<openmrs:htmlInclude file='<%= request.getContextPath() + CashierWebConstants.MODULE_RESOURCE_ROOT + "css/style.css" %>'/>
<openmrs:htmlInclude
		file='<%= request.getContextPath() + CashierWebConstants.MODULE_COMMONS_RESOURCE_ROOT + "css/css_2.x/style2x.css" %>'/>
<openmrs:htmlInclude
		file='<%= request.getContextPath() + CashierWebConstants.MODULE_COMMONS_RESOURCE_ROOT + "styles/bootstrap.css" %>'/>
<%@ include file="/WEB-INF/view/module/openhmis/commons/template/common/customizedHeader.jsp" %>
<%@ include file="../template/localHeader.jsp" %>

<%@ include file="../template/customizedLinksHeaderSeqReceiptNumberGenerator.jsp" %>

<openmrs:require privilege="Manage Cashier Bills" otherwise="/login.htm"
                 redirect="/module/openhmis/cashier/admin/seqReceiptNumberGenerator2x.page"/>

<c:set var="gtNone" value="<%=GroupingType.NONE %>"/>
<c:set var="gtCashier" value="<%=GroupingType.CASHIER %>"/>
<c:set var="gtCashPoint" value="<%=GroupingType.CASH_POINT %>"/>
<c:set var="gtCasherCashPoint" value="<%=GroupingType.CASHIER_AND_CASH_POINT %>"/>

<c:set var="stCounter" value="<%=SequenceType.COUNTER %>"/>
<c:set var="stDate" value="<%=SequenceType.DATE_COUNTER %>"/>
<c:set var="stDateTime" value="<%=SequenceType.DATE_TIME_COUNTER %>"/>

<div id="body-wrapper">
	<h2>
		<spring:message code="openhmis.cashier.admin.seqReceiptNumberGenerator"/>
	</h2>

	<p>Generator Configuration Settings</p>

	<form:form method="post" modelAttribute="generator">
		<div>
			<input type="hidden" id="id" name="id" value="${generator.id}">
			<br/>
			<div>
				<h3>Grouping Type</h3>
				<hr/>
				<table class="table table-striped table-bordered removeBold">
					<tr>
						<td>
							<input type="radio" name="groupingType" value="<%= GroupingType.NONE %>"
							       id="groupingType_NONE"
							       <c:if test="${generator.groupingType == gtNone}">checked="true"</c:if>/>
							<label class="removeBold" for="groupingType_NONE">
								<spring:message code="openhmis.cashier.receiptGenerator.noGrouping"/>
							</label>
						</td>
					</tr>
					<tr>
						<td style="font-style: italic;" class="indent">
							<spring:message code="openhmis.cashier.receiptGenerator.noSequence"/>
						</td>
					</tr>
					<tr>
						<td>
							<input type="radio" name="groupingType" value="<%= GroupingType.CASHIER %>"
							       id="groupingType_CASHIER"
							       <c:if test="${generator.groupingType == gtCashier}">checked="true"</c:if>/>
							<label class="removeBold" for="groupingType_CASHIER">
								<spring:message code="openhmis.cashier.receiptGenerator.groupByCashier"/>
							</label>
						</td>
					</tr>
					<tr>
						<td style="font-style: italic;" class="indent">
							<spring:message code="openhmis.cashier.receiptGenerator.eachCashierSequence"/>
						</td>
					</tr>
					<tr>
						<td>
							<input type="radio" name="groupingType" value="<%= GroupingType.CASH_POINT %>"
							       id="groupingType_CASH_POINT"
							       <c:if test="${generator.groupingType == gtCashPoint}">checked="true"</c:if>/>
							<label class="removeBold" for="groupingType_CASH_POINT">
								<spring:message code="openhmis.cashier.receiptGenerator.groupByCashPoint"/>
							</label>
						</td>
					</tr>
					<tr>
						<td class="indent" style="font-style: italic;">
							<spring:message code="openhmis.cashier.receiptGenerator.eachCashPointSequence"/>
						</td>
					</tr>
					<tr>
						<td>
							<input type="radio" name="groupingType"
							       value="<%= GroupingType.CASHIER_AND_CASH_POINT %>"
							       id="groupingType_CASHIER_AND_CASH_POINT"
							       <c:if test="${generator.groupingType == gtCasherCashPoint}">checked="true"</c:if>/>
							<label class="removeBold" for="groupingType_CASHIER_AND_CASH_POINT">
								<spring:message
										code="openhmis.cashier.receiptGenerator.groupByCashierCashPoint"/>
							</label>
						</td>
					</tr>
					<tr>
						<td class="indent" style="font-style: italic;">
							<spring:message
									code="openhmis.cashier.receiptGenerator.eachCashierCashPointSequence"/>
						</td>
					</tr>
				</table>
			</div>
			<br/>
			<div>
				<h3><spring:message code="openhmis.cashier.receiptGenerator.sequenceType"/></h3>
				<hr/>
					<table class="table table-striped table-bordered removeBold">
						<tr>
							<td>
								<input type="radio" name="sequenceType" value="<%= SequenceType.COUNTER %>"
								       id="sequenceType_COUNTER"
								       <c:if test="${generator.sequenceType == stCounter}">checked="true"</c:if>/>
								<label class="removeBold" for="sequenceType_COUNTER">
									<spring:message code="openhmis.cashier.receiptGenerator.simpleCounter"/>
								</label>
							</td>
						</tr>
						<tr>
							<td class="indent" style="font-style: italic;">
								<spring:message code="openhmis.cashier.receiptGenerator.sequenceCounter"/>
							</td>
						</tr>
						<tr>
							<td>
								<input type="radio" name="sequenceType" value="<%= SequenceType.DATE_COUNTER %>"
								       id="sequenceType_DATE_COUNTER"
								       <c:if test="${generator.sequenceType == stDate}">checked="true"</c:if>/>
								<label class="removeBold" for="sequenceType_DATE_COUNTER">
									<spring:message code="openhmis.cashier.receiptGenerator.dateAndCounter"/>
								</label>
							</td>
						</tr>
						<tr>
							<td class="indent" style="font-style: italic;">
								<spring:message code="openhmis.cashier.receiptGenerator.sequenceDateAndCounter"/>
							</td>
						</tr>
						<tr>
							<td>
								<input type="radio" name="sequenceType" value="<%= SequenceType.DATE_TIME_COUNTER %>"
								       id="sequenceType_DATE_TIME_COUNTER"
								       <c:if test="${generator.sequenceType == stDateTime}">checked="true"</c:if>/>
								<label class="removeBold" for="sequenceType_DATE_TIME_COUNTER">
									<spring:message code="openhmis.cashier.receiptGenerator.dateTimeCounter"/>
								</label>
							</td>
						</tr>
						<tr>
							<td class="indent" style="font-style: italic;">
								<spring:message code="openhmis.cashier.receiptGenerator.sequenceDateTimeAndCounter"/>
							</td>
						</tr>
					</table>
			</div>
			<br/>
			<div>
				<h3>General Settings</h3>
				<hr/>
				<div class="row">
					<div class="col-md-6 .margin-left">
						<label for="separator">
							<spring:message code="openhmis.cashier.receiptGenerator.selector"/>
						</label>
						<div class="bbf-editor">
						<select id="separator" name="separator" class="dropdown form-control">
							<option value="-" <c:if test="${generator.separator == '-'}">selected="true"</c:if>>-</option>
							<option value="_" <c:if test="${generator.separator == '_'}">selected="true"</c:if>>_</option>
							<option value="." <c:if test="${generator.separator == '.'}">selected="true"</c:if>>.</option>
							<option value=" " <c:if test="${generator.separator == ' '}">selected="true"</c:if>>
								&lt;space&gt;</option>
						</select>
							</div>
					</div>
					<div class="col-md-6">
						<label for="cashierPrefix">
							<spring:message code="openhmis.cashier.receiptGenerator.cashierGroupingPrefix"/>
						</label>
						<div class="bbf-editor">
							<input id="cashierPrefix" name="cashierPrefix" type="text" value="${generator.cashierPrefix}"
							       class="form-control">
						</div>
					</div>
				</div>
				<br/>
				<div class="row">
					<div class="col-md-6 .margin-left">
						<label for="cashPointPrefix">
							<spring:message code="openhmis.cashier.receiptGenerator.cashpointGroupingPrefix"/>
						</label>
						<div class="bbf-editor">
							<input id="cashPointPrefix" name="cashPointPrefix" type="text" value="${generator.cashPointPrefix}"
							       class="form-control">
						</div>
					</div>
					<div class="col-md-6">
						<label for="sequencePadding">
							<spring:message code="openhmis.cashier.receiptGenerator.sequencePadding"/>
						</label>
						<div class="bbf-editor">
							<input id="sequencePadding" name="sequencePadding" type="number"
							       value="${generator.sequencePadding}" class="form-control">
						</div>
					</div>
				</div>
				<br/>
				<div class="row">
					<div class="col-md-6 .margin-left">
						<label for="includeCheckDigit">
							<spring:message code="openhmis.cashier.receiptGenerator.includeCheckDigit"/>
						</label>
						<div class="bbf-editor">
							<input id="includeCheckDigit" name="includeCheckDigit" type="checkbox"
							<c:if test="${generator.includeCheckDigit}"> checked="true"</c:if>>
						</div>
					</div>
				</div>
				<br/>
			</fieldset>
			<br/>
			<hr/>
			<input class="submitButton confirm right"
			       value="<openmrs:message code="openhmis.cashier.receiptGenerator.save"/>" type="submit"/>
		</div>
	</form:form>
</div>
