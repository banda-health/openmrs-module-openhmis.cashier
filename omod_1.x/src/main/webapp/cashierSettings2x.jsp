<%@ page import="org.openmrs.module.openhmis.cashier.api.util.PrivilegeConstants" %>
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
  ~
  --%>
<openmrs:require allPrivileges="<%=PrivilegeConstants.TASK_MANAGE_CASHIER_METADATA%>" otherwise="/login.htm"
                 redirect="<%= CashierWebConstants.CASHIER_SETTINGS_2X_PAGE %>"/>

<openmrs:htmlInclude file='<%= request.getContextPath() + CashierWebConstants.MODULE_RESOURCE_ROOT + "css/style.css" %>'/>
<openmrs:htmlInclude
		file='<%= request.getContextPath() + CashierWebConstants.MODULE_COMMONS_RESOURCE_ROOT + "css/css_2.x/style2x.css" %>'/>
<openmrs:htmlInclude
		file='<%= request.getContextPath() + CashierWebConstants.MODULE_COMMONS_RESOURCE_ROOT + "styles/bootstrap.css" %>'/>
<%@ include file="/WEB-INF/view/module/openhmis/commons/template/common/customizedHeader.jsp" %>
<%@ include file="template/localHeader.jsp" %>
<%@ include file="template/customizedCashierSettingsHeader.jsp" %>

