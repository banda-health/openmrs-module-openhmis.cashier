describe("Payment", function() {
	it("requires Payment", function() {
		require([openhmis.url.cashierBase + "js/model/payment"]);
	});
	
	it("should provide a correct serializable object", function() {
		var paymentData = jQuery.parseJSON(openhmis.testData.JSON.payment);
		var payment = new openhmis.Payment(paymentData);
		var attrs = payment.toJSON();
		
		expect(attrs.amount).toEqual(paymentData.amount);
		expect(attrs.amountTendered).toEqual(paymentData.amountTendered);
		expect(attrs.paymentMode).toEqual(paymentData.paymentMode);
		expect(attrs.attributes[0].paymentModeAttributeType).toEqual(paymentData.attributes[0].paymentModeAttributeType);
		expect(attrs.attributes[0].value).toEqual(paymentData.attributes[0].value);
		expect(attrs.attributes[1].paymentModeAttributeType).toEqual(paymentData.attributes[1].paymentModeAttributeType);
		expect(attrs.attributes[1].value).toEqual(paymentData.attributes[1].value);
	});
});