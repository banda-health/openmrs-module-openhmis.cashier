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
				if (options) {
					this.modelType = options.modelType;
				}
				this.model = new this.modelType();
			},
			
			events: {
				"keypress #nameSearchName": "onKeyPress",
				"submit form": "doSearch"
			},
			
			onKeyPress: function(event) {
				if (event.which === 13)
					this.doSearch();
			},
			
			doSearch: function() {
				var a = 1;
			},
			
			render: function() {
				this.$el.html(this.template({ model: this.model }));
				return this;
			}
		});
	}
)