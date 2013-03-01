describe("ItemAddEditView", function() {
	var departmentsObj = $.parseJSON(openhmis.testData.JSON.departmentCollection);

	var listIsRendered = function(view) {
		var call = view.trigger.mostRecentCall;
		return call.args !== undefined && call.args[0] === "readyToAdd";
	}

	beforeEach(function() {
		//TODO: Maybe we can make a helper class to make this spying less ugly
		// Setup mocked AJAX responses
		spyOn(jQuery, "ajax").andCallFake(function(options) {
			if (options.url.indexOf(openhmis.Department.prototype.meta.restUrl) !== -1)
				options.success(departmentsObj);
			else
				options.success([]);
		});
	});

	it("requires ItemAddEditView", function() {
		var required = {};
		required[openhmis.url.cashierBase + "js/view/item"] = null;
		require(required);
	});
	
	it("should edit an item's department", function() {
		var secondDepartment = departmentsObj.results[1];		
		var view = new openhmis.ItemAddEditView({
			collection: new openhmis.GenericCollection([], { model: openhmis.Item }) });
		var item = new openhmis.Item($.parseJSON(openhmis.testData.JSON.item), { parse: true });
		// Need to wait for this so that the form can be submitted properly
		var baseEditor = Backbone.Form.editors.Base.prototype;
		spyOn(baseEditor, "trigger").andCallThrough();
		view.edit(item);
		waitsFor(function() { return listIsRendered(baseEditor); },
			"form lists to be rendered", 1000);
		runs(function() {
			// Change department on form
			view.$("#" + item.cid + "_department").val(secondDepartment.uuid);
			view.save();
			expect(item.get("department").id).toEqual(secondDepartment.uuid);
		});
	});
	
	it("should edit an item's default price", function() {
		var item = new openhmis.Item($.parseJSON(openhmis.testData.JSON.item), { parse: true });
		var secondPrice = item.get("prices")[1];
		var view = new openhmis.ItemAddEditView({
			collection: new openhmis.GenericCollection([], { model: openhmis.Item }) });
		// Need to wait for this so that the form can be submitted properly
		var baseEditor = Backbone.Form.editors.Base.prototype;
		spyOn(baseEditor, "trigger").andCallThrough();
		view.edit(item);
		waitsFor(function() { return listIsRendered(baseEditor); },
			"form lists to be rendered", 1000);
		runs(function() {
			// Change department on form
			view.$("#" + item.cid + "_defaultPrice").val(secondPrice.id);
			view.save();
			expect(item.get("defaultPrice").id).toEqual(secondPrice.id);
		});		
	});
});