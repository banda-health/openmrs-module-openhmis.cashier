$(function() {
openhmis.LineItemView = Backbone.View.extend({
	tagName: 'tr',
	
	template: _.template($('#line-item-template').html()),
	
	initialize: function() {
		_.bindAll(this);
	},
	
	events: {
		'click .remove': 'remove',
		'click': 'highlight'
	},
	
	highlight: function() {
		this.trigger('highlight');
		$(this.el).addClass('row_selected');
	},
	
	remove: function() {
		if (confirm(__("Are you sure you want to remove the selected item?"))) {
			this.model.destroy();
			Backbone.View.prototype.remove.call(this);
			this.trigger('remove');
		}
	},
	
	render: function() {
		$(this.el).html(this.template({ item: this.model }));
		return this;
	}
});

});