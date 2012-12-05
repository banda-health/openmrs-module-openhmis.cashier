define(
	[
		'lib/jquery',
		'lib/underscore',
		'lib/backbone',
		'lib/i18n',
		'openhmis'
	],
	function($, _, Backbone, __, openhmis) {
		openhmis.BaseSearchView = Backbone.View.extend({
			tagName: "div",
			tmplFile: "search.html",

			initialize: function(options) {
				this.template = this.getTemplate();
				if (options) {
					this.modelType = options.modelType;
				}
				this.model = new this.modelType();
			},
			
			events: {
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
			
			fetch: function(options) {
				options = _.extend({}, this.getFetchOptions(), options);
				this.trigger("fetch", options, this);
			}
		});

		openhmis.NameSearchView = openhmis.BaseSearchView.extend({
			tmplSelector: '#name-search',
			initialize: function(options) {
				this.events["keypress #nameSearchName"] = "onKeyPress";
				openhmis.BaseSearchView.prototype.initialize.call(this, options);
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
			
			focus: function() { this.$("#nameSearchName").focus(); },

			render: function() {
				this.$el.html(this.template({
					model: this.model,
					searchFilter: this.searchFilter || undefined,
					__: __
				}));
				return this;
			}
		});
		
		openhmis.DepartmentAndNameSearchView = openhmis.BaseSearchView.extend({
			tmplSelector: '#department-name-search',
			
			initialize: function(options) {
				this.events['change #department_uuid'] = 'submitForm';
				this.events['click #submit'] = 'submitForm';
				openhmis.BaseSearchView.prototype.initialize.call(this, options);
				var departmentCollection = new openhmis.GenericCollection([], { model: openhmis.Department });
				departmentCollection.on("reset", function(collection) {
					collection.unshift(new openhmis.Department({ name: __("Any") }));
				});
				this.form = new Backbone.Form({
					className: "inline",
					schema: {
						department_uuid: {
							title: __("Department"),
							type: "Select",
							options: departmentCollection
						},
						q: {
							title: __("%s Identifier or Name", this.model.meta.name),
							type: "Text",
							editorClass: "search"
						}
					},
					data: {}
				});
			},
			
			submitForm: function() {
				var filters = this.form.getValue();
				if (!filters.department_uuid && !filters.q)
					this.searchFilter = undefined;
				else
					this.searchFilter = filters;
				this.fetch();
			},
			
			getFetchOptions: function(options) {
				options = options ? options : {}
				if (this.searchFilter) {
					for (var filter in this.searchFilter)
						options.queryString = openhmis.addQueryStringParameter(
							options.queryString, filter + "=" + encodeURIComponent(this.searchFilter[filter]));
				}
				return options;
			},

			focus: function() { this.$("#q").focus(); },
			
			render: function() {
				this.$el.html(this.template({ __: __ }));
				this.$("div.box").append(this.form.render().el);
				if (this.searchFilter)
					this.form.setValue(this.searchFilter);
				this.$("form").addClass("inline");
				this.$("form ul").append('<button id="submit">'+__("Search")+'</button>');
				return this;
			}
		});
	}
)