describe("BillView", function() {
	it("requires BillView", function() { require({"view/bill": "view/bill"}); });
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
		var item = new openhmis.Item($.parseJSON(openhmis.testData.JSON.item));
		// Setting attributes will trigger change, which will trigger adding
		// the new item
		billView.newItem.set({
			item: item,
			quantity: 1,
			price: item.get("defaultPrice").price
		});
		expect(billView.bill.get("lineItems").length).toEqual(1);
		var newItem = billView.bill.get("lineItems").models[0];
		newItem.view.removeItem();
		// Removing the item should remove its row; there should be one row
		// left
		$trs = billView.$("table.bill tbody tr");
		expect($trs.length).toEqual(1);
	});
});