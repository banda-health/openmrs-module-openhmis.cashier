<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<openmrs:htmlInclude file="/moduleResources/openhmis/cashier/js/underscore.js"/>
<openmrs:htmlInclude file="/moduleResources/openhmis/cashier/js/backbone.js"/>
<openmrs:htmlInclude file="/moduleResources/openhmis/cashier/js/backbone-forms.js"/>
<openmrs:htmlInclude file="/moduleResources/openhmis/cashier/js/sprintf-0.7-beta1.js"/>
<openmrs:htmlInclude file="/moduleResources/openhmis/cashier/js/util.js"/>
<openmrs:htmlInclude file="/moduleResources/openhmis/cashier/js/i18n/english.js"/>
<openmrs:htmlInclude file="/moduleResources/openhmis/cashier/js/view/generic.js"/>
<openmrs:htmlInclude file="/moduleResources/openhmis/cashier/js/model/paymentMode.js"/>

<script type="text/javascript">
    $(function() {
        openhmis.startAddEditScreen(openhmis.PaymentMode, "/payment_mode", {
            listFields: ['name', 'description']
        });
    });
</script>
<%@ include file="template/localHeader.jsp"%>

<h2>
    <spring:message code="openhmis.cashier.admin.paymentModes" />
</h2>

<%@ include file="template/genericAddEdit.jsp"%>