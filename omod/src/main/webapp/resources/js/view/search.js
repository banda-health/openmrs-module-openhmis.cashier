define(
	[
		'lib/jquery',
		'lib/underscore',
		'lib/backbone',
		'openhmis'
	],
	function($, _, Backbone, openhmis) {
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
			
			getSearchFilter: function() { return this.searchFilter; },
			
			setSearchFilter: function(query) { this.searchFilter = query; },

			onKeyPress: function(event) {
				if (event.which === 13) {
					event.stopPropagation();
				}
			},

			onFormSubmit: function(event) {
				event.preventDefault();
				this.submitForm();
			},
			
			submitForm: function() {
				var name = this.$("#nameSearchName").val();
				this.searchFilter = name;
				this.fetch();
			},
			
			getFetchOptions: function(options) {
				options = options ? options : {}
				if (this.searchFilter)
					options.queryString = openhmis.addQueryStringParameter(options.queryString, "q=" + encodeURIComponent(this.searchFilter));
				return options;
			},
			
			fetch: function(options) {
				options = _.extend({}, this.getFetchOptions(), options);
				this.trigger("fetch", options, this);
			},
			
			render: function() {
				this.$el.html(this.template({
					model: this.model,
					searchFilter: this.searchFilter || undefined
				}));
				return this;
			}
		});
	}
)