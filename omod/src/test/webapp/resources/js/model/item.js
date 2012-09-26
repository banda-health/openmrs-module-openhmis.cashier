describe('ItemCode', function() {
	var require = function(map, libs) {
		for (var lib in libs) { delete libs[lib]; }
		var list = [];
		for (var lib in map) { list.push(lib); }
		curl({ baseUrl: OPENHMIS_BASE_URL}, list, function() {
			libList = {};
			for (var lib in arguments) {
				libs[map[list[lib]]] = arguments[lib];
			}
		});
		waitsFor(function() {
			for (var item in map) {
				if (libs[map[item]] !== undefined) continue;
				else return false;
			}
			return true;
		}, "required resources to load", 1000);
	};

	var lib = {};

	it('should provide a string representation for itself', function() {
		require({'model/item': 'openhmis'}, lib);
		var code = 'TestCode';
		var itemCode = new lib.openhmis.ItemCode({
			code: code
		});
		expect(code.toString()).toEqual(code);
	});
});