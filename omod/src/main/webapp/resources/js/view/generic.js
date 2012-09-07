openhmis.GenericCollection = Backbone.Collection.extend({
	baseUrl: '/openmrs/ws/rest',
	
	initialize: function(models, options) {
		this.url = this.baseUrl + options.url;
	},
	
	fetch: function() {
		Backbone.Collection.prototype.fetch.call(this, {
			error: function(model, data) {
				var o = $.parseJSON(data.response).error;
				alert("Message: " + o.message + "\n" + "Code: " + o.code + "\n" + "Detail: " + o.detail);
			}
		});
	},
	
	parse: function(response) {
		return response.results;
	},
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
		this.model = new this.collection.model();
		this.model.collection = this.collection;
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
		this.model.save([], { success: function(model, resp) {
			model.trigger('sync', model, resp);
			view.cancel();
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
		}
		if (this.addEditView !== undefined)
			this.addEditView.on('cancel', this.deselectAll);
		this.showRetired = false;
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
	
	render: function() {
		this.$el.html(this.template({
			list: this.model,
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
	initialize: function() {
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
		this.$el.html(this.template({ model: this.model })).addClass("selectable");
		return this;
	}
});