<%@ taglib prefix="x_rt" uri="http://java.sun.com/jstl/xml_rt" %>
<%@ page import="org.openmrs.module.openhmis.cashier.api.SequentialReceiptNumberGenerator.GroupingType" %>
<%@ page import="org.openmrs.module.openhmis.cashier.api.SequentialReceiptNumberGenerator.SequenceType" %>
<%@ page import="org.openmrs.module.openhmis.cashier.api.util.CashierPrivilegeConstants" %>
<%@ page import="org.openmrs.module.openhmis.cashier.web.CashierWebConstants" %>

<%--
  ~ The contents of this file are subject to the OpenMRS Public License
  ~ Version 2.0 (the "License"); you may not use this file except in
  ~ compliance with the License. You may obtain a copy of the License at
  ~ http://license.openmrs.org
  ~
  ~ Software distributed under the License is distributed on an "AS IS"
  ~ basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
  ~ License for the specific language governing rights and limitations
  ~ under the License.
  ~
  ~ Copyright (C) OpenMRS, LLC.  All Rights Reserved.
  --%>

<%--@elvariable id="generator" type="org.openmrs.module.openhmis.cashier.api.model.SequentialReceiptNumberGeneratorModel"--%>

<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<openmrs:require privilege="Manage Cashier Bills" otherwise="/login.htm" redirect="/module/openhmis/cashier/admin/seqReceiptNumberGenerator.form" />

<c:set var="gtNone" value="<%=GroupingType.NONE %>"/>
<c:set var="gtCashier" value="<%=GroupingType.CASHIER %>"/>
<c:set var="gtCashPoint" value="<%=GroupingType.CASH_POINT %>"/>
<c:set var="gtCasherCashPoint" value="<%=GroupingType.CASHIER_AND_CASH_POINT %>"/>

<c:set var="stCounter" value="<%=SequenceType.COUNTER %>"/>
<c:set var="stDate" value="<%=SequenceType.DATE_COUNTER %>"/>
<c:set var="stDateTime" value="<%=SequenceType.DATE_TIME_COUNTER %>"/>

<h2>
    <spring:message code="openhmis.cashier.admin.seqReceiptNumberGenerator" />
</h2>

