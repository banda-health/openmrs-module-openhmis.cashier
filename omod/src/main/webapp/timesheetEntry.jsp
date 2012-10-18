<%--@elvariable id="returnUrl" type="java.lang.String"--%>
<%--@elvariable id="cashier" type="org.openmrs.Provider"--%>
<%--@elvariable id="timesheet" type="org.openmrs.module.openhmis.cashier.api.model.Timesheet"--%>
<%--@elvariable id="cashPoints" type="java.util.List<org.openmrs.module.openhmis.cashier.api.model.CashPoint>"--%>

<%@ include file="/WEB-INF/template/include.jsp"%>
<openmrs:require privilege="Manage Cashier Timesheets" otherwise="/login.htm" redirect="/module/openhmis/cashier/timesheetEntry.form" />

<%@ include file="/WEB-INF/template/header.jsp"%>

<script type="text/javascript">
    function enterTime(elementId) {
        var element = $j("#" + elementId);

        var dt = new Date();
        var day = dt.getDate();
        var month = dt.getMonth();
        var year = dt.getFullYear();
        var hour = dt.getHours();
        var min = dt.getMinutes();

        day = day < 10 ? "0" + day : day.toString();
        month = month < 10 ? "0" + month: month.toString();
        hour = hour < 10 ? "0" + hour : hour.toString();
        min = min < 10 ? "0" + min: min.toString();
        var dateString = day + '/' + month + '/' + year + " " + hour + ":" + min;

        element.val(dateString);
    }
</script>

<spring:hasBindErrors name="timesheet">
    <openmrs:message code="fix.error"/>
    <div class="error">
        <c:forEach items="${errors.allErrors}" var="error">
            <openmrs:message code="${error.code}" text="${error.defaultMessage}"/><br/>
        </c:forEach>
    </div>
    <br />
</spring:hasBindErrors>

<h2>
    <spring:message code="openhmis.cashier.timesheet.entry.name" />
</h2>

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
        <input type="submit">
    </div>
</form:form>

<%@ include file="/WEB-INF/template/footer.jsp" %>