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
				quantity: 'BasicNumber',
				price: 'BasicNumber',
				total: { type: 'BasicNumber' }
			},
			
			initialize: function() {
				this.schema.total.value = this.getTotal;
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