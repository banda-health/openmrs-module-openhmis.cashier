openhmis.LineItem = Backbone.Model.extend({
	getTotal: function() {
		return this.get('price') * this.get('quantity');
	}
});

openhmis.LineItemCollection = Backbone.Collection.extend({
   model: openhmis.LineItem 
});