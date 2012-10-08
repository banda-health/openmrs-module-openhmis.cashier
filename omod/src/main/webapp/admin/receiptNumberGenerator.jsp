<%@ page import="org.openmrs.module.openhmis.cashier.api.util.CashierPrivilegeConstants" %>
<%@ page import="org.openmrs.module.openhmis.cashier.web.CashierWebConstants" %>
<%--@elvariable id="currentGenerator" type="org.openmrs.module.openhmis.cashier.api.IReceiptNumberGenerator"--%>
<%--@elvariable id="generators" type="java.util.List"--%>

<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<openmrs:require privilege="Manage Cashier Bills" otherwise="/login.htm" redirect="/module/openhmis/cashier/admin/receiptNumberGenerator.form" />

<h2>
    <spring:message code="openhmis.cashier.admin.receiptNumberGenerator" />
</h2>

<form method="POST">
<b class="boxHeader">Select Receipt Number Generator</b>
<div class="box">
    <table>
        <tr>
            <td colspan="2">
                <input type="radio" name="selectedGenerator" value="" id="noGenerator"
                    <c:if test="${currentGenerator == null}">checked="true"</c:if> />
                <label for="noGenerator">No Generator</label></td>
        </tr>
        <tr>
            <td width="5%"></td><td style="font-style: italic;">No receipt numbers will be generated for bills but the bill will still be created and saved.</td>
        </tr>
        <c:forEach var="generator" items="${generators}">
            <tr>
                <td colspan="2">
                    <input type="radio" name="selectedGenerator" value="${generator.name}" id="${generator}"
                        <c:if test="${currentGenerator.name == generator.name}">checked="true"</c:if>/>
                    <label for="${generator}">${generator.name}</label></td>
            </tr>
            <tr>
                <td></td><td style="font-style: italic;">${generator.description}</td>
            </tr>
            <tr>
                <td></td><td>
                    <c:if test="${currentGenerator.name == generator.name && not empty generator.configurationPage}">
                            <a href="${pageContext.request.contextPath}/${generator.configurationPage}.form">Configure Generator</a>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
    </table>
    <input type="submit" />
</div>
</form>

<%@ include file="/WEB-INF/template/footer.jsp" %>