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
describe("BillView", function() {
	it("requires BillView", function() {
		require([openhmis.url.cashierBase + "js/view/bill"]);
	});
	it("should render with a blank line (new bill)", function() {
		var billView = new openhmis.BillView();
		billView.render();
		billView.setupNewItem();
		expect(billView.$el.html()).not.toBeNull();
		$trs = billView.$("table.bill tbody tr");
		expect($trs.length).toEqual(1);
	});
	
	it("should return to the same state after adding and deleting an item", function() {
		var billView = new openhmis.BillView();
		billView.render();
		billView.setupNewItem();
		var item = new openhmis.Item($.parseJSON(openhmis.testData.JSON.item), { parse: true });
		// Setting attributes will trigger change, which will trigger adding
		// the new item
		billView.newItem.set({
			item: item,
			quantity: 1,
			price: item.get("defaultPrice").price
		});
		expect(billView.bill.get("lineItems").length).toEqual(1);
		var newItem = billView.bill.get("lineItems").models[0];
		newItem.view._removeItem();
		// Removing the item should remove its row; there should be one row
		// left
		$trs = billView.$("table.bill tbody tr");
		expect($trs.length).toEqual(1);
	});
	
	it("should post a bill", function() {
		var bill = new openhmis.Bill($.parseJSON(openhmis.testData.JSON.bill), { parse: true });
		// Check that the bill is PENDING to begin with
		expect(bill.get("status")).toEqual(bill.BillStatus.PENDING);		
		var billView = new openhmis.BillView({ bill: bill });
		spyOn(Backbone, "sync").andCallFake(function(method, model, options) {
			options.success(model);
		});
		billView.postBill();
		// Get the bill model as it was passed to Backbone.sync
		bill = Backbone.sync.mostRecentCall.args[1];
		// Expect the status to be POSTED as it is being saved
		expect(bill.get("status")).toEqual(bill.BillStatus.POSTED);
	});
});