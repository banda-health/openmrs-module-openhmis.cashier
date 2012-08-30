$(function() {
openhmis.LineItemView = Backbone.View.extend({
	tagName: 'tr',
	
	template: _.template($('#line-item-template').html()),
	
	render: function() {
		$(this.el).html(this.template({ item: this.model }));
		return this;
	}
});

});