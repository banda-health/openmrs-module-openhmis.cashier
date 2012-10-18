describe("Item Editor", function() {
	/**
	 * @requires server response
	 */
	it("should return the proper data after a search", function() {
		var callback = jasmine.createSpy();
		var editor = new Backbone.Form.editors.Item();

		spyOn(jQuery, "ajax").andCallFake(function(options) {
			var resp = jQuery.parseJSON(openhmis.testData.JSON.singleItem);
			options.success(resp);
		});
		
		var request = { term: "Test term" }
		var response = jasmine.createSpy();
		editor.doSearch(request, response, openhmis.Item);
		waitsFor(function() {
			return response.wasCalled;
		}, "search to complete", 1000);
		
		runs(function() {
			var data = response.mostRecentCall.args[0][0];
			expect(data.codes.length).toEqual(1);
			expect(data.department_uuid).toEqual("faf2f364-189c-4959-9428-4f917f52b8de");
			expect(data.label).toEqual("Ciprofloxacin");
			expect(data.val).toEqual("20aa2858-6642-4b7a-b456-b07621c26538");
		});
	});
});