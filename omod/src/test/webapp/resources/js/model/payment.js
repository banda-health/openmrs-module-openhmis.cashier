/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
describe("Payment", function() {
	
	it("requires Payment and Bill", function() {
		requires = {};
		requires[openhmis.url.cashierBase + "js/model/payment"] = null;
		requires[openhmis.url.cashierBase + "js/model/bill"] = null;
		require(requires);
	});
	
	it("should provide a correct serializable object", function() {
		var paymentData = jQuery.parseJSON(openhmis.testData.JSON.payment);
		var refPayment  = jQuery.parseJSON(openhmis.testData.JSON.payment);
		var payment = new openhmis.Payment(paymentData);
		openhmis.test.modelToJson(payment, refPayment, expect);
	});
	
	it("should provide a correct REST url", function() {
		var paymentData = jQuery.parseJSON(openhmis.testData.JSON.payment);
		var payment = new openhmis.Payment(paymentData);
		expect(payment.url()).toEqual(openhmis.url.rest + "v2/cashier/payment");
	});

	it("should provide a correct REST url when added to a bill", function() {
		var paymentData = jQuery.parseJSON(openhmis.testData.JSON.payment);
		var payment = new openhmis.Payment(paymentData);
		var billId = "1234";
		var bill = new openhmis.Bill({ uuid: billId });
		bill.addPayment(payment);
		expect(payment.url()).toEqual(bill.url() + "/payment");
	});
});