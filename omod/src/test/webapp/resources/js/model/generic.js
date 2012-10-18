describe('GenericModel', function() {
	it('should provide a string representation of itself', function() {
		var display = "Generic Model";
		var model = new openhmis.GenericModel({ display: display });
		expect(model.toString()).toEqual(display);
	});
	
	it('should convert an array to a string list', function() {
		
	});
});
