<%@ page import="org.openmrs.module.openhmis.cashier.web.CashierWebConstants" %>
<%@ page import="org.openmrs.module.openhmis.cashier.web.PrivilegeWebConstants" %>
<%@ include file="/WEB-INF/template/include.jsp" %>
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
<openmrs:require allPrivileges="<%=PrivilegeWebConstants.SETTING_PAGE_PRIVILEGE%>" otherwise="/login.htm"
                 redirect="<%= CashierWebConstants.CASHIER_SETTINGS_PAGE %>"/>
<%@ include file="/WEB-INF/template/header.jsp" %>
<%@ include file="template/localHeader.jsp" %>
<%@ include file="template/linksHeader.jsp" %>
<h2>
	<spring:message code="openhmis.cashier.admin.cashierSettings"/>
</h2>

<form:form method="POST" modelAttribute="cashierSettings">
	<b class="boxHeader">Cashier Settings</b>

	<div class="box">
		<table>
			<tr>
				<td style="width: 70%;">
					Require Adjustment Reason
					<br/>
					<span class="description">True/false whether or not the adjustment reason field should be used.</span>
				</td>
				<td>
					<spring:bind path="adjustmentReasonField">
					<input id="adjustmentReasonField" name="adjustmentReasonField" type="checkbox"
					       value="${cashierSettings.adjustmentReasonField}" <c:if
							       test="${cashierSettings.adjustmentReasonField}">checked</c:if> />
						<label for="adjustmentReasonField">Require Adjustment Reason</label>
					</spring:bind>
				</td>
			</tr>
			<tr>
				<td style="width: 70%;">
					Allow Bill Adjustments
					<br/>
					<span class="description">True/false weather or not the adjustment field feature should be turned on.</span>
				</td>
				<td>
					<spring:bind path="allowBillAdjustment">
						<input id="allowBillAdjustment" name="allowBillAdjustment" type="checkbox"
						       value="${cashierSettings.allowBillAdjustment}" <c:if
								       test="${cashierSettings.allowBillAdjustment}">checked</c:if> />
						<label for="allowBillAdjustment">Allow Bill Adjustments</label>
					</spring:bind>
				</td>
			</tr>
			<tr>
				<td style="width: 70%;">
					Autofill Payment Amount
					<br/>
					<span class="description">True/false weather or not the payment amount should be automatically filled with the remaining balance.</span>
				</td>
				<td>
					<spring:bind path="autoFillPaymentAmount">
						<input id="autoFillPaymentAmount" name="autoFillPaymentAmount" type="checkbox"
						       value="${cashierSettings.autoFillPaymentAmount}" <c:if
								       test="${cashierSettings.autoFillPaymentAmount}">checked</c:if> />
						<label for="autoFillPaymentAmount">Autofill Payment Amount</label>
					</spring:bind>
				</td>
			</tr>
			<tr>
				<td style="width: 70%;">
					Require Timesheet
					<br/>
					<span class="description">Whether or not to require an active timesheet when creating a new bill. True or false.</span>
				</td>
				<td>
					<spring:bind path="cashierTimesheetRequired">
						<input id="cashierTimesheetRequired" name="cashierTimesheetRequired" type="checkbox"
						       value="${cashierSettings.cashierTimesheetRequired}" <c:if
								       test="${cashierSettings.cashierTimesheetRequired}">checked</c:if> />
						<label for="cashierTimesheetRequired">Require Timesheet</label>
					</spring:bind>
				</td>
			</tr>
			<tr>
				<td style="width: 70%;">
					Default Receipt Report Id
					<br/>
					<span class="description">ID of the default Jasper report to use for generating a receipt on the Bill page.</span>
				</td>
				<td>
					<spring:bind path="defaultReceiptReportId">
						<select id="defaultReceiptReportId" name="defaultReceiptReportId">
							<option value=""></option>
							<c:forEach items="${reports}" var="report">
								<option value="${report.reportId}"
								        <c:if test="${cashierSettings.defaultReceiptReportId == report.reportId}">selected</c:if>>
										${report.name}
								</option>
							</c:forEach>
						</select>
					</spring:bind>
				</td>
			</tr>
			<tr>
				<td style="width: 70%;">
					Default Shift Report Id
					<br/>
					<span class="description">ID of the Jasper Cashier Shift report.</span>
				</td>
				<td>
					<spring:bind path="defaultShitReportId">
						<select id="defaultShitReportId" name="defaultShitReportId">
							<option value=""></option>
							<c:forEach items="${reports}" var="report">
								<option value="${report.reportId}"
								        <c:if test="${cashierSettings.defaultShitReportId == report.reportId}">selected</c:if>>
										${report.name}
								</option>
							</c:forEach>
						</select>
					</spring:bind>
				</td>
			</tr>
			<tr>
				<td style="width: 70%;">
					Round To Nearest
					<br/>
					<span class="description">Nearest unit to round to. Can be a decimal number.</span>
				</td>
				<td>
					<spring:bind path="cashierRoundingToNearest">
						<input id="cashierRoundingToNearest" name="cashierRoundingToNearest" type="text" value="${cashierSettings.cashierRoundingToNearest}"
						       size="50"
						       maxlength="4000">
					</spring:bind>
				</td>
			</tr>
			<tr>
				<td style="width: 70%;">
					Rounding Mode
					<br/>
					<span class="description">How to do rounding to bill totals (FLOOR, MID, CEILING)</span>
				</td>
				<td>
					<spring:bind path="cashierRoundingMode">
						<select id="cashierRoundingMode" name="cashierRoundingMode">
							<option value=""></option>
							<option value="CEILING"<c:if test="${cashierSettings.cashierRoundingMode == 'CEILING'}">selected</c:if>>CEILING</option>
							<option value="MID"<c:if test="${cashierSettings.cashierRoundingMode == 'MID'}">selected</c:if>>
								MID</option>
							<option value="FLOOR" <c:if test="${cashierSettings.cashierRoundingMode == 'FLOOR'}">selected
							</c:if>>FLOOR</option>
						</select>
					</spring:bind>
				</td>
			</tr>
		</table>
		<br/>
		<input type="submit" value="Save" >
	</div>
</form:form>
<%@ include file="/WEB-INF/template/footer.jsp" %>
