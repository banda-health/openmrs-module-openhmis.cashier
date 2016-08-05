
<%@ page import="org.openmrs.module.openhmis.cashier.api.util.PrivilegeConstants" %>
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

<%--@elvariable id="shiftReport" type="org.openmrs.module.jasperreport.JasperReport"--%>
<%--@elvariable id="cashier" type="org.openmrs.Provider"--%>
<%--@elvariable id="timesheet" type="org.openmrs.module.openhmis.cashier.api.model.Timesheet"--%>
<%--@elvariable id="returnUrl" type="java.lang.String"--%>

<%@ include file="/WEB-INF/template/include.jsp"%>
<openmrs:require privilege="<%=PrivilegeConstants.MANAGE_TIMESHEETS%>" otherwise="/login.htm" redirect="/module/openhmis/cashier/timesheetEntry.form" />
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="template/localHeader.jsp" %>
<%@ page import="org.openmrs.module.openhmis.cashier.web.CashierWebConstants" %>
<openmrs:htmlInclude file="/openmrs/ws/module/openhmis/backboneforms/init.js" />
<openmrs:htmlInclude file="/moduleResources/openhmis/cashier/js/init.js" />

<script type="text/javascript">
    //var $ = jQuery;
    $j(function() {
        $j("#shiftDate").datepicker().change(findTimesheets);
    });

    function dateTimeFormat(date) {
        var day = date.getDate();

        // The controller expects a date time to have the following format: mm/dd/yyyy hh:mm

        // The javascript getMonth is zero-based
        var month = date.getMonth() + 1;
        var year = date.getFullYear();
        var hour = date.getHours();
        var min = date.getMinutes();

        day = day < 10 ? "0" + day : day.toString();
        month = month < 10 ? "0" + month: month.toString();
        hour = hour < 10 ? "0" + hour : hour.toString();
        min = min < 10 ? "0" + min: min.toString();

        return month + '/' + day + '/' + year + " " + hour + ":" + min;
    }

    function enterTime(elementId) {
        var element = $j("#" + elementId);

        element.val(dateTimeFormat(new Date()));
    }

    function findTimesheets() {
        var element = $j("#shiftDate");
        var resultDiv = $j("#shiftSearchResults");

        if (!element.val()) {
            return;
        }
        // get date from textbox
        var dt = new Date(element.val());
        if (!dt) {
            return;
        }

        // remove existing timesheet items
        resultDiv.empty();

        // get timesheets from server
        $j.getJSON(openhmis.url.rest + "/v2/cashier/timesheet?date=" + encodeURIComponent(element.val()), function(data) {
            if (data == null || data.length == 0) {
                resultDiv.append("No timesheets found on " + dt.toDateString());
            }

            $j.each(data.results, function(i, item) {
                resultDiv.append("<input type='radio' name='timesheetId' value='" + item.id + "'><label for='" + item.id + "'>" + item.display + "</label><br />");
            });
        })
    }

    function printReport() {
        var reportId = $j("#reportId").val();
        var timesheetId = jQuery("input[name=timesheetId]:checked").val();

        if (!timesheetId) {
            alert("<spring:message code="openhmis.cashier.page.reports.error.timesheetRequired" />");
            return false;
        }

        var url = openhmis.url.openmrs + "<%= CashierWebConstants.JASPER_REPORT_PAGE %>.form?"
        url += "reportId=" + reportId  + "&timesheetId=" + timesheetId;
        window.open(url, "pdfDownload");
        return false;
    }
</script>

<spring:hasBindErrors name="timesheet">
    <openmrs:message code="fix.error" htmlEscape="false"/>
    <div class="error">
        <c:forEach items="${errors.allErrors}" var="error">
            <openmrs:message code="${error.code}" text="${error.defaultMessage}"/><br/>
        </c:forEach>
    </div>
    <br />
</spring:hasBindErrors>

