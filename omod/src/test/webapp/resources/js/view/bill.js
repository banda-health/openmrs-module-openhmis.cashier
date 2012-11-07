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
		//TODO: Implement this
	});
});