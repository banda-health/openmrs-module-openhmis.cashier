//describe("GenericListView", function() {
//	it("requires GenericListView", function() {
//		require({"view/generic": null});
//	});
//	
//	it("should should show a message when it is not displaying items", function() {
//		var collection = new openhmis.GenericCollection();
//		var listView = new openhmis.GenericListView({ model: collection });
//		listView.render();
//		expect(listView.$("p.empty").length).toEqual(1);
//	});
//});

describe("GenericListItemView", function() {
	var $ = jQuery;
	var TestClass = openhmis.GenericModel.extend({
		schema: {
			text: { type: "Text" },
			list: { type: "List", model: openhmis.GenericModel }
		}
	});
	
	it("requires GenericListItemView", function() {
		require({"view/generic": null});
	});
	
	it("should render based on model schema", function() {
		var model = new TestClass();
		var listItemView = new openhmis.GenericListItemView({ model: model });
		listItemView.render();
		expect(listItemView.$('td').length).toEqual(2);
	});
	
	it("should render simple and list attributes of a model", function() {
		var model = new TestClass({
			text: "Text",
			list: [
				{ display: "A" },
				{ display: "B" }
			]
		});
		var listItemView = new openhmis.GenericListItemView({ model: model });
		listItemView.render();
		// Check first table cell
		expect( $(listItemView.$('td')[0]).text().trim() ).toEqual("Text");
		// Check second cell		
		expect( $(listItemView.$('td')[1]).text().trim() ).toEqual("A, B");
	});
	
	it("should distinguish retired models", function() {
		var model = new TestClass({ retired: true });
		var listItemView = new openhmis.GenericListItemView({ model: model });
		listItemView.render();
		expect( $(listItemView.$("td")[0]).hasClass("retired") ).toBeTruthy();
	});
});