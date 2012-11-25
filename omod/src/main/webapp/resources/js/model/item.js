define(
	[
		'lib/underscore',
		'model/generic',
		'lib/i18n',
		'model/department'
	],
	function(_, openhmis, __) {
		openhmis.ItemCode = openhmis.GenericModel.extend({
			meta: {
				name: "Item Code",
				namePlural: "Item Codes",
				openmrsType: 'metadata'
			},
			schema: {
				code: { type: 'Text' }
			},
			toString: function() { return this.get('code'); }
		});
		
		openhmis.ItemPrice = openhmis.GenericModel.extend({
			meta: {
				name: "Item Price",
				namePlural: "Item Prices",
				openmrsType: 'metadata'
			},
			schema: {
				price: { type: 'BasicNumber' }
			},
			format: function(price) {
				if (price === undefined) return 0;
				return price.toFixed(2);
			},
			
			toString: function() { return this.format(this.get('price')); }
		});
		
		openhmis.Item = openhmis.GenericModel.extend({
			meta: {
				name: "Item",
				namePlural: "Items",
				openmrsType: 'metadata',
				restUrl: 'item'
			},
			schema: {
				name: { type: 'Text' },
				department: {
					type: 'DepartmentSelect',
					options: new openhmis.GenericCollection(null, {
						model: openhmis.Department,
						url: '/department'
					}),
					objRef: true
				},
				codes: { type: 'List', itemType: 'NestedModel', model: openhmis.ItemCode },
				prices: { type: 'List', itemType: 'NestedModel', model: openhmis.ItemPrice },
				defaultPrice: { type: 'Select', options: [], objRef: true }
			},
			
			initialize: function(attributes, options) {
				openhmis.GenericModel.prototype.initialize.call(this, attributes, options);
				this.setPriceOptions();
			},
			
			fetch: function(options) {
				options = options || {};
				var success = options.success;
				options.success = function(model, resp) {
					// Load price options
					model.setPriceOptions();
					if (success) success(model, resp);
				}
				return openhmis.GenericModel.prototype.fetch.call(this, options);
			},
			
			getCodesList: function(list) {
				var codes, schema;
				if (list !== undefined) {
					codes = list;
					schema = { model: openhmis.ItemCode }
				} else {
					codes = this.get("codes");
					schema = this.schema.codes
				}
				return openhmis.GenericCollection.prototype.toString.call(codes, schema);
			},
			
			setPriceOptions: function(prices) {
				prices = prices ? prices : this.get('prices');
				this.schema.defaultPrice.options = _.map(prices, function(price) { return { val: price.uuid || price.price, label: openhmis.ItemPrice.prototype.format(price.price) } });
			},
			
			validate: function(attrs, options) {
				if (!attrs.name) return { name: __("A name is required") }
				if (!attrs.department) return { department: __("An item needs to be associated with a department") }
				if (!attrs.prices || attrs.prices.length < 1) return { prices: __("An item should have at least one price.") }
				if (!attrs.defaultPrice) return { defaultPrice: "Please specify a default price."}
				return null;
			},
			
			parse: function(resp) {
				if (resp) {
					if (resp.department && _.isObject(resp.department))
						resp.department = new openhmis.Department(resp.department);
				}
				return resp;
			},
			
			toJSON: function() {
				if (this.attributes.codes !== undefined) {
					// Can't set these, so need to remove them from JSON
					for (var code in this.attributes.codes)
						delete this.attributes.codes[code].resourceVersion;
					for (var price in this.attributes.prices)
						delete this.attributes.prices[price].resourceVersion;
				}
				return openhmis.GenericModel.prototype.toJSON.call(this);
			},
			
			toString: function() {
				if (this.get("codes").length > 0)
					return this.get("codes")[0].code + ' - ' + this.get("name");
				return this.get("name");
			}
		});
		return openhmis;
	}
)