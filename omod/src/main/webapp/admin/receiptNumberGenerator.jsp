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

<%--@elvariable id="currentGenerator" type="org.openmrs.module.openhmis.cashier.api.IReceiptNumberGenerator"--%>
<%--@elvariable id="generators" type="java.util.List"--%>
<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="../template/linksHeader.jsp"%>

<openmrs:require privilege="<%=PrivilegeConstants.MANAGE_BILLS %>" otherwise="/login.htm" redirect="/module/openhmis/cashier/admin/receiptNumberGenerator.form" />

<h2>
    <spring:message code="openhmis.cashier.admin.receiptNumberGenerator" />
</h2>

<form method="POST">
<b class="boxHeader"><spring:message code="openhmis.cashier.receiptGenerator.select" /></b>
<div class="box">
    <table>
        <tr>
            <td colspan="2">
                <input type="radio" name="selectedGenerator" value="" id="noGenerator"
                    <c:if test="${currentGenerator == null}">checked="true"</c:if> />
                <label for="noGenerator"><spring:message code="openhmis.cashier.receiptGenerator.noGenerator" /></label></td>
        </tr>
        <tr>
            <td width="5%"></td><td style="font-style: italic;"><spring:message code="openhmis.cashier.receiptGenerator.noReceiptNumbers" /></td>
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
                            <a href="${pageContext.request.contextPath}/${generator.configurationPage}.form">
                                <spring:message code="openhmis.cashier.receiptGenerator.configure" />
                            </a>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
    </table>
    <input type="submit" />
</div>
</form>

<%@ include file="/WEB-INF/template/footer.jsp" %>
