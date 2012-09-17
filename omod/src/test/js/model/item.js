var openhmis = curl({ baseUrl: ohmisBaseUrl}, ['model/item']);

describe('ItemCode', function() {
	it('should provide a string representation for itself', function() {
		var code = 'TestCode';
		var itemCode = new openhmis.ItemCode({
			code: code
		});
		expect(code.toString()).toEqual(code);
	});
});