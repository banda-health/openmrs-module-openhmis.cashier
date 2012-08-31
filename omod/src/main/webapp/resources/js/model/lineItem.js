openhmis.LineItem = Backbone.Model.extend({

	getTotal: function() {
		if (this.get('quantity') === undefined
			|| this.get('price') === undefined)
			return undefined;
		return this.get('price') * this.get('quantity');
	}
});

openhmis.LineItemCollection = Backbone.Collection.extend({
   model: openhmis.LineItem 
});