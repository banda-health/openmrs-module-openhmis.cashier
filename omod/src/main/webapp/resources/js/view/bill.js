$(function() {
openhmis.BillView = Backbone.View.extend({
	initialize: function() {
		this.model.add(new openhmis.LineItem({
			description: "Consultation Fee",
			quantity: 1,
			price: 50
		}));
		this.model.add(new openhmis.LineItem({
			description: "File Retrieval Fee",
			quantity: 1,
			price: 10
		}));

	},
	
	render: function() {
		var lineNumber = 1;
		var className;
		for (var index in this.model.models) {
			var item = this.model.models[index];
			className = (lineNumber % 2) === 0 ? "" : "odd";
			var lineItemView = new openhmis.LineItemView({ model: item, className: className });
			$(this.el).append(lineItemView.render().el);
			lineNumber++;
		}
		
		//this.model.each(function(item) {
		//	var lineItemView = new openhmis.LineItemView({ model: item });
		//	$(view.el).append(lineItemView.render().el);
		//});
		return this;
    }
});

});