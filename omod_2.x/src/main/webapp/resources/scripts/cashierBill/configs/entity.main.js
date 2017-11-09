/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
 * the License for the specific language governing rights and
 * limitations under the License.
 *
 * Copyright (C) OpenHMIS.  All Rights Reserved.
 *
 */

/* initialize and bootstrap application */
requirejs(['cashierBill/configs/entity.module'], function() {
	angular.bootstrap(document, ['entitiesApp']);
});

emr.loadMessages([
	"openhmis.cashier.bill",
	"openhmis.cashier.restful_name",
	"openhmis.cashier.billPlural",
	"general.cancel",
	"openhmis.cashier.payment.confirm.paymentProcess",
	"openhmis.commons.general.required.itemAttribute",
	"openhmis.cashier.editBill",
	"openhmis.cashier.viewBill",
	"openhmis.cashier.bill.chooseItemErrorMessage",
	"openhmis.cashier.bill.previousBill",
	"openhmis.cashier.adjustmentOf",
	"openhmis.cashier.adjustedBy",
	"openhmis.cashier.adjustedReason",
	"openhmis.cashier.bill.lineItems.error.invalidItem",
	"openhmis.cashier.payment.error.amountRequired",
	"openhmis.commons.general.requirePatient",
]);
