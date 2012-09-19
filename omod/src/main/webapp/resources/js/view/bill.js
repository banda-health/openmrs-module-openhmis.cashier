define(
	[
		'lib/jquery',
		'lib/underscore',
		'view/generic'
	],
	function($, _, openhmis) {
		openhmis.BillView = openhmis.GenericListView.extend({
			model: openhmis.LineItem
		});
		//openhmis.BillView = Backbone.View.extend({
		//	initialize: function() {
		//		_.bindAll(this);
		//		this.model.add(new openhmis.LineItem({
		//			description: "Consultation Fee",
		//			quantity: 1,
		//			price: 50
		//		}));
		//		this.model.add(new openhmis.LineItem({
		//			description: "File Retrieval Fee",
		//			quantity: 1,
		//			price: 10
		//		}));
		//		this.newItemView = new openhmis.LineItemView({ model: new openhmis.LineItem() });
		//		this.newItemView.on('highlight', this.clearHighlights);
		//	},
		//	
		//	clearHighlights: function() {
		//		$(this.el).find('tr').removeClass('row_selected');
		//	},
		//	
		//	render: function() {
		//		var lineNumber = 1;
		//		var className = "odd";
		//		$(this.el).html("");
		//		for (var index in this.model.models) {
		//			var item = this.model.models[index];
		//			var lineItemView = new openhmis.LineItemView({ model: item, className: className });
		//			$(this.el).append(lineItemView.render().el);
		//			lineItemView.on('highlight', this.clearHighlights);
		//			lineItemView.on('remove', this.render);
		//			lineNumber++;
		//			className = (lineNumber % 2) === 0 ? "even" : "odd";
		//		}
		//		$(this.el).append(this.newItemView.render().el);
		//		this.newItemView.$el.removeClass('even odd').addClass(className);
		//		return this;
		//	}
		//});
		
		return openhmis;
	}
);