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
		'click .submit': 'save'
	},

	beginAdd: function() {
		this.model = new this.collection.model();
		this.model.collection = this.collection;
		this.render();
		$(this.addLinkEl).hide();
		$(this.titleEl).show();
		this.modelForm = new Backbone.Form({ model: this.model });
		this.modelForm.render();
		$(this.formEl).prepend(this.modelForm.el);
		$(this.formEl).show();
	},
	
	cancel: function() {
		this.trigger('cancel');
		$(this.addLinkEl).show();
		$(this.titleEl).hide();
		$(this.formEl).hide();
	},
	
	edit: function(model) {
		this.model = model;
		this.render();
		$(this.titleEl).show();
		this.modelForm = new Backbone.Form({ model: this.model });
		this.modelForm.render();
		$(this.formEl).prepend(this.modelForm.el);
		$(this.formEl).show();	
	},
	
	save: function() {
		this.modelForm.commit();
		var view = this;
		this.model.save([], { success: function(model) {
			model.trigger('sync');
			view.cancel();
		}});
	},

	render: function() {
		this.$el.html(this.template({ model: this.model }));
		this.addLinkEl = this.$('a.addLink');
		this.titleEl = this.$('b.title');
		this.formEl = this.$('div.form');
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
	},
	
	deselectAll: function() {
		this.$('tr').removeClass('row_selected');
	},
	
	render: function() {
		this.$el.html(this.template({
			list: this.model,
			modelMeta: this.model.model.prototype.meta,
			modelSchema: this.model.model.prototype.schema
		}));
		var view = this;
		var tbody = this.$('tbody');
		var lineNumber = 0;
		this.model.each(function(model) {
			var itemView = new openhmis.GenericListItemView({
				model: model,
				className: (lineNumber % 2 === 0) ? "evenRow" : "oddRow"
			});
			tbody.append(itemView.render().el);
			itemView.on('select', view.deselectAll);
			itemView.on('select', view.addEditView.edit);
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