<form:form method="post" modelAttribute="generator">
    <b class="boxHeader">Generator Configuration Settings</b>
    <div class="box">
        <input type="hidden" id="id" name="id" value="${generator.id}">
        <ul>
            <li class="bbf-field field-name">
                <label>Grouping Type</label>
                <div class="bbf-editor">
                    <table>
                        <tr><td colspan="2">
                            <input type="radio" name="groupingType" value="<%= GroupingType.NONE %>"
                                   id="groupingType_NONE" <c:if test="${generator.groupingType == gtNone}">checked="true"</c:if>
                            />
                            <label for="groupingType_NONE">No Grouping</label>
                        </td></tr>
                        <tr><td width="5%"></td><td style="font-style: italic;">No sequence grouping will be done
                            resulting in a single sequence of receipt numbers.
                        </td></tr>
                        <tr><td colspan="2">
                            <input type="radio" name="groupingType" value="<%= GroupingType.CASHIER %>"
                                   id="groupingType_CASHIER" <c:if test="${generator.groupingType == gtCashier}">checked="true"</c:if>
                            />
                            <label for="groupingType_CASHIER">Group by Cashier</label>
                        </td></tr>
                        <tr><td width="5%"></td><td style="font-style: italic;">Each cashier has their own sequence
                            of receipt numbers that are incremented independently.
                        </td></tr>
                        <tr><td colspan="2">
                            <input type="radio" name="groupingType" value="<%= GroupingType.CASH_POINT %>"
                                   id="groupingType_CASH_POINT" <c:if test="${generator.groupingType == gtCashPoint}">checked="true"</c:if>
                            />
                            <label for="groupingType_CASH_POINT">Group by Cash Point</label>
                        </td></tr>
                        <tr><td width="5%"></td><td style="font-style: italic;">Each cash point has its own sequence
                            of receipt numbers that are incremented independently.
                        </td></tr>
                        <tr><td colspan="2">
                            <input type="radio" name="groupingType" value="<%= GroupingType.CASHIER_AND_CASH_POINT %>"
                                   id="groupingType_CASHIER_AND_CASH_POINT" <c:if test="${generator.groupingType == gtCasherCashPoint}">checked="true"</c:if>
                            />
                            <label for="groupingType_CASHIER_AND_CASH_POINT">Group by Cashier and Cash Point</label>
                        </td></tr>
                        <tr><td width="5%"></td><td style="font-style: italic;">Each cashier-cash point pair has a
                            distinct sequence of receipt numbers that are incremented independently.
                        </td></tr>
                    </table>
                </div>
            </li>
            <li class="bbf-field field-description">
                <label>Sequence Type</label>
                <div class="bbf-editor">
                    <table>
                        <tr><td colspan="2">
                            <input type="radio" name="sequenceType" value="<%= SequenceType.COUNTER %>"
                                   id="sequenceType_COUNTER" <c:if test="${generator.sequenceType == stCounter}">checked="true"</c:if>
                                    />
                            <label for="sequenceType_COUNTER">Simple Counter</label>
                        </td></tr>
                        <tr><td width="5%"></td><td style="font-style: italic;">A sequence that simply contains a counter.
                        </td></tr>
                        <tr><td colspan="2">
                            <input type="radio" name="sequenceType" value="<%= SequenceType.DATE_COUNTER %>"
                                   id="sequenceType_DATE_COUNTER" <c:if test="${generator.sequenceType == stDate}">checked="true"</c:if>
                                    />
                            <label for="sequenceType_DATE_COUNTER">Date and Counter</label>
                        </td></tr>
                        <tr><td width="5%"></td><td style="font-style: italic;">A sequence that includes the date and a counter.
                        </td></tr>
                        <tr><td colspan="2">
                            <input type="radio" name="sequenceType" value="<%= SequenceType.DATE_TIME_COUNTER %>"
                                   id="sequenceType_DATE_TIME_COUNTER" <c:if test="${generator.sequenceType == stDateTime}">checked="true"</c:if>
                                    />
                            <label for="sequenceType_DATE_TIME_COUNTER">Date and Time and Counter</label>
                        </td></tr>
                        <tr><td width="5%"></td><td style="font-style: italic;">A sequence that include the date, time, and a counter.
                        </td></tr>
                    </table>
                </div>
            </li>

            <li class="bbf-field field-description">
                <label for="separator">Separator</label>
                <div class="bbf-editor">
                    <select id="separator" name="separator">
                        <option value="-" <c:if test="${generator.separator == '-'}">selected="true"</c:if>>-</option>
                        <option value="_" <c:if test="${generator.separator == '_'}">selected="true"</c:if>>_</option>
                        <option value="." <c:if test="${generator.separator == '.'}">selected="true"</c:if>>.</option>
                        <option value=" " <c:if test="${generator.separator == ' '}">selected="true"</c:if>>&lt;space&gt;</option>
                    </select>
                </div>
            </li>
            <li class="bbf-field field-description">
                <label for="cashierPrefix">Cashier Grouping Prefix</label>
                <div class="bbf-editor">
                    <input id="cashierPrefix" name="cashierPrefix" type="text" value="${generator.cashierPrefix}">
                </div>
            </li>
            <li class="bbf-field field-description">
                <label for="cashPointPrefix">Cash Point Grouping Prefix</label>
                <div class="bbf-editor">
                    <input id="cashPointPrefix" name="cashPointPrefix" type="text" value="${generator.cashPointPrefix}">
                </div>
            </li>
            <li class="bbf-field field-description">
                <label for="sequencePadding">Sequence Padding</label>
                <div class="bbf-editor">
                    <input id="sequencePadding" name="sequencePadding" type="number" value="${generator.sequencePadding}">
                </div>
            </li>
            <li class="bbf-field field-description">
                <label for="includeCheckDigit">Include Check Digit</label>
                <div class="bbf-editor">
                    <input id="includeCheckDigit" name="includeCheckDigit" type="checkbox"
                        <c:if test="${generator.includeCheckDigit}"> checked="true"</c:if>>
                </div>
            </li>
        </ul>

        <input type="submit">
    </div>
</form:form>

<%@ include file="/WEB-INF/template/footer.jsp" %>