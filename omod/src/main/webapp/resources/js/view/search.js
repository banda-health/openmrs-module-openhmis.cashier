define(
	[
		'lib/jquery',
		'lib/backbone',
		'openhmis'
	],
	function($, Backbone, openhmis) {
		openhmis.NameSearchView = Backbone.View.extend({
			tagName: "div",
			tmplFile: "search.html",
			tmplSelector: '#name-search',
			
			initialize: function(options) {
				this.template = this.getTemplate();
				this.model = new this.model();
			},
			
			render: function() {
				this.$el.html(this.template({ model: this.model }));
				return this;
			}
		});
	}
)