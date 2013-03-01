describe('Item', function() {
	var testItem = jQuery.parseJSON(openhmis.testData.JSON.item);

	it("should set a list of price options in schema.defaultPrice", function() {
		var price1 = testItem.prices[0];
		var price2 = testItem.prices[1];

		var item = new openhmis.Item({ prices: [ price1, price2 ] }, { parse: true});
		var option1 = item.schema.defaultPrice.options[0];
		var option2 = item.schema.defaultPrice.options[1];
		expect(option1.val).toEqual(price1.uuid);
		expect(option1.label).toEqual("1000.00 (AIDS)");
		expect(option2.val).toEqual(price2.uuid);
		expect(option2.label).toEqual("4.40 (Default)");
	});
	it("should get the full price from the prices attribute when setting defaultPrice", function() {
		var item = new openhmis.Item(_.clone(testItem), { parse: true });
		expect(item.get("defaultPrice").name).toBeUndefined();
		// Set using a uuid string
		item.set("defaultPrice", testItem.prices[1].uuid);
		expect(item.get("defaultPrice").get("name")).toEqual(testItem.prices[1].name);
		// Set using a model containing only uuid
		var barePrice = new openhmis.ItemPrice(
			{ uuid: testItem.prices[0].uuid }
		);
		item.set("defaultPrice", barePrice);
		expect(item.get("defaultPrice").get("name")).toEqual(testItem.prices[0].name);
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