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
				"submit form": "onFormSubmit"
			},
			
			onKeyPress: function(event) {
				if (event.which === 13) {
					event.stopPropagation();
					//this.submitForm();
				}
			},

			onFormSubmit: function(event) {
				event.preventDefault();
				this.submitForm();
			},
			
			submitForm: function() {
				var name = this.$("#nameSearchName").val();
				this.lastSearch = name;
				var query = (name !== "") ? "q=" + encodeURIComponent(name) : null;
				this.doSearch(query);
			},
			
			doSearch: function(query) {
				var collection = new openhmis.GenericCollection([], {
					model: this.modelType });
				var self = this;
				collection.search(query, {
					remember: true,
					success: function(model, resp) {
						self.trigger("search", model);
					}
				});
			},
			
			render: function() {
				this.$el.html(this.template({
					model: this.model,
					lastSearch: this.lastSearch || undefined
				}));
				return this;
			}
		});
	}
)