<h2><spring:message code="openhmis.cashier.page.timesheet" /></h2>
<form:form method="POST" modelAttribute="timesheet">
    <b class="boxHeader"><spring:message code="openhmis.cashier.page.timesheet.box.title" /></b>
    <div class="box">
        <ul>
            <li class="bbf-field field-name">
                <label for="cashPoint"><spring:message code="openhmis.cashier.page.timesheet.box.cash.point" /></label>
                <div class="bbf-editor">
                    <spring:bind path="cashPoint">
                        <select id="cashPoint" name="${status.expression}">
                            <c:forEach items="${cashPoints}" var="cashPoint">
                                <option value="${cashPoint.id}" <c:if test="${timesheet.cashPoint.id == cashPoint.id}"> selected </c:if>>
                                        ${cashPoint.name}
                                </option>
                            </c:forEach>
                        </select>
                    </spring:bind>
                </div>
            </li>
            <li class="bbf-field field-description">
                <label for="clockIn"><spring:message code="openhmis.cashier.page.timesheet.box.clock.in" /></label>
                <div class="bbf-editor">
                    <spring:bind path="clockIn">
                        <input id="clockIn" name="${status.expression}" type="text" value="${status.value}" readonly="true" />

                        <c:if test="${timesheet.id == null}">
                            <input type="button" value="<spring:message code="openhmis.cashier.page.timesheet.box.button.clock.in" />"
                            	onclick="enterTime('clockIn');" />
                        </c:if>
                    </spring:bind>
                </div>
            </li>
            <c:if test="${timesheet.id != null}">
                <li class="bbf-field field-description">
                    <label for="clockOut"><spring:message code="openhmis.cashier.page.timesheet.box.clock.out" /></label>
                    <div class="bbf-editor">
                        <spring:bind path="clockOut">
                            <input id="clockOut" name="${status.expression}" type="text" value="${status.value}" />
                            <input type="button" value="<spring:message code="openhmis.cashier.page.timesheet.box.button.clock.out" />"
                            	onclick="enterTime('clockOut');" />
                        </spring:bind>
                    </div>
                </li>
            </c:if>
        </ul>

        <input type="hidden" id="timesheetId" name="id" value="${timesheet.id}">
        <input type="hidden" id="timesheetUuid" name="uuid" value="${timesheet.uuid}">
        <input type="hidden" id="cashier" name="cashier" value="${cashier.id}">
        <input type="hidden" id="returnUrl" name="returnUrl" value="${returnUrl}">
        <input type="submit" value="<spring:message code="openhmis.cashier.page.timesheet.box.button.save" />" >
    </div>
</form:form>

<c:if test="${shiftReport != null}">
<form:form onsubmit="return false;">
    <h2><spring:message code="openhmis.cashier.page.reports" /></h2>
    <b class="boxHeader"><spring:message code="openhmis.cashier.page.reports.box.title" /></b>
    <div class="box">
        <ul>
            <li class="bbf-field field-description">
                <h3>${shiftReport.name}</h3>
                <label for="shiftDate"><spring:message code="openhmis.cashier.page.reports.box.select.shift.date" /></label>
                <div class="bbf-editor">
                    <div style="float: left;">
                        <input id="shiftDate" type="text" /><br />
                    </div>
                    <div style="float: left; padding-left: 8px;">
                        <label for="shiftSearchResults"><spring:message code="openhmis.cashier.page.reports.box.timesheets.shift.date" /></label>
                        <div id="shiftSearchResults"></div>
                    </div>
                    <div style="clear: both;"></div>
                </div>
            </li>
        </ul>
        <input type="hidden" id="reportId" name="reportId" value="${shiftReport.reportId}">
        <input type="submit" value="<spring:message code="openhmis.cashier.page.reports.box.generate.report" />" onclick="printReport()">
    </div>
</form:form>
</c:if>

<%@ include file="/WEB-INF/template/footer.jsp" %>
