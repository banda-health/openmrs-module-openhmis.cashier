<%--@elvariable id="shiftReport" type="org.openmrs.module.jasperreport.JasperReport"--%>
<%--@elvariable id="timesheet" type="org.openmrs.module.openhmis.cashier.api.model.Timesheet"--%>

<%@ page import="org.openmrs.module.openhmis.cashier.web.CashierWebConstants" %>
<%@ include file="/WEB-INF/template/include.jsp"%>
<openmrs:require privilege="Manage Cashier Timesheets" otherwise="/login.htm" redirect="/module/openhmis/cashier/timesheetEntry.form" />

<%@ include file="/WEB-INF/template/header.jsp"%>
<openmrs:htmlInclude file="/moduleResources/openhmis/cashier/js/init.js" />
<openmrs:htmlInclude file="/moduleResources/openhmis/cashier/js/openhmis.js" />
<script type="text/javascript">
    var $ = jQuery;
    $(function() {
        $("#shiftDate").datepicker().change(findTimesheets);
    });

    function dateTimeFormat(date) {
        var day = date.getDate();

        // The javascript getMonth is zero-based
        var month = date.getMonth() + 1;
        var year = date.getFullYear();
        var hour = date.getHours();
        var min = date.getMinutes();

        day = day < 10 ? "0" + day : day.toString();
        month = month < 10 ? "0" + month: month.toString();
        hour = hour < 10 ? "0" + hour : hour.toString();
        min = min < 10 ? "0" + min: min.toString();
        return day + '/' + month + '/' + year + " " + hour + ":" + min;
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
        $j.getJSON(openhmis.url.rest + "timesheet?date=" + encodeURIComponent(element.val()), function(data) {
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
        var timesheetId = $("input[name=timesheetId]:checked").val();

        if (!timesheetId) {
            alert("You must select a timesheet to run the report.");
            return false;
        }

        var url = openhmis.url.openmrs + "<%= CashierWebConstants.JASPER_REPORT_PAGE %>.form?"
        url += "reportId=" + reportId  + "&timesheetId=" + timesheetId;
        window.open(url, "pdfDownload");
        return false;
    }
</script>

<h2><spring:message code="openhmis.cashier.page.timesheet" /></h2>
<form:form method="POST" modelAttribute="timesheet">
    <b class="boxHeader">Timesheet Entry</b>
    <div class="box">
        <ul>
            <li class="bbf-field field-name">
                <label for="cashPoint">Cash Point</label>
                <div class="bbf-editor">
                    <spring:bind path="cashPoint">
                        <select id="cashPoint" name="${status.expression}">
                            <c:forEach items="${cashPoints}" var="cashPoint">
                                <option value="${cashPoint.id}" <c:if test="${timesheet.cashPoint.id == status.value}">selected="true"</c:if>>
                                        ${cashPoint.name}
                                </option>
                            </c:forEach>
                        </select>
                    </spring:bind>
                </div>
            </li>
            <li class="bbf-field field-description">
                <label for="clockIn">Clock In Date/Time</label>
                <div class="bbf-editor">
                    <spring:bind path="clockIn">
                        <input id="clockIn" name="${status.expression}" type="text" value="${status.value}" readonly="true" />

                        <c:if test="${timesheet.id == null}">
                            <input type="button" value="Clock In" onclick="enterTime('clockIn');" />
                        </c:if>
                    </spring:bind>
                </div>
            </li>
            <c:if test="${timesheet.id != null}">
                <li class="bbf-field field-description">
                    <label for="clockOut">Clock Out</label>
                    <div class="bbf-editor">
                        <spring:bind path="clockOut">
                            <input id="clockOut" name="${status.expression}" type="text" value="${status.value}" readonly="true" />
                            <input type="button" value="Clock Out" onclick="enterTime('clockOut');" />
                        </spring:bind>
                    </div>
                </li>
            </c:if>
        </ul>

        <input type="hidden" id="timesheetId" name="id" value="${timesheet.id}">
        <input type="hidden" id="timesheetUuid" name="uuid" value="${timesheet.uuid}">
        <input type="hidden" id="cashier" name="cashier" value="${cashier.id}">
        <input type="hidden" id="returnUrl" name="returnUrl" value="${returnUrl}">
        <input type="submit" value="Save">
    </div>
</form:form>

<c:if test="${shiftReport != null}">
<form:form onsubmit="return false;">
    <h2><spring:message code="openhmis.cashier.page.reports" /></h2>
    <b class="boxHeader">Reports</b>
    <div class="box">
        <ul>
            <li class="bbf-field field-description">
                <h3>${shiftReport.name}</h3>
                <label for="shiftDate">Select Shift Date:</label>
                <div class="bbf-editor">
                    <div style="float: left;">
                        <input id="shiftDate" type="text" /><br />
                    </div>
                    <div style="float: left; padding-left: 8px;">
                        <label for="shiftSearchResults">Timesheets on Shift Date:</label>
                        <div id="shiftSearchResults"></div>
                    </div>
                    <div style="clear: both;"></div>
                </div>
            </li>
        </ul>
        <input type="hidden" id="reportId" name="reportId" value="${shiftReport.reportId}">
        <input type="submit" value="Generate Report" onclick="printReport()">
    </div>
</form:form>
</c:if>

<%@ include file="/WEB-INF/template/footer.jsp" %>