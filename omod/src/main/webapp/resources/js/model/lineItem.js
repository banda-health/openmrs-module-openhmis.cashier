define(
	[
		'lib/backbone',
		'model/generic',
		'model/item'
	],
	function(Backbone, openhmis) {
		openhmis.LineItem = openhmis.GenericModel.extend({
			meta: {
				name: 'Line Item',
				namePlural: 'Line Items'
			},
			schema: {
				item: { type: 'NestedModel', model: openhmis.Item },
				quantity: { type: 'BasicNumber' },
				price: { type: 'BasicNumber' },
				total: { type: 'BasicNumber', readOnly: true }
			},
			
			initialize: function() {
				this.schema.total.value = this.getTotal;
			},
			
			get: function(attr) {
				switch (attr) {
					case 'total':
						return this.getTotal();
					default:
						return openhmis.GenericModel.prototype.get.call(this, attr);
				}
			},
			
			getTotal: function() {
				if (this.get('quantity') === undefined
					|| this.get('price') === undefined)
					return undefined;
				return this.get('price') * this.get('quantity');
			}
		});
		
		return openhmis;
	}
);