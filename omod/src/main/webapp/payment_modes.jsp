<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>

<openmrs:htmlInclude file="/moduleResources/openhmis/cashier/js/model/paymentMode.js"/>

<script type="text/javascript">
    $(function() {
        openhmis.startAddEditScreen(openhmis.PaymentMode, "/payment_mode", {
            listFields: ['name', 'description']
        });
    });
</script>

<h2>
    <spring:message code="openhmis.cashier.admin.paymentModes" />
</h2>

<%@ include file="template/genericAddEdit.jsp"%>