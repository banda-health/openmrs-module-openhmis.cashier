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
				this.events = _.extend(this.events, {
					'click': 'determineChange'
				});
				editors.Number.prototype.initialize.call(this, options);
				if (options && options.schema) this.nonNegative = options.schema.nonNegative;
			},
			
			setValue: function(value) {
				this.el.defaultValue = value;
				editors.Number.prototype.setValue.call(this, value);
			},
			
			determineChange: function(event) {
				if (this.nonNegative && parseInt(this.$el.val()) < 0) {
					this.$el.val(0);
					return;
				}
				editors.Number.prototype.determineChange.call(this, event);
			}
			//valueChanged: function(event) {
			//		this.$el.val(this.el.defaultValue);
			//		return;
			//	}
			//	event.target.defaultValue = event.target.value;
			//}
		});
		
		editors.DepartmentSelect = editors.Select.extend({
		    getValue: function() {
				$selected = this.$('option:selected');
				return new openhmis.Department({ uuid: $selected.val(), name: $selected.text() });
			},
			
			setValue: function(value) {
				if (value === null)
					return;
				else if (_.isString(value))
					this.$el.val(value);
				else {
					if (value.attributes) this.$el.val(value.id); // Backbone model
					else this.$el.val(value.uuid); // bare object
				}
			},
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
			
			events: {
				'change select.department': 'modified',
				'change input.item-name' : 'modified',
				'focus select': 'handleFocus',
				'focus .item-name': 'handleFocus',
				'blur select': 'handleBlur',
				'blur .item-name': 'handleBlur',
				'keypress .item-name': 'onItemNameKeyPress'
			},
			
			getUuid: function() {
				return this.$('.item-uuid').val();
			},
			
			getValue: function() {
				return this.value;
			},
			
			handleFocus: function(event) {
				if (this.hasFocus) return;
				this.trigger("focus", this);
			},
			
			handleBlur: function(event) {
				if (!this.hasFocus) return;
				var self = this;
				setTimeout(function() {
					// Check if another input from this editor has come into focus
					if (self.$('select:focus')[0] || self.$('.item-name:focus')[0] || self.$('label:focus')[0])
						return;
					if (self.value) {
						self.$('.item-name').val(self.value.get("name"));
						self.$('.department').val(self.value.get("department").id);
						self.$('label.over-apply').hide();
					}
					self.trigger("blur", self);
				}, 0);
			},
			
			onItemNameKeyPress: function(event) {
				if (event.keyCode === 13) {	
					if (this.itemUpdating !== undefined)
						event.stopPropagation();
				}
			},
			
			modified: function(event) {
				// TODO: Some logic to handle messing with the form after
				//       successful validation
			},
			
			doItemSearch: function(request, response) {
				var term = request.term;
				var department_uuid = this.$('.department').val();
				var query = "?q=" + encodeURIComponent(term);
				if (department_uuid) query += "&department_uuid=" + encodeURIComponent(department_uuid);
				this.doSearch(request, response, openhmis.Item, query);
			},
			
			doSearch: function(request, response, model, query) {
				var term = request.term;
				if (query in this.cache) {
					response(this.cache[query]);
					return;
				}
				var resultCollection = new openhmis.GenericCollection([], { model: model });
				var view = this;
				var fetchQuery = query ? query : "?q=" + encodeURIComponent(term);
				resultCollection.fetch({
					url: resultCollection.url + fetchQuery,
					success: function(collection, resp) {
						var data = collection.map(function(model) { return {
							val: model.id,
							label: model.get('name'),
							codes: model.get('codes'),
							department_uuid: model.get('department').id
						}});
						view.cache[query] = data;
						response(data);
					}
				});
			},
			
			selectItem: function(event, ui) {
				this.itemUpdating = true;
				var uuid = ui.item.val;
				var name = ui.item.label;
				var departmentUuid = ui.item.department_uuid;
				this.$('.item-name').val(name);
				this.$('.item-uuid').val(uuid);
				this.$('.department').val(departmentUuid);
				this.value = new openhmis.Item({ uuid: uuid });
				var view = this;
				this.value.fetch({ success: function(model, resp) {
					view.trigger('change', view);
					delete view.itemUpdating;
				}});
			},
			
			departmentKeyDown: function(event) {
				if (event.keyCode === 8) {
					$(event.target).val('');
				}
			},
			
			render: function() {
				this.$el.html(this.template({
					departments: this.departmentMap,
					item: this.value
				}));
				this.$('select').keydown(this.departmentKeyDown);
				this.$('label').labelOver('over-apply');
				var self = this;
				this.$('.item-name').autocomplete({
					minLength: 2,
					source: this.doItemSearch,
					select: this.selectItem
				})
				// Tricky stuff here to get the autocomplete list to render with our custom data
				.data("autocomplete")._renderItem = function(ul, item) {
					return $("<li></li>").data("item.autocomplete", item)
						.append("<a>" + openhmis.ItemCode.prototype.listToString(item.codes) + ": " + item.label + "</a>").appendTo(ul);
				};
				return this;
			}
		});
	}
)
