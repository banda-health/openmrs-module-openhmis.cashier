define(
	[
		'lib/jquery',
		'lib/underscore',
		'lib/backbone',
		'lib/i18n',
		'openhmis',
		'lib/backbone-forms',
		'model/generic',
		'view/list',
		'view/paginate',
		'view/editors',
		'link!/openmrs/scripts/jquery/dataTables/css/dataTables_jui.css'
	],
	function($, _, Backbone, __, openhmis) {
		
		/*======================================================================
		 *
		 * GenericAddEditView
		 *
		 */
		openhmis.GenericAddEditView = Backbone.View.extend(
		/** @lends GenericAddEditView.prototype */
		{
			tmplFile: 'generic.html',
			tmplSelector: '#add-edit-template',
			
			/**
			 * @class GenericAddEditView
			 * @classdesc Generic view for performing add/edit/delete operations
			 *     on a GenericModel
			 * @constructor GenericAddEditView
			 * @param {map} options Optional. View options.
			 */
			initialize: function(options) {
				_.bindAll(this);
				this.collection = options.collection;
				this.model = new this.collection.model();
				this.template = this.getTemplate();
			},
			
			events: {
				'click a.addLink': 'beginAdd',
				'click .cancel': 'cancel',
				'click .submit': 'save',
				'submit form': 'save',
				'click button.retireOrVoid': 'retireOrVoid',
				'click button.unretireOrUnvoid': 'unretireOrUnvoid',
				'click button.purge': 'purge'
			},
			
			/**
			 * Prepare a Backbone form based on a model
			 * 
			 * @static
			 * @param {Model} model A model with a defined backbone-forms schema
			 * @param {map} options Options for the Backbone form
			 * @returns {Form} A prerendered Backbone form
			 */
			prepareModelForm: function(model, options) {
				options = options ? options : {};
				var formFields = [];
				for (var key in model.schema) {
					if (key === 'retired') continue;
					if (model.schema[key].hidden === true) continue;
					formFields.push(key);
				}
				var formOptions = {
					model: model,
					fields: formFields,
					classNames: { errClass: "error" }
				};
				formOptions = _.extend(options, formOptions);
				var modelForm = new Backbone.Form(options)
				modelForm.render();
				return modelForm;
			},
			
			/** Switch to Add mode, for adding a new model. */
			beginAdd: function() {
				this.model = new this.collection.model(null, { urlRoot: this.collection.url });
				this.render();
				$(this.addLinkEl).hide();
				$(this.retireVoidPurgeEl).hide();
				$(this.titleEl).show();
				this.modelForm = this.prepareModelForm(this.model);
				$(this.formEl).prepend(this.modelForm.el);
				$(this.formEl).show();
				$(this.formEl).find('input')[0].focus();
			},
			
			/**
			 * Cancel the current view
			 *
			 * @fires cancel
			 */
			cancel: function() {
				this.trigger('cancel');
				$(this.addLinkEl).show();
				$(this.titleEl).hide();
				$(this.formEl).hide();
				$(this.retireVoidPurgeEl).hide();
			},
			
			/**
			 * Switch to edit mode, for editing a model
			 *
			 * @param {GenericModel} A model for editing
			 */
			edit: function(model) {
				this.model = model;
				var self = this;
				this.model.fetch({
					success: function(model, resp) {
						self.render();
						$(self.titleEl).show();
						self.modelForm = self.prepareModelForm(self.model);
						$(self.formEl).prepend(self.modelForm.el);
						$(self.formEl).show();
						$(self.retireVoidPurgeEl).show();
						$(self.formEl).find('input')[0].focus();
					},
					error: openhmis.error
				});
			},
			
			/**
			 * Save the current model
			 *
			 * @param {event} event Optional.  Pass an event.
			 */
			save: function(event) {
				if (event) event.preventDefault();
				var errors = this.modelForm.commit();
				if (errors) return;
				var view = this;
				this.model.save(null, {
					success: function(model, resp) {
						if (model.collection === undefined) {
							view.collection.add(model);
						}
						model.trigger("sync");
						view.cancel();
					},
					error: function(model, resp) { openhmis.error(resp); }
				});
			},
			
			/** Retire or void an existing model */
			retireOrVoid: function() {
				var reason = this.$('#reason').val();
				var view = this;
				this.model.retire({
					reason: reason,
					success: function(model, resp) {
						view.cancel();
					},
					error: function(model, resp) { openhmis.error(resp); }
				});
			},
			
			/** Unretire or unvoid an existing model */
			unretireOrUnvoid: function() {
				if (confirm("Are you sure you want to unretire this object? It will then be restored to the system")) {
					var view = this;
					this.model.unretire({
						success: function(model, resp) {
							view.model.trigger('sync', model, resp);
							view.cancel();
						},
						error: function(model, resp) { openhmis.error(resp); }
					});
				}
			},
			
			/** Purge an existing model */
			purge: function() {
				if (confirm(__("Are you sure you want to purge this object? It will be permanently removed from the system."))) {
					var view = this;
					this.model.purge({
						success: function(model) { view.cancel(); },
						error: function(model, resp) { openhmis.error(resp); }
					});
				}
			},
			
			/**
			 * Render the view
			 * @returns {View} The rendered view
			 */
			render: function() {
				this.$el.html(this.template({ model: this.model }));
				this.addLinkEl = this.$('a.addLink');
				this.titleEl = this.$('b.title');
				this.formEl = this.$('div.form');
				this.retireVoidPurgeEl = this.$('div.retireVoidPurge');
				return this;
			}
		});
		
		
		/*======================================================================
		 *
		 * GenericListView
		 *
		 */
		openhmis.GenericListView = Backbone.View.extend(
		/** @lends GenericListView.prototype */
		{
			// The template file to use
			tmplFile: 'generic.html',
			
			// The jQuery selector for the template
			tmplSelector: '#generic-list',
			
			/** The default ListItemView to use to display each item */
			itemView: openhmis.GenericListItemView,
			
			/**
			 * A list of other FetchHelpers that may affect the fetch results
			 * for this view.  A FetchHelper must implement the
			 * <b>getFetchOptions<b> method which will return
			 * 
			 * @type Array
			 */
			fetchable: [],
			
			/**
			 * @class GenericListView
			 * @classdesc Displays a GenericCollection in a tabular list
			 * @constructor GenericListView
			 * @param {map} options Options for the GenericListView.
			 * 	  <ul>
			 *     <li><b>model:</b> Expected to be a GenericCollection</li>
			 *     <li><b>itemView:</b> The view type to use to display each item in the list. Defaults to GenericListItemView.</li>
			 *     <li><b>schema:</b> Can be specified to override the schema of the models in the collection.</li>
			 *     <li><b>listTitle:</b> Title to be displayed for the list.</li>
			 *     <li><b>itemActions:</b> A list of actions to enable for the items in the list. GenericListItemView supports "remove" and "inlineEdit".</li>
			 *     <li><b>listFields:</b> A list of attributes in the model's schema to display as columns in the list.  Defaults to all the attributes in the model's schema.</li>
			 *     <li><b>listExcludeFields:</b> A list of attributes in the model's schema to exclude from the list's columns.</li>
			 *     <li><b>showPaging:</b> Whether to display pagination controls. Defaults to true.</li>
			 *     <li><b>pageSize:</b> Set the initial number of results to show in the list</li>
			 *     <li><b>showRetiredOption:</b> Whether to display the option of showing/hiding retired/voided items.</li>
			 *     <li><b>hideIfEmpty:</b> If true and the view's collection is empty, the entire list view will not be displayed.  Defaults to false.</li>
			 *    </ul>
			 */
			initialize: function(options) {
				_.bindAll(this);
				this.options = {};
				this.paginateView = new openhmis.PaginateView({ model: this.model, pageSize: 5 });
				this.paginateView.on("fetch", this.fetch);
				this.fetchable.push(this.paginateView);
				if (options !== undefined) {
					//TODO: Remove this  vvv
					this.addEditView = options.addEditView;
					
					this.itemView = options.itemView ? options.itemView : openhmis.GenericListItemView
					if (options.schema) this.schema = options.schema;
					
					// Why is this inside options??
					this.template = this.getTemplate();
					
					this.options.listTitle = options.listTitle;
					this.options.itemActions = options.itemActions || [];
					this.options.includeFields = options.listFields;
					this.options.excludeFields = options.listExcludeFields;
					this.options.showPaging = options.showPaging !== undefined ? options.showPaging : true;
					if (options.pageSize) this.paginateView.setPageSize(options.pageSize);
					this.options.showRetiredOption = options.showRetiredOption !== undefined ? options.showRetiredOption : true;
					this.options.hideIfEmpty = options.hideIfEmpty !== undefined ? options.hideIfEmpty : false;
				}
				if (this.addEditView !== undefined)
					this.addEditView.on('cancel', this._deselectAll);
				this.model.on("reset", this.render);
				this.model.on("add", this.addOne);
				this.model.on("remove", this.onItemRemoved);
				this.showRetired = false;
				this._determineFields();
			},
			
			events: {
				'change #showRetired': '_toggleShowRetired'
			},
			
			/**
			 * Add another item to the view.
			 *
			 * Hooks up:
			 *     <ul>
			 *      <li><b>itemSelected</b> on ListItemView <b>select</b> and <b>focus</b></li>
			 *      <li><b>itemRemoved</b> on ListItemView <b>remove</b></li>
			 *     </ul>

			 * @param {model} model Required. A GenericModel to be added to the
			 *     list.
			 * @param {schema} schema Optional. A schema to override the model's
			 *     schema.
			 * @param {int} lineNumber Optional. The lineNumber of the item,
			 *     used to determine styles for alternating rows.  Otherwise
			 *     this is determined using jQuery and the DOM.
			 */
			addOne: function(model, schema, lineNumber) {
				if (this.showRetired === false && model.isRetired()) return null;
				if ((this.$el.html() === "" && this.options.hideIfEmpty === true)
					|| this.$("p.empty").length === 1) {
					this.render();
					// Re-rendering the entire list means we don't have to
					// continue adding this item
					return null;
				}
				schema = schema ? _.extend({}, model.schema, schema) : _.extend({}, this.model.model.prototype.schema, this.schema || {});
				
				// Determine class name for alternating row styling
				var className = "evenRow";
				if (lineNumber && !isNaN(lineNumber))
					className = lineNumber % 2 === 0 ? "evenRow" : "oddRow";
				else {
					var $rows = this.$('tbody.list tr');
					if ($rows.length > 0) {
						var lastRow = $rows[$rows.length - 1];
						if ($(lastRow).hasClass("evenRow"))
							className = "oddRow";
					}
				}
				
				var itemView = new this.itemView({
					model: model,
					fields: this.fields,
					schema: schema,
					className: className,
					actions: this.options.itemActions
				});
				model.view = itemView;
				this.$('tbody.list').append(itemView.render().el);
				itemView.on('select focus', this.onItemSelected);
				itemView.on('remove', this.onItemRemoved);
				var view = this;
				//TODO: Should de-anonymize this function
				model.on("retire", function(item) {
					if (!view.showRetired)
						itemView.remove();
						view.onItemRemoved(item);
				});
				return itemView;
			},
			
			/**
			 * Called when a ListItemView is removed.
			 * 
			 * @param {GenericListItemView} item The view that has been removed
			 */
			onItemRemoved: function(item) {
				if (this._visibleItemCount() === 0)
					this.render();
				else
					this._colorRows();
			},
			
			/**
			 * Called when a ListItemView is selected.
			 *
			 * @param {GenericListItemView} view The view that has been selected
			 */
			onItemSelected: function(view) {
				this._deselectAll();
				this.selectedItem = view;
				if (this.addEditView) this.addEditView.edit(view.model);
			},
			
			/** Called when the view loses form focus. */
			blur: function() {
				this._deselectAll();
			},
			
			/** Called when the view gains form focus. */
			focus: function() {
				if (this.selectedItem) {
					this.selectedItem.focus();
				}
			},
			
			/**
			 * Use the GenericCollection to fetch an updated list of items from
			 * the server.  Uses the list of fetchables.
			 * 
			 * @param {map} options Options for the fetch operation.
			 * @param {FetchHelper} sender Optional. The FetchHelper that
			 *     called for this fetch.
			 */
			fetch: function(options, sender) {
				options = options ? options : {};
				for (var f in this.fetchable) {
					if (this.fetchable[f] !== sender)
						options = this.fetchable[f].getFetchOptions(options);
				}
				if(this.showRetired) options.queryString = openhmis.addQueryStringParameter(options.queryString, "includeAll=true");
				this.model.fetch(options);
			},
			
			/**
			 * Render the list view
			 *
			 * @param {map} extraContext Optional. Extra context to override the
			 *     base context and be passed to the template.
			 * @returns {View} The rendered view
			 */
			render: function(extraContext) {
				var self = this;
				var length = this._visibleItemCount();
				if (length === 0 && this.options.hideIfEmpty) {
					this.$el.html("");
					return this;
				}
				var schema = _.extend({}, this.model.model.prototype.schema, this.schema || {});
				var pagingEnabled = this.options.showPaging && length > 0;
				var context = {
					list: this.model,
					listLength: length,
					fields: this.fields,
					modelMeta: this.model.model.prototype.meta,
					modelSchema: this.model.model.prototype.schema,
					showRetired: this.showRetired,
					pagingEnabled: pagingEnabled,
					options: this.options
				}
				if (extraContext !== undefined) context = _.extend(context, extraContext);
				this.$el.html(this.template(context));
				if (pagingEnabled) {
					this.paginateView.setElement(this.$(".paging-container"));
					this.paginateView.render();
					this.paginateView.getRenderedPageSizeEl(this.$("span.pageSize"));
				}
				var view = this;
				var lineNumber = 0;
				this.model.each(function(model) {
					view.addOne(model, schema, lineNumber)
					lineNumber++;
				});
				return this;
			},
			
			/**
			 * Reassigns alternating styles to item views.
			 * 
			 * @private
			 */
			_colorRows: function() {
				var lineNumber = 0;
				this.$el.find('tbody tr').each(function() {
					$(this)
					.removeClass("evenRow oddRow")
					.addClass((lineNumber % 2 === 0) ? "evenRow" : "oddRow");
					lineNumber++;
				});
			},
			
			/**
			 * Determine the number of items in the collection that are
			 * actually visible according to UI settings
			 *
			 * @private
			 */
			_visibleItemCount: function() {
				if (this.showRetired)
					return this.model.length;
				return this.model.filter(function(item) { return !item.isRetired() }).length;
			},
			
			/**
			 * Determine the fields that should be shown as columns in the table
			 *
			 * @private
			 */
			_determineFields: function() {
				if (this.options.includeFields !== undefined)
					this.fields = this.options.includeFields;
				else
					this.fields = _.keys(this.model.model.prototype.schema);
				if (this.options.excludeFields !== undefined) {
					var argv = _.clone(this.options.excludeFields);
					argv.unshift(this.fields);
					this.fields = _.without.apply(this, argv);
				}
			},
			
			/**
			 * Remove selection style from all rows.
			 *
			 * @private
			 */
			_deselectAll: function() {
				this.$('tr').removeClass('row_selected');
			},

			/**
			 * Event handler for the "Show Retired" control.
			 * 
			 * @private
			 */			
			_toggleShowRetired: function(event) {
				this.showRetired = event.target.checked;
				this.fetch();
			}
		});
		

		/*======================================================================
		 *
		 * GenericListItemView
		 *
		 */
		openhmis.GenericListItemView = Backbone.View.extend(
		/** @lends GenericListItemView.prototype */
		{
			// Name of the HTML tag to use for the view's containing element
			tagName: "tr",
			
			// The template file to use
			tmplFile: "generic.html",

			// The jQuery selector for the template
			tmplSelector: '#generic-list-item',

			/**
			 * List of actions (strings) that the view should support by default.
			 * GenericListItemView supports: <ul>
			 *     <li><b>remove:</b> Support removing list items</li>
			 *     <li><b>inlineEdit:</b> Support editing the values of the item attributes</li>
			 * </ul>
			 *
			 * @type Array
			 */
			actions: [], // see enableActions()

			/**
			 * @class GenericListItemView
			 * @classdesc Displays a single model in a GenericListView
			 * @constructor GenericListItemView
			 * @param {map} options Options for the GenericListItemView
			 */
			initialize: function(options) {
				if (options !== undefined) {
					this.fields = options.fields ? options.fields : _.keys(this.model.schema);
					if (options.actions) this.actions = options.actions;
					if (options.schema) this.schema = options.schema;
				}
				_.bindAll(this);
				this.template = this.getTemplate();
				this.model.on("sync", this.render);
				this.model.on('destroy', this.remove);
				this.model.on("change", this.onModelChange);
				this._enableActions();
			},
			
			events: {
				'click td': 'select',
			},
			
			/**
			 * Cause the item to be selected
			 * @fires select
			 */
			select: function() {
				// If this list item has a form, we'll use that for focus
				if (this.form !== undefined) return;
				if (this.$el.hasClass("row_selected")) return;
				this.trigger('select', this);
				this.$el.addClass("row_selected");
			},
			
			/**
			 * Focus the item
			 * @fires focus
			 */
			focus: function() {
				this.trigger("focus", this);
				this.$el.addClass("row_selected");
			},
			
			/**
			 * Blur the item (cancel focus)
			 * @param {event} event Optional. Because blur will commit form data
			 *     if applicable, it may be helpful to pass on an event, if this
			 *     method is used as an event handler.
			 */
			blur: function(event) {
				//this.trigger("blur", this);
				this.$el.removeClass("row_selected");
				this.commitForm(event);
			},
			
			/**
			 * Called when the view's model changes
			 * 
			 * @param {Model} model The model that has changed
			 * @fires change
			 */
			onModelChange: function(model) {
				if (model.hasChanged())
					this.trigger("change", this);
			},
			
			/**
			 * <b>ABSTRACT</b> - Define a way to display validation errors for
			 * the view, for example in the case that it supports inline editing
			 * of fields.
			 *
			 * @param {map} errorMap A map from model attributes or form fields
			 *     to error messages
			 * @param {event} event Optional. The event that triggered the
			 *     failed validation.
			 */
			displayErrors: undefined,
			
			/**
			 * Commit the current form data, triggering validation.
			 *
			 * @param {event} event Optional. Triggering event.
			 * @returns {map} A map from field names to error messages, or
			 *     undefined if validation is successful
			 */
			commitForm: function(event) {
				var errors = this.form.commit();
				if (errors && this.displayErrors) this.displayErrors(errors, event);
				return errors;
			},
			
			/**
			 * Called when the remove action has been chosen for the item
			 *
			 * @param {event} event Optional. Triggering event.
			 */
			onRemove: function(event) {
				if (confirm(__("Are you sure you want to remove the selected item?"))) {
					this._removeItem(event);
					return true;
				}
				// Prevent this event from propagating
				else return false;
			},
			
			/**
			 * Render the list item
			 * 
			 * @returns {View} The rendered view
			 */
			render: function() {
				this.$el.html(this.template({
					model: this.model,
					actions: this.actions,
					fields: this.fields,
					GenericCollection: openhmis.GenericCollection
				})).addClass("selectable");
				if (_.indexOf(this.actions, 'inlineEdit') !== -1) {
					//this.form.render();
					this.$el.append(this.form.$('td'));
				}
				return this;
			},

			
			/**
			 * Remove this item
			 *
			 * @private
			 */
			_removeItem: function(event) {
				this._removeModel();
				Backbone.View.prototype.remove.call(this);
				this.trigger('remove', this.model);
				this.off();
			},
			
			/**
			 * Destroy the view's model
			 *
			 * @private
			 */
			_removeModel: function() {
				this.model.destroy();
			},
			
			/**
			 * Enable actions according to this.actions
			 *
			 * @private
			 */
			_enableActions: function() {
				for (var act in this.actions) {
					switch (this.actions[act]) {
						// Display remove action for the item
						case 'remove':
							this.events['click .remove'] = 'onRemove'
							break;
						case 'inlineEdit':
							var schema = _.extend({}, this.model.schema, this.schema || {});
							this.form = openhmis.GenericAddEditView.prototype.prepareModelForm.call(this, this.model, {
								schema: schema,
								template: 'trForm',
								fieldsetTemplate: 'blankFieldset',
								fieldTemplate: 'tableField'
							});
							this.form.on('blur', this.blur);
							this.form.on('focus', this.focus);
							break;
					}
				}
				this.delegateEvents();
			}
		});
		
		
		/**
		 * <b>Not a real class!</b>  Interface/pseudoclass for documentation
		 * purposes.
		 *
		 * @class FetchHelper
		 * @classdesc A FetchHelper is a view that can be used by a base view
		 *     (such as {@link GenericListView}) to affect the query sent to the
		 *     server when fetch() is called.  A FetchHelper view should
		 *     implement the getFetchOptions() method.<br>
		 *     <br>
		 *     <b>NOTE: This is not a real class.</b>  It's an interface and is
		 *     listed here for documentation purposes.  {@link NameSearchView}
		 *     and {@link DepartmentAndNameSearchView} are examples of
		 *     FetchHelpers.
		 */
		
		/**
		 * @function FetchHelper#getFetchOptions
		 * @param {map} options The current fetch options.  Should not be
		 *     modified.
		 * @returns {map} options The fetch options that the FetchHelper wants
		 *     to submit.
		 */
		
		
		/*======================================================================
		 *
		 * GenericSearchableListView
		 *
		 */
		openhmis.GenericSearchableListView = openhmis.GenericListView.extend(
		/** @lends GenericSearchableListView.prototype */
		{
			/**
			 * @class GenericSearchableListView
			 * @extends GenericListView
			 * @classdesc Specialized GenericListView that supports filtering
			 *     results by search criteria.
			 * @constructor GenericSearchableListView
			 * @param {map} options Optional.  View options.
			 *     GenericSearchableListView-specific options are: <ul>
			 *      <li><b>searchView:</b> A FetchHelper view to be used for setting the search filter</li>
			 * </ul>
			 */
			initialize: function(options) {
				_.bindAll(this);
				openhmis.GenericListView.prototype.initialize.call(this, options);
				this.searchViewType = options.searchView;
				this.searchView = new this.searchViewType({
					modelType: this.model.model
				});
				this.searchView.on("fetch", this.onSearch);
				this.fetchable.push(this.searchView);
			},
			
			/**
			 * Called when the search view fires a fetch event
			 *
			 * @param {map} options Fetch options
			 * @param {SearchView} sender The view that is triggering the search
			 */
			onSearch: function(options, sender) {
				if (this.paginateView) this.paginateView.setPage(1);
				this.fetch(options, sender);
			},
			
			/**
			 * Render the view.  Overrides GenericListView.render()
			 *
			 * @returns {View} The rendered view
			 */
			render: function() {
				if (this.searchView.lastSearch)
					this.options.listTitle = __('Results for "%s"', this.searchView.lastSearch);
				else
					this.options.listTitle = undefined;
				openhmis.GenericListView.prototype.render.call(this);
				this.$el.prepend(this.searchView.render().el);
				this.searchView.delegateEvents();
				this.searchView.focus();
				return this;
			}
		});
		
		// Create new generic add/edit screen
		openhmis.startAddEditScreen = function(model, options) {
			var collection = new openhmis.GenericCollection([], {
				url: model.prototype.meta.restUrl,
				model: model
			});
			var addEditView = options.addEditViewType
				? new options.addEditViewType({ collection: collection })
				: new openhmis.GenericAddEditView({ collection: collection });
			addEditView.setElement($('#add-edit-form'));
			addEditView.render();
			var viewOptions = _.extend({
				model: collection,
				addEditView: addEditView
			}, options);
			var listViewType = options.listView ? options.listView : openhmis.GenericListView;
			var listView = new listViewType(viewOptions);
			listView.setElement($('#existing-form'));
			listView.fetch();
		}
		
		Backbone.Form.setTemplates({
			trForm: '<b>{{fieldsets}}</b>',
			blankFieldset: '<b class="fieldset">{{fields}}</b>',
			tableField: '<td class="bbf-field field-{{key}}"><span class="editor">{{editor}}</span></td>'
		}, {
			errClass: "error"
		});
		
		return openhmis;
	}
);