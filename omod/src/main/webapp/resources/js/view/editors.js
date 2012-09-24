define(
	[
		'lib/jquery',
		'lib/backbone',
		'lib/underscore',
		'model/item',
		'model/department',
		'lib/backbone-forms',
		'lib/labelOver',
	],
	function($, Backbone, _, openhmis) {
		var editors = Backbone.Form.editors;
		editors.BasicNumber = editors.Number.extend({
			initialize: function(options) {
				this.defaultValue = null;
				editors.Text.prototype.initialize.call(this, options);
			},
			
			/**
			* Check value is numeric
			*/
			onKeyPress: function(event) {
				var self = this,
					delayedDetermineChange = function() {
					  setTimeout(function() {
						self.determineChange();
					  }, 0);
					}
					
				//Allow backspace && enter
				if (event.charCode == 0 || event.charCode == 13) {
				  delayedDetermineChange();
				  return;
				}
				
				//Get the whole new value so that we can prevent things like double decimals points etc.
				var newVal = this.$el.val() + String.fromCharCode(event.charCode);
		  
				var numeric = /^[0-9]*\.?[0-9]*?$/.test(newVal);
		  
				if (numeric) {
				  delayedDetermineChange();
				}
				else {
				  event.preventDefault();
				}
		   },
			
		});
		
		editors.CustomNumber = editors.Number.extend({
			initialize: function(options) {
				editors.Number.prototype.initialize.call(this, options);
				if (options && options.schema) this.nonNegative = options.schema.nonNegative;
			},
			
			events: {
				'change': 'valueChanged'
			},
			
			setValue: function(value) {
				this.el.defaultValue = value;
				editors.Number.prototype.setValue.call(this, value);
			},
			
			valueChanged: function(event) {
				if (this.nonNegative && parseInt(this.$el.val()) < 0) {
					this.$el.val(this.el.defaultValue);
					event.preventDefault();
					return;
				}
				event.target.defaultValue = event.target.value;
			}
		});
		
		editors.Item = editors.Base.extend({
			tagName: "span",
			className: "editor",
			tmplFile: 'editors.html',
			tmplSelector: '#item-editor',
			departmentMap: function() {
				var collection = new openhmis.GenericCollection([], { model: openhmis.Department });
				var map = {};
				collection.fetch({ success: function(collection) {
					collection.each(function(item) { map[item.id] = item.get('name') });
				}});
				return map;
			}(),
			initialize: function(options) {
				_.bindAll(this);
				editors.Base.prototype.initialize.call(this, options);
				this.template = this.getTemplate();
				this.cache = {};
			},
			doItemSearch: function(request, response) {
				this.doSearch(request, response, openhmis.Item);
			},			
			doSearch: function(request, response, model) {
				var term = request.term;
				if (term in this.cache) {
					response(this.cache[term]);
					return;
				}
				var resultCollection = new openhmis.GenericCollection([], { model: model });
				var view = this;
				resultCollection.fetch({
					url: resultCollection.url + "?q=" + encodeURIComponent(term),
					success: function(collection, resp) {
						var data = collection.map(function(model) { return model.get('name') });
						view.cache[term] = data;
						response(data);
					}
				});
			},
			render: function() {
				this.$el.html(this.template({
					departments: this.departmentMap,
					item: this.value
				}));
				this.$('label').labelOver('over-apply');
				
				this.$('.item-name').autocomplete({
					minLength: 2,
					source: this.doItemSearch
				});
				return this;
			}
		});
	}
)
