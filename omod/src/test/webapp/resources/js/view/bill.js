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