openhmis.GenericModel = Backbone.Model.extend({
	initialize: function(attributes, options) {
		if (options !== undefined) {
			this.urlRoot = options.urlRoot;
		}
		//_.bindAll(this, 'saveSubResource');
	},
		
	toJSON: function(options) {
		if (this.schema === undefined) return Backbone.Model.prototype.toJSON.call(this, options);
		var attributes = {};
		for (var attr in this.attributes) {
			// This gets added to representations but cannot be set
			if (attr === 'resourceVersion') continue;
			
			if (this.schema[attr] !== undefined
				&& (this.schema[attr].readOnly === undefined
					|| this.schema[attr].readOnly === false))
				attributes[attr] = this.attributes[attr];
		}
		return _.clone(attributes);
	},
	
	listToString: function(field) {
		var collection = new openhmis.GenericCollection(this.get(field), {
			model: this.schema[field].model
		});
		var str = "";
		collection.each(function(model) {
			str += str === "" ? model.toString() : ", " + model.toString();
		});
		return str;
	}
});

openhmis.GenericCollection = Backbone.Collection.extend({
	baseUrl: '/openmrs/ws/rest',
	
	initialize: function(models, options) {
		this.url = this.baseUrl + options.url;
	},
	
	fetch: function(options) {
		options = options ? options : {};
		var error = options.error;
		options.error = function(model, data) {
			openhmis.displayError(data);
			if (error !== undefined)
				error(model, data);
		}
		Backbone.Collection.prototype.fetch.call(this, options)
	},
	
	parse: function(response) {
		var results = response.results;
		for (var result in results) {
			if (this.model.prototype.parse !== undefined)
				results[result] = this.model.prototype.parse.call(this, results[result]);
		}
		return results;
	}
});

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
	
	renderModelForm: function() {
		var formFields = [];
		for (var key in this.model.schema) {
			if (key === 'retired') continue;
			if (this.model.schema[key].readOnly === true) continue;
			formFields.push(key);
		}
		this.modelForm = new Backbone.Form({
			model: this.model,
			fields: formFields
		});
		this.modelForm.render();		
	},

	beginAdd: function() {
		this.model = new this.collection.model(null, { urlRoot: this.collection.url });
		this.render();
		$(this.addLinkEl).hide();
		$(this.retireVoidPurgeEl).hide();
		$(this.titleEl).show();
		this.renderModelForm();
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
		this.renderModelForm();
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
	
	initialize: function(options) {
		_.bindAll(this);
		if (options !== undefined) {
			this.addEditView = options.addEditView;
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
	
	render: function() {
		this.$el.html(this.template({
			list: this.model,
			fields: this.fields,
			modelMeta: this.model.model.prototype.meta,
			modelSchema: this.model.model.prototype.schema,
			showRetired: this.showRetired
		}));
		var view = this;
		var tbody = this.$('tbody');
		var lineNumber = 0;
		this.model.each(function(model) {
			if (view.showRetired === false && model.isRetired()) return;
			var itemView = new openhmis.GenericListItemView({
				model: model,
				fields: view.fields,
				className: (lineNumber % 2 === 0) ? "evenRow" : "oddRow"
			});
			tbody.append(itemView.render().el);
			itemView.on('select', view.deselectAll);
			itemView.on('select', view.addEditView.edit);
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
	initialize: function(options) {
		if (options !== undefined)
			this.fields = options.fields ? options.fields : _.keys(this.model.schema);
		_.bindAll(this);
		this.template = this.getTemplate();
		this.model.on('sync', this.render);
		this.model.on('destroy', this.remove);
	},
	
	events: {
		'click td': 'select'
	},
	
	select: function() {
		this.trigger('select', this.model);
		this.$el.addClass("row_selected");
	},
	
	render: function() {
		this.$el.html(this.template({ model: this.model, fields: this.fields })).addClass("selectable");
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