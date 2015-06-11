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
describe("Bill", function() {
	it("requires Bill", function() {
		require([openhmis.url.cashierBase + "js/model/bill"]);
	});
	
	it("should provide a correct serializable object", function() {
		var billData = jQuery.parseJSON(openhmis.testData.JSON.bill);
		var refbill  = jQuery.parseJSON(openhmis.testData.JSON.bill);
		var bill = new openhmis.Bill(billData, { parse: true });
		openhmis.test.modelToJson(bill, refbill, expect);
	});
});