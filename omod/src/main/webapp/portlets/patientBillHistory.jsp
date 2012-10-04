<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:hasPrivilege privilege="Manage Cashier Bills">
    &nbsp;<a href="<openmrs:contextPath />/openhmis/cashier/bill.form?patientId=${model.patient.patientId}"><openmrs:message code="openhmis.cashier.addBill" /></a>
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
            <c:forEach var="bill" items="${model.bills}">
                <tr>
                    <td style="display: none">${bill.id}</td>
                    <td>${bill.createdOn}</td>
                    <td>${bill.receiptNumber}</td>
                    <td>${bill.status}</td>
                    <td>TO DO</td>
                    <td>TO DO</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <c:if test="${empty model.bills}">No bills have been created.</c:if>
</div>