<div id="body-wrapper">
	<h2>
		<spring:message code="openhmis.cashier.admin.cashierSettings"/>
	</h2>
	<p>
		<spring:message code="openhmis.cashier.setting.header"/>
	</p>
	<form:form method="POST" modelAttribute="cashierSettings">
		<table class="table table-striped table-bordered removeBold">
			<tr>
				<td style="width: 70%;">
					<spring:message code="openhmis.cashier.setting.adjustmentReason.field.header"/>
					<br/>
						<span class="description"><spring:message
								code="openhmis.cashier.setting.adjustmentReason.field.description"/></span>
				</td>
				<td>
					<spring:bind path="adjustmentReasonField">
						<input id="adjustmentReasonField" name="adjustmentReasonField" type="checkbox"
						       <c:if test="${cashierSettings.adjustmentReasonField}">checked</c:if> />
						<label class="removeBold" for="adjustmentReasonField"><spring:message
								code="openhmis.cashier.setting.adjustmentReason.field.header"/></label>
					</spring:bind>
				</td>
			</tr>
			<tr>
				<td style="width: 70%;">
					<spring:message code="openhmis.cashier.setting.billAdjustment.field.header"/>
					<br/>
						<span class="description"><spring:message
								code="openhmis.cashier.setting.billAdjustment.field.description"/></span>
				</td>
				<td>
					<spring:bind path="allowBillAdjustment">
						<input id="allowBillAdjustment" name="allowBillAdjustment" type="checkbox"
						       <c:if test="${cashierSettings.allowBillAdjustment}">checked</c:if> />
						<label class="removeBold" for="allowBillAdjustment"><spring:message
								code="openhmis.cashier.setting.billAdjustment.field.header"/></label>
					</spring:bind>
				</td>
			</tr>
			<tr>
				<td style="width: 70%;">
					<spring:message code="openhmis.cashier.setting.autofillPaymentAmount.field.header"/>
					<br/>
						<span class="description"><spring:message
								code="openhmis.cashier.setting.autofillPaymentAmount.field.description"/> </span>
				</td>
				<td>
					<spring:bind path="autoFillPaymentAmount">
						<input id="autoFillPaymentAmount" name="autoFillPaymentAmount" type="checkbox"
						       <c:if test="${cashierSettings.autoFillPaymentAmount}">checked</c:if> />
						<label class="removeBold" for="autoFillPaymentAmount"><spring:message
								code="openhmis.cashier.setting.autofillPaymentAmount.field.header"/></label>
					</spring:bind>
				</td>
			</tr>
			<tr>
				<td style="width: 70%;">
					<spring:message code="openhmis.cashier.setting.timesheet.field.header"/>
					<br/>
						<span class="description"><spring:message
								code="openhmis.cashier.setting.timesheet.field.description"/></span>
				</td>
				<td>
					<spring:bind path="cashierTimesheetRequired">
						<input id="cashierTimesheetRequired" name="cashierTimesheetRequired" type="checkbox"
						       <c:if test="${cashierSettings.cashierTimesheetRequired}">checked</c:if> />
						<label class="removeBold" for="cashierTimesheetRequired"><spring:message
								code="openhmis.cashier.setting.timesheet.field.header"/></label>
					</spring:bind>
				</td>
			</tr>
			<tr>
				<td style="width: 70%;">
					<spring:message code="openhmis.cashier.setting.nearestRounding.field.header"/>
					<br/>
						<span class="description"><spring:message
								code="openhmis.cashier.setting.nearestRounding.field.description"/></span>
				</td>
				<td>
					<spring:bind path="cashierRoundingToNearest">
						<input class="form-control" id="cashierRoundingToNearest" name="cashierRoundingToNearest" type="text"
						       value="${cashierSettings.cashierRoundingToNearest}"
						       size="50"
						       maxlength="4000">
					</spring:bind>
				</td>
			</tr>
			<tr>
				<td style="width: 70%;">
					<spring:message code="openhmis.cashier.setting.roundingMode.field.header"/>
					<br/>
						<span class="description"><spring:message
								code="openhmis.cashier.setting.roundingMode.field.description"/></span>
				</td>
				<td>
					<spring:bind path="cashierRoundingMode">
						<select class="form-control" id="cashierRoundingMode" name="cashierRoundingMode">
							<option value=""></option>
							<option value="CEILING"
							        <c:if test="${cashierSettings.cashierRoundingMode == 'CEILING'}">selected</c:if>>
								CEILING
							</option>
							<option value="MID"
							        <c:if test="${cashierSettings.cashierRoundingMode == 'MID'}">selected</c:if>>
								MID
							</option>
							<option value="FLOOR"
							        <c:if test="${cashierSettings.cashierRoundingMode == 'FLOOR'}">selected
							</c:if>>FLOOR
							</option>
						</select>
					</spring:bind>
				</td>
			</tr>
			<tr>
				<td style="width: 70%;">
					<spring:message code="openhmis.cashier.setting.patientDashboard2BillCount.field.header"/>
					<br/>
					<span class="description"><spring:message code="openhmis.cashier.setting.patientDashboard2BillCount.field.description"/></span>
				</td>
				<td>
					<spring:bind path="patientDashboard2BillCount">
						<input class="form-control" id="patientDashboard2BillCount" name="patientDashboard2BillCount" type="text" value="${cashierSettings.patientDashboard2BillCount}"
						       size="50"
						       maxlength="4000">
					</spring:bind>
				</td>
			</tr>
			<tr>
				<td style="width: 70%;">
					<spring:message code="openhmis.cashier.setting.receiptReportId.field.header"/>
					<br/>
					<span class="description"><spring:message
							code="openhmis.cashier.setting.receiptReportId.field.description"/></span>
				</td>
				<td>
					<spring:bind path="defaultReceiptReportId">
						<select class="form-control" id="defaultReceiptReportId" name="defaultReceiptReportId">
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
					<spring:message code="openhmis.cashier.setting.departmentCollectionsReportId.field.header"/>
					<br/>
					<span class="description">
						<spring:message code="openhmis.cashier.setting.departmentCollectionsReportId.field.description"/>
					</span>
				</td>
				<td>
					<spring:bind path="departmentCollectionsReportId">
						<select class="form-control" id="departmentCollectionsReportId" name="departmentCollectionsReportId">
							<option value=""></option>
							<c:forEach items="${reports}" var="report">
								<option value="${report.reportId}"
								        <c:if test="${cashierSettings.departmentCollectionsReportId == report.reportId}">selected</c:if>>
										${report.name}
								</option>
							</c:forEach>
						</select>
					</spring:bind>
				</td>
			</tr>
			<tr>
				<td style="width: 70%;">
					<spring:message code="openhmis.cashier.setting.departmentRevenueReportId.field.header"/>
					<br/>
					<span class="description"><spring:message
							code="openhmis.cashier.setting.departmentRevenueReportId.field.description"/></span>
				</td>
				<td>
					<spring:bind path="departmentRevenueReportId">
						<select class="form-control" id="departmentRevenueReportId" name="departmentRevenueReportId">
							<option value=""></option>
							<c:forEach items="${reports}" var="report">
								<option value="${report.reportId}"
								        <c:if test="${cashierSettings.departmentRevenueReportId == report.reportId}">selected</c:if>>
										${report.name}
								</option>
							</c:forEach>
						</select>
					</spring:bind>
				</td>
			</tr>
			<tr>
				<td style="width: 70%;">
					<spring:message code="openhmis.cashier.setting.shiftReportId.field.header"/>
					<br/>
					<span class="description"><spring:message
							code="openhmis.cashier.setting.shiftReportId.field.description"/></span>
				</td>
				<td>
					<spring:bind path="defaultShiftReportId">
						<select class="form-control" id="defaultShiftReportId" name="defaultShiftReportId">
							<option value=""></option>
							<c:forEach items="${reports}" var="report">
								<option value="${report.reportId}"
								        <c:if test="${cashierSettings.defaultShiftReportId == report.reportId}">selected</c:if>>
										${report.name}
								</option>
							</c:forEach>
						</select>
					</spring:bind>
				</td>
			</tr>
			<tr>
				<td style="width: 70%;">
					<spring:message code="openhmis.cashier.setting.dailyShiftSummaryReportId.field.header"/>
					<br/>
					<span class="description"><spring:message
							code="openhmis.cashier.setting.dailyShiftSummaryReportId.field.description"/></span>
				</td>
				<td>
					<spring:bind path="dailyShiftSummaryReportId">
						<select class="form-control" id="dailyShiftSummaryReportId" name="dailyShiftSummaryReportId">
							<option value=""></option>
							<c:forEach items="${reports}" var="report">
								<option value="${report.reportId}"
								        <c:if test="${cashierSettings.dailyShiftSummaryReportId == report.reportId}">selected</c:if>>
										${report.name}
								</option>
							</c:forEach>
						</select>
					</spring:bind>
				</td>
			</tr>
			<tr>
				<td style="width: 70%;">
					<spring:message code="openhmis.cashier.setting.shiftSummaryReportId.field.header"/>
					<br/>
					<span class="description"><spring:message
							code="openhmis.cashier.setting.shiftSummaryReportId.field.description"/></span>
				</td>
				<td>
					<spring:bind path="shiftSummaryReportId">
						<select class="form-control" id="shiftSummaryReportId" name="shiftSummaryReportId">
							<option value=""></option>
							<c:forEach items="${reports}" var="report">
								<option value="${report.reportId}"
								        <c:if test="${cashierSettings.shiftSummaryReportId == report.reportId}">selected</c:if>>
										${report.name}
								</option>
							</c:forEach>
						</select>
					</spring:bind>
				</td>
			</tr>
		</table>
		<br/>
		<p><input class="submitButton confirm right" type="submit" value="<openmrs:message code="openhmis.cashier.settings.page.settings.save"/>"></p>
	</form:form>
</div>
