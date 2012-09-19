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
		'view/editors'
	],
	function($, _, Backbone, __, openhmis) {
		openhmis.GenericAddEditView = Backbone.View.extend({
			tmplFile: 'generic.html',
			tmplSelector: '#add-edit-template',
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
				'click button.retireOrVoid': 'retireOrVoid',
				'click button.unretireOrUnvoid': 'unretireOrUnvoid',
				'click button.purge': 'purge'
			},
			
			prepareModelForm: function(model, options) {
				options = options ? options : {};
				var formFields = [];
				for (var key in model.schema) {
					if (key === 'retired') continue;
					//if (model.schema[key].readOnly === true) continue;
					formFields.push(key);
				}
				var formOptions = { model: model, fields: formFields };
				formOptions = _.extend(options, formOptions);
				var modelForm = new Backbone.Form(options)
				return modelForm;
			},
		
			beginAdd: function() {
				this.model = new this.collection.model(null, { urlRoot: this.collection.url });
				this.render();
				$(this.addLinkEl).hide();
				$(this.retireVoidPurgeEl).hide();
				$(this.titleEl).show();
				this.modelForm = this.prepareModelForm(this.model).render();
				$(this.formEl).prepend(this.modelForm.el);
				$(this.formEl).show();
				$(this.formEl).find('input')[0].focus();
			},
			
			cancel: function() {
				this.trigger('cancel');
				$(this.addLinkEl).show();
				$(this.titleEl).hide();
				$(this.formEl).hide();
				$(this.retireVoidPurgeEl).hide();
			},
			
			edit: function(model) {
				this.model = model;
				this.render();
				$(this.titleEl).show();
				this.modelForm = this.prepareModelForm(this.model).render();
				$(this.formEl).prepend(this.modelForm.el);
				$(this.formEl).show();
				$(this.retireVoidPurgeEl).show();
				$(this.formEl).find('input')[0].focus();
			},
			
			save: function() {
				this.modelForm.commit();
				var view = this;
				this.model.save(null, { success: function(model, resp) {
					model.trigger('sync', model, resp);
					if (model.collection === undefined) {
						view.collection.add(model);
						view.collection.trigger('reset');
					}
					view.cancel();
				}, error: function(model, resp) {
					openhmis.displayError(resp);
				}});
			},
			
			retireOrVoid: function() {
				var reason = this.$('#reason').val();
				var view = this;
				this.model.retire({
					reason: reason,
					success: function(model, resp) {
						model.trigger('sync', model, resp);
						view.cancel();
					}
				});
			},
			
			unretireOrUnvoid: function() {
				if (confirm("Are you sure you want to unretire this object? It will then be restored to the system")) {
					this.model.set('retired', false);
					var view = this;
					this.model.save([], { success: function(model, resp) {
						model.trigger('sync', model, resp);
						view.cancel();
					}});
				}
			},
			
			purge: function() {
				if (confirm(__("Are you sure you want to purge this object? It will be permanently removed from the system."))) {
					var view = this;
					this.model.purge({ success: function(model) {
						view.cancel();
					}});
				}
			},
		
			render: function() {
				this.$el.html(this.template({ model: this.model }));
				this.addLinkEl = this.$('a.addLink');
				this.titleEl = this.$('b.title');
				this.formEl = this.$('div.form');
				this.retireVoidPurgeEl = this.$('div.retireVoidPurge');
				return this;
			}
		});
		
		openhmis.GenericListView = Backbone.View.extend({
			tmplFile: 'generic.html',
			tmplSelector: '#generic-list',
			itemView: openhmis.GenericListItemView,
			itemActions: [],
			
			initialize: function(options) {
				_.bindAll(this);
				if (options !== undefined) {
					this.addEditView = options.addEditView;
					this.itemView = options.itemView ? options.itemView : openhmis.GenericListItemView
					if (options.itemActions) this.itemActions = options.itemActions;
					if (options.schema) this.schema = options.schema;
					this.template = this.getTemplate();
					this.includeFields = options.listFields;
					this.excludeFields = options.listExcludeFields;
				}
				if (this.addEditView !== undefined)
					this.addEditView.on('cancel', this.deselectAll);
				this.model.on('reset', this.render);
				this.showRetired = false;
				this._determineFields();
			},
			
			events: {
				'change #showRetired': 'toggleShowRetired'
			},
			
			deselectAll: function() {
				this.$('tr').removeClass('row_selected');
			},
			
			toggleShowRetired: function(event) {
				this.showRetired = event.target.checked;
				this.model.fetch();
			},
			
			_determineFields: function() {
				if (this.includeFields !== undefined)
					this.fields = this.includeFields;
				else
					this.fields = _.keys(this.model.model.prototype.schema);
				if (this.excludeFields !== undefined) {
					var argv = _.clone(this.excludeFields);
					argv.unshift(this.fields);
					this.fields = _.without.apply(this, argv);
				}
			},
			
			render: function(extraContext) {
				var schema = _.extend({}, this.model.model.prototype.schema, this.schema || {});
				var context = {
					list: this.model,
					fields: this.fields,
					modelMeta: this.model.model.prototype.meta,
					modelSchema: this.model.model.prototype.schema,
					showRetired: this.showRetired,
					listTitle: undefined, // custom title for list
					itemActions: this.itemActions
				}
				if (extraContext !== undefined) context = _.extend(context, extraContext);
				this.$el.html(this.template(context));
				var view = this;
				var tbody = this.$('tbody');
				var lineNumber = 0;
				this.model.each(function(model) {
					if (view.showRetired === false && model.isRetired()) return;
					var itemView = new openhmis.GenericListItemView({
						model: model,
						fields: view.fields,
						schema: schema,
						className: (lineNumber % 2 === 0) ? "evenRow" : "oddRow",
						actions: view.itemActions
					});
					tbody.append(itemView.render().el);
					itemView.on('select', view.deselectAll);
					if (view.addEditView) itemView.on('select', view.addEditView.edit);
					model.on('retired', function() { if (!view.showRetired) itemView.remove(); });
					lineNumber++;
				});
				return this;
			}
		});
		
		openhmis.GenericListItemView = Backbone.View.extend({
			tagName: "tr",
			tmplFile: "generic.html",
			tmplSelector: '#generic-list-item',
			actions: [], // see enableActions()
			initialize: function(options) {
				if (options !== undefined) {
					this.fields = options.fields ? options.fields : _.keys(this.model.schema);
					if (options.actions) this.actions = options.actions;
					if (options.schema) this.schema = options.schema;
				}
				_.bindAll(this);
				this.template = this.getTemplate();
				this.model.on('sync', this.render);
				this.model.on('destroy', this.remove);
				this.enableOperations();
			},
			
			events: {
				'click td': 'select'
			},
			
			enableOperations: function() {
				for (var act in this.actions) {
					switch (this.actions[act]) {
						// Display remove action for the item
						case 'remove':
							this.events['click .remove'] = 'itemRemove';
							break;
					}
				}
				this.delegateEvents();
			},
			
			select: function() {
				this.trigger('select', this.model);
				this.$el.addClass("row_selected");
			},
			
			itemRemove: function(event) {
				if (confirm(__("Are you sure you want to remove the selected item?"))) {
					this.model.destroy();
					Backbone.View.prototype.remove.call(this);
					this.trigger('remove');
					return true;
				}
				// Prevent this event from propagating
				else return false;
			},
			
			render: function() {
				this.$el.html(this.template({
					model: this.model,
					actions: this.actions,
					fields: this.fields
				})).addClass("selectable");

				if (_.indexOf(this.actions, 'inlineEdit') !== -1) {
					var schema = _.extend({}, this.model.schema, this.schema || {});
					this.form = openhmis.GenericAddEditView.prototype.prepareModelForm.call(this, this.model, {
						schema: schema,
						template: 'trForm',
						fieldsetTemplate: 'blankFieldset',
						fieldTemplate: 'tableField'
					});
					this.form.render();
					this.$el.append(this.form.$('td'));
					//this.setElement(this.form.el);
					//this.$el.html(this.$el.html() + this.form.$('b.fieldset').html());
				}
				return this;
			}
		});
		
		// Create new generic add/edit screen
		openhmis.startAddEditScreen = function(model, restResourceUrl, options) {
			var collection = new openhmis.GenericCollection([], {
				url: restResourceUrl,
				model: model
			});
			var addEditView = new openhmis.GenericAddEditView({ collection: collection });
			addEditView.setElement($('#add-edit-form'));
			addEditView.render();
			var viewOptions = _.extend({
				model: collection,
				addEditView: addEditView
			}, options);
			var listView = new openhmis.GenericListView(viewOptions);
			collection.on('reset', listView.render);
			listView.setElement($('#existing-form'));
			collection.fetch();
		}
		
		Backbone.Form.setTemplates({
			trForm: '<b>{{fieldsets}}</b>',
			blankFieldset: '<b class="fieldset">{{fields}}</b>',
			tableField: '<td class="bbf-field field-{{key}}">{{editor}}</td>'
		})
		
		return openhmis;
	}
);
