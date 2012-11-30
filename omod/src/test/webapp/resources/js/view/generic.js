describe("GenericListView", function() {
	var CoolPersonClass = openhmis.GenericModel.extend({
		meta: {
			name: "Cool Person",
			namePlural: "Cool People"
		},
		schema: {
			name: { type: "Text" },
			age: { type: "Number"},
			friends: { type: "List" }
		},
		toString: function (){
			return this.get("name");
		}
	});
	CoolPersonClass.prototype.schema.friends.model = CoolPersonClass;

	it("requires GenericListView", function() {
		require({"view/generic": null});
	});
	
	it("should show a message when it is not displaying items", function() {
		var collection = new openhmis.GenericCollection([], { model: CoolPersonClass });
		var listView = new openhmis.GenericListView({ model: collection });
		listView.render();
		expect(listView.$("p.empty").length).toEqual(1);
		
		var jennie = new CoolPersonClass({
			name: "Jennie Reeb",
			age: 20,
			friends: [
				new CoolPersonClass ({name: "Daniel"}),
				new CoolPersonClass ({name: "stevie"})
			]
		});
		collection.add(jennie);
		expect(listView.$("p.empty").length).toEqual(0);
		
		collection.remove(jennie);
		expect(listView.$("p.empty").length).toEqual(1);
		
		spyOn(jQuery, "ajax").andCallFake(function(options) {
			options.success();
		});
		
		collection.add(jennie);
		jennie.id = "fakeID";
		jennie.retire();
		expect(listView.$("p.empty").length).toEqual(1);
	});
	
	it("should hide pagination when there are no items to display", function() {
		var collection = new openhmis.GenericCollection([], { model: CoolPersonClass });
		var listView = new openhmis.GenericListView({ model: collection });
		listView.render();
		expect(listView.$("div.paging-container").html()).toBeNull();
		var micha = new CoolPersonClass({ name: "Micha" });
		collection.add(micha);
		expect(listView.$("div.paging-container").html()).not.toBeNull();
		collection.remove(micha);
		expect(listView.$("div.paging-container").html()).toBeNull();
	});
});

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

describe("GenericAddEditView", function() {
	it("should trigger the model's \"sync\" event upon a successful save", function() {
		var view = new openhmis.GenericAddEditView({
			collection: new openhmis.GenericCollection({ model: openhmis.GenericModel })
		});
		view.model = new openhmis.GenericModel([], { urlRoot: "fakeRoot" });
		view.modelForm = view.prepareModelForm(view.model);
		spyOn(jQuery, "ajax").andCallFake(function(options) {
			options.success();
		});
		spyOn(view.model, "trigger");
		view.save();
		expect(view.model.trigger).toHaveBeenCalledWith("sync");
	});
	
});