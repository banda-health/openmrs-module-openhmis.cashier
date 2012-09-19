define(
	[
		'lib/jquery',
		'lib/underscore',
		'view/generic'
	],
	function($, _, openhmis) {
		openhmis.BillView = openhmis.GenericListView.extend({
			itemView: openhmis.BillLineItemView,
			itemActions: ['remove', 'inlineEdit'],
			schema: {
				item: { type: 'Text'}
			},
			render: function() {
				openhmis.GenericListView.prototype.render.call(this, {
					listTitle: ""
				});
				this.$("table").addClass("bill");
				return this;
			}
		});
		
		openhmis.BillLineItemView = openhmis.GenericListItemView.extend({
			
		});
		
		return openhmis;
	}	
);