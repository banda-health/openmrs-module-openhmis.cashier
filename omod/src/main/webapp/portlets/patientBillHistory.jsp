<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:hasPrivilege privilege="Manage Cashier Bills">
    &nbsp;<a href="<openmrs:contextPath />/module/openhmis/cashier/bill.form?patientUuid=${patient.uuid}"><openmrs:message code="openhmis.cashier.addBill" /></a>
    <br />
    <br />
</openmrs:hasPrivilege>

<div class="boxHeader">Bills</div>
<div class="box">
    <table>
        <thead><tr>
            <th style="display: none">Bill Id</th>
            <th>Created On</th>
            <th>Receipt Number</th>
            <th>Status</th>
            <th>Total Paid</th>
            <th>Total Amount</th>
        </tr></thead>
        <tbody>
            <c:forEach var="bill" items="${bills}">
                <tr>
                    <td style="display: none">${bill.id}</td>
                    <td>${bill.dateCreated}</td>
                    <td><a href="<openmrs:contextPath />/module/openhmis/cashier/bill.form?billUuid=${bill.uuid}">${bill.receiptNumber}</a></td>
                    <td>${bill.status}</td>
                    <td>${bill.totalPaid}</td>
                    <td>${bill.total}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <c:if test="${empty bills}">No bills have been created.</c:if>
</div>