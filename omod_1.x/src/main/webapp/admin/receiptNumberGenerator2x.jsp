<%@ page import="org.openmrs.module.openhmis.cashier.api.util.PrivilegeConstants" %>
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

<%--@elvariable id="currentGenerator" type="org.openmrs.module.openhmis.cashier.api.IReceiptNumberGenerator"--%>
<%--@elvariable id="generators" type="java.util.List"--%>
<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:require privilege="<%=PrivilegeConstants.TASK_MANAGE_CASHIER_METADATA %>" otherwise="/login.htm" redirect="<%= CashierWebConstants.RECEIPT_NUMBER_GENERATOR_PAGE_2X%>" />

<openmrs:htmlInclude file='<%= request.getContextPath() + CashierWebConstants.MODULE_RESOURCE_ROOT + "css/style.css" %>'/>
<openmrs:htmlInclude file='<%= request.getContextPath() + CashierWebConstants.MODULE_COMMONS_RESOURCE_ROOT + "css/css_2.x/style2x.css" %>'/>
<openmrs:htmlInclude file='<%= request.getContextPath() + CashierWebConstants.MODULE_COMMONS_RESOURCE_ROOT + "styles/bootstrap.css" %>' />
<%@ include file="/WEB-INF/view/module/openhmis/commons/template/common/customizedHeader.jsp"%>
<%@ include file="../template/localHeader.jsp" %>

<%@ include file="../template/customizedLinksHeaderReceiptNumberGenerator.jsp" %>

<div id="body-wrapper">
    <h2>
        <spring:message code="openhmis.cashier.admin.receiptNumberGenerator" />
    </h2>

    <p><spring:message code="openhmis.cashier.receiptGenerator.select" />:</p>

    <form method="POST">
        <table class="table table-striped table-bordered removeBold">
            <tr>
                <td>
                    <input type="radio" name="selectedGenerator" value="" id="noGenerator"
                        <c:if test="${currentGenerator == null}">checked="true"</c:if> />
                    <label class="removeBold" for="noGenerator"><spring:message code="openhmis.cashier.receiptGenerator.noGenerator" /></label>
                </td>
            </tr>
            <tr>
               <td style="font-style: italic;" class="indent"><spring:message code="openhmis.cashier.receiptGenerator.noReceiptNumbers" /></td>
            </tr>
            <c:forEach var="generator" items="${generators}">
                <tr>
                    <td>
	                    <table style="width: 100%">
		                    <tr>
			                    <td>
				                    <input type="radio" name="selectedGenerator" value="${generator.name}" id="${generator}"
			                                <c:if test="${currentGenerator.name == generator.name}">checked="true"</c:if>/>
				                    <label class="removeBold" for="${generator}">${generator.name}</label>
			                    </td>
			                    <td id="generatorLink" style="text-align: right" align="right">
				                    <c:if test="${currentGenerator.name == generator.name && not empty generator.configurationPage}">
					                    <a class="btn btn-grey text-align-right" href="${pageContext.request.contextPath}/${generator.configurationPage}2x.page">
						                    <spring:message code="openhmis.cashier.receiptGenerator.configure"/></a>
				                    </c:if>
			                    </td>
		                    </tr>
	                    </table>
                    </td>
                </tr>
                <tr>
                   <td>
	            <table>
                   <tr>
                   <td style="font-style: italic;" class="indent">${generator.description}</td>
                    <td>
                </td>
                </tr>
		            </table></td>
                </tr>
            </c:forEach>
        </table>
        <input class="submitButton confirm right" value="<openmrs:message code="openhmis.cashier.receiptGenerator.save"/>" type="submit" />
    </form>
</div>
