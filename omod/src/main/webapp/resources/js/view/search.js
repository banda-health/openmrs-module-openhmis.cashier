define(
	[
		'lib/jquery',
		'lib/underscore',
		'lib/backbone',
		'lib/i18n',
		'openhmis'
	],
	function($, _, Backbone, __, openhmis) {
		openhmis.BaseSearchView = Backbone.View.extend(
		/** @lends BaseSearchView.prototype */
		{
			tagName: "div",
			tmplFile: "search.html",

			/**
			 * @class BaseSearchView
			 * @classdesc Base class for search views
			 * @constructor BaseSearchView
			 * @param {map} options View options. <ul>
			 *     <li><b>modelType:</b> The model type being searched.  Used mostly for model metadata</li>
			 * </ul>
			 */
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
			
			/**
			 * Get the view's current search filter
			 * 
			 * @returns {Object} The search filter being used by the search view
			 */
			getSearchFilter: function() { return this.searchFilter; },
			
			/**
			 * Set the search filter
			 * 
			 * @param {Object} query The filter to set for the search view
			 */
			setSearchFilter: function(query) { this.searchFilter = query; },
			
			// Why is this here if it's only used in one extending class?
			onKeyPress: function(event) {
				if (event.which === 13) {
					event.stopPropagation();
				}
			},

			/**
			 * Abstract - should be implemented by extending class.  Should
			 * call {@link BaseSearchView#fetch}.
			 */
			submitForm: function() { throw "A search view needs to implement a submitForm() method!" },

			/**
			 * Abstract - should be implemented by extending class.  Should
			 * handle taking form focus.
			 */
			focus: function() { throw "A search view needs to implement a focus() method!" },
			
			// TODO: It might be better to call fetch() here instead of
			// expecting it to be done in submitForm()
			/** Called when the search should be submitted */
			onFormSubmit: function(event) {
				event.preventDefault();
				this.submitForm();
			},
			
			/**
			 * Get options and trigger fetch
			 * 
			 * @fires fetch
			 */
			fetch: function(options) {
				options = _.extend({}, this.getFetchOptions(), options);
				this.trigger("fetch", options, this);
			}
		});
		
		
		openhmis.NameSearchView = openhmis.BaseSearchView.extend(
		/** @lends NameSearchView.prototype */
		{
			tmplSelector: '#name-search',
			
			/**
			 * @class NameSearchView
			 * @extends BaseSearchView
			 * @classdesc A search view that supports searching by name.
			 * @constructor NameSearchView
			 * @param {map} options View options.  See options for
			 *     {@link BaseSearchView}.
			 */
			initialize: function(options) {
				this.events["keypress #nameSearchName"] = "onKeyPress";
				openhmis.BaseSearchView.prototype.initialize.call(this, options);
			},
			
			/** Collect user input */
			submitForm: function() {
				var name = this.$("#nameSearchName").val();
				this.searchFilter = name;
				this.fetch();
			},
			
			/**
			 * Get fetch options
			 *
			 * @param {map} options Fetch options from base view
			 * @returns {map} Map of fetch options
			 */
			getFetchOptions: function(options) {
				options = options ? options : {}
				if (this.searchFilter)
					options.queryString = openhmis.addQueryStringParameter(options.queryString, "q=" + encodeURIComponent(this.searchFilter));
				return options;
			},
			
			/** Focus the search form */
			focus: function() { this.$("#nameSearchName").focus(); },
			
			/**
			 * Render the view
			 * @returns {View} The rendered view
			 */
			render: function() {
				this.$el.html(this.template({
					model: this.model,
					searchFilter: this.searchFilter || undefined,
					__: __
				}));
				return this;
			}
		});
		
		
		openhmis.DepartmentAndNameSearchView = openhmis.BaseSearchView.extend(
		/** @lends DepartmentAndNameSearchView.prototype */
		{
			tmplSelector: '#department-name-search',
			
			/**
			 * @class DepartmentAndNameSearchView
			 * @extends BaseSearchView
			 * @classdesc A search view that supports searching by department
			 *     and name.
			 * @constructor DepartmentAndNameSearchView
			 * @param {map} options View options.  See options for
			 *     {@link BaseSearchView}.
			 *     
			 */
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
			
			/** Collect user input */
			submitForm: function() {
				var filters = this.form.getValue();
				if (!filters.department_uuid && !filters.q)
					this.searchFilter = undefined;
				else
					this.searchFilter = filters;
				this.fetch();
			},
			
			/**
			 * Get fetch options
			 *
			 * @param {map} options Fetch options from base view
			 * @returns {map} Map of fetch options
			 */
			getFetchOptions: function(options) {
				options = options ? options : {}
				if (this.searchFilter) {
					for (var filter in this.searchFilter)
						options.queryString = openhmis.addQueryStringParameter(
							options.queryString, filter + "=" + encodeURIComponent(this.searchFilter[filter]));
				}
				return options;
			},
			
			/** Focus the search form */
			focus: function() { this.$("#q").focus(); },
			
			/**
			 * Render the view
			 *
			 * @returns {View} The rendered view
			 */
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