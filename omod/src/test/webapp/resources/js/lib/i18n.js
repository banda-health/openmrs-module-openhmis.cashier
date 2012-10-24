describe("i18n", function() {
	var lib = {};
	it("requires i18n", function() {
		require({"lib/i18n": "i18n"}, lib);
	});
	it("should handle an arbitrary number of parameters", function() {
		var __ = lib.i18n;
		expect(__("%s, %s, %s", "A", "B", "C")).toEqual("A, B, C");
	});
});