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