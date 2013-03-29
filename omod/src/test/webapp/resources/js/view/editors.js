describe("Item Editor", function() {
	it("requires Cashier editors", function() {
		require([openhmis.url.cashierBase + 'js/view/editors']);
	});
	/**
	 * @requires server response
	 */
	it("should return the proper data after a search", function() {
		var response = jasmine.createSpy();

		runs(function() {
			var editor = new Backbone.Form.editors.Item();
	
			spyOn(jQuery, "ajax").andCallFake(function(options) {
				var resp = jQuery.parseJSON(openhmis.testData.JSON.itemCollection);
				options.success(resp);
			});
			
			var request = { term: "Test term" }
			editor.doSearch(request, response, openhmis.Item);
		});

		waitsFor(function() {
			return response.wasCalled;
		}, "search to complete", 1000);
		
		runs(function() {
			var data = response.mostRecentCall.args[0][0];
			expect(data.codes.length).toEqual(2);
			expect(data.department_uuid).toEqual("faf2f364-189c-4959-9428-4f917f52b8de");
			expect(data.label).toEqual("Ciprofloxacin");
			expect(data.val).toEqual("20aa2858-6642-4b7a-b456-b07621c26538");
		});
	});
});