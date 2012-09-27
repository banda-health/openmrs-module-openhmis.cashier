define(
	[
		'lib/underscore',
		'model/generic',
		'model/department'
	],
	function(_, openhmis) {
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
					})
				},
				codes: { type: 'List', itemType: 'NestedModel', model: openhmis.ItemCode },
				prices: { type: 'List', itemType: 'NestedModel', model: openhmis.ItemPrice },
				defaultPrice: { type: 'Select', options: [] }
			},
			
			fetch: function(options) {
				options = options || {};
				var success = options.success;
				options.success = function(model, resp) {
					// Load price options
					model.schema.defaultPrice.options = model.getPriceOptions();
					if (success) success(model, resp);
				}
				return openhmis.GenericModel.prototype.fetch.call(this, options);
			},
			
			getPriceOptions: function() {
				var prices = this.get('prices');
				return _.map(prices, function(price) { return { val: price.uuid, label: openhmis.ItemPrice.prototype.format(price.price) } });
			},
			
			parse: function(resp) {
				if (resp && resp.department && _.isObject(resp.department))
					resp.department = new openhmis.Department(resp.department);
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
				var cloned_attributes = openhmis.GenericModel.prototype.toJSON.call(this);
				// Send department as UUID string
				cloned_attributes.department = cloned_attributes.department.id;
				return cloned_attributes;
			}
		});
		return openhmis;
	}
)