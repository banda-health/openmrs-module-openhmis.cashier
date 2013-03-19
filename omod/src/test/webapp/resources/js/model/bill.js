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