describe('GenericModel', function() {
	it("should provide a string representation of itself, using 'display' attribute if it exists", function() {
		var display = "Generic Model";
		var model = new openhmis.GenericModel({ display: display });
		expect(model.toString()).toEqual(display);
	});
	
	it("should obey trackUnsaved option and be unsaved when new or when changed", function() {
		// Option not enabled
		var model = new openhmis.GenericModel();
		expect(model.isUnsaved()).toEqual(undefined);
		
		expect(model.setUnsaved).toThrow("trackUnsaved option should be enabed to use this funtion.");

		// Option enabled -- new object
		model = new openhmis.GenericModel({}, { trackUnsaved: true });
		 // We'll wait for a change to say that a new model is unsaved
		expect(model.isUnsaved()).toBeFalsy();

		// Option enabled, existing model
		model = new openhmis.GenericModel({ uuid: "f546f45d-b000-4f9f-be93-1ec29aea0a8b" }, { trackUnsaved: true });
		expect(model.isUnsaved()).toBeFalsy();
		
		// Fire change event
		model.set("foo", "bar");
		expect(model.isUnsaved()).toBeTruthy();		
	});
	
	it("should throw exception if no url property is set", function() {
		var model = new openhmis.GenericModel();
		expect(model.url).toThrow('A "url" property or function must be specified');
	});
	
	it("should allow setting a url root", function() {
		var path = "/path/to/model";
		var model = new openhmis.GenericModel({}, { urlRoot: path });
		expect(model.url()).toEqual(path);
	});
	
	it("should compose url from meta.restUrl and openhmis config", function() {
		var model = new openhmis.GenericModel();
		model.meta = {
			restUrl: 'foobar'
		}
		expect(model.url()).toEqual(openhmis.config.restUrlRoot + model.meta.restUrl);
	});
	
	it("should serialize itself according to Backbone if no schema is specified", function() {
		var model = new openhmis.GenericModel({
			foo: "bar"
		});
		var obj = model.toJSON();
		expect(obj.foo).toEqual("bar");
	});
	
	it("should")
	
	it("should serialize itself according to the schema if one is specified", function() {
		var subClass = openhmis.GenericModel.extend({
			schema: {
				name: { type: "Text" },
				age: { type: "Number" },
				parent: { type: "Object", objRef: true }
			}
		});
		var model = new subClass({
			name: "Brian",
			age: 20,
			parent: new openhmis.GenericModel({ uuid: "e2756470-1c28-11e2-892e-0800200c9a66"}),
			foo: "bar"
		});
		var obj = model.toJSON();
		expect(obj.name).toEqual("Brian");
		expect(obj.age).toEqual(20);
		expect(typeof obj.parent).toEqual("string");
		expect(obj.foo).toBeUndefined();
	});
});

describe("GenericCollection", function() {
	it("should convert an array to a string list", function() {
		var itemObj = jQuery.parseJSON(openhmis.testData.JSON.item);
		var schema = {
			model: openhmis.ItemCode
		}
		var list = openhmis.GenericCollection.prototype.toString.call(itemObj.codes, schema);
		expect(list).toEqual("P3, P1");
	});
	
	it("should allow setting a base url", function() {
		var model = new openhmis.GenericModel();
		var collection = new openhmis.GenericCollection([ model ], { baseUrl: "/path/to/collection" });
		expect(model.url()).toEqual("/path/to/collection");
	});
	
	it("should allow setting just a resource url", function() {
		var model = new openhmis.GenericModel();
		var collection = new openhmis.GenericCollection([ model ], { url: "resource" });
		expect(model.url()).toEqual(openhmis.config.restUrlRoot + "resource");
	});
	
	it("should allow setting a base url and resource url", function() {
		var model = new openhmis.GenericModel();
		var collection = new openhmis.GenericCollection([ model ], {
			baseUrl: "/path/to/",
			url: "resource"
		});
		expect(model.url()).toEqual("/path/to/resource");		
	});
	
	it("should search its model for a url root", function() {
		var modelClass = openhmis.GenericModel.extend({
			urlRoot: "/path/to/resource"
		});
		var model = new modelClass();
		var collection = new openhmis.GenericCollection([ model ], { model: modelClass });
		expect(model.url()).toEqual("/path/to/resource");
		expect(collection.url).toEqual("/path/to/resource");
	});
});
