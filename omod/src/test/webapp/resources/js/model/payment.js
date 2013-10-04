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
	
	it("requires Payment", function() {
		require([openhmis.url.cashierBase + "js/model/payment"]);
	});
	
	it("should provide a correct serializable object", function() {
		var paymentData = jQuery.parseJSON(openhmis.testData.JSON.payment);
		var refPayment  = jQuery.parseJSON(openhmis.testData.JSON.payment);
		var payment = new openhmis.Payment(paymentData);
		openhmis.test.modelToJson(payment, refPayment, expect);
	});
});