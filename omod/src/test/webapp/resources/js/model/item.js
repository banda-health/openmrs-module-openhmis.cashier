describe('Item', function() {
	it("should set a list of price options in schema.defaultPrice", function() {
		var price1 = {
			uuid: "508b446c-6fd2-4915-b44d-e64aa82be2f1",
			price: 10
		}
		var price2 = {
			uuid: "9a834c9b-b13d-43a5-8fff-0b9d9f6e3368",
			price: 15.5
		}
		var item = new openhmis.Item({ prices: [ price1, price2 ] }, { parse: true});
		var option1 = item.schema.defaultPrice.options[0];
		var option2 = item.schema.defaultPrice.options[1];
		expect(option1.val).toEqual(price1.uuid);
		expect(option1.label).toEqual("10.00");
		expect(option2.val).toEqual(price2.uuid);
		expect(option2.label).toEqual("15.50");
	});
});

describe('ItemCode', function() {
	it('should provide a string representation for itself', function() {
		var code = 'TestCode';
		var itemCode = new openhmis.ItemCode({
			code: code
		});
		expect(code.toString()).toEqual(code);
	});
});