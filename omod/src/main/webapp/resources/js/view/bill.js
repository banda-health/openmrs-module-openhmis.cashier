define(
	[
		'lib/jquery',
		'lib/underscore',
		'view/generic',
		'lib/i18n',
		'view/editors',
		'model/bill'
	],
	function($, _, openhmis, i18n) {
		openhmis.BillLineItemView = openhmis.GenericListItemView.extend({
			initialize: function(options) {
				this.events = _.extend({}, this.events, {
					'keypress': 'onKeyPress'
				});
				openhmis.GenericListItemView.prototype.initialize.call(this, options);
				_.bindAll(this);
				this.form.on('price:change', this.update);
				this.form.on('quantity:change', this.update);
				this.form.on('item:change', this.updateItem);
				this.form.validate = function() { return null; }
			},
			
			updateItem: function(form, itemEditor) {
				var item = itemEditor.getValue();
				form.fields.price.setValue(item.get('defaultPrice').price);
				if (form.fields.quantity.getValue() === 0)
					form.fields.quantity.setValue(1);
				this.update();
				this.$('.field-quantity input').focus();
			},
			
			update: function() {
				if (this.updateTimeout !== undefined) clearTimeout(this.updateTimeout);
				var view = this;
				var update = function() {
					var price = view.form.getValue("price");
					var quantity = view.form.getValue("quantity");
					view.form.setValue({ total: price * quantity });
					view.trigger("change", view);
				}
				this.updateTimeout = setTimeout(update, 200);
			},
			
			onKeyPress: function(event) {
				if (event.charCode === 13) {
					this.commitForm(event);
				}
			},
			
			displayErrors: function(errorMap, event) {
				// If there is already another item in the collection and
				// this is not triggered by enter key, skip the error message
				if (event && event.type !== "keypress" && this.model.collection.length > 1)
					return;
				
				for(var item in errorMap) {
					var $errorEl = this.$('.field-' + item);
					if ($errorEl.length > 0) {
						openhmis.validationMessage($errorEl, errorMap[item]);
					}
				}
			},
			
			focus: function(form) {
				openhmis.GenericListItemView.prototype.focus.call(this, form);
				if (!form)
					this.$('.item-name').focus();
			},
						
			removeModel: function() {
				this.model.collection.remove(this.model);
			},
			
			render: function() {
				openhmis.GenericListItemView.prototype.render.call(this);
				this.$(".field-price input, .field-total input").attr("readonly", "readonly");
				return this;
			},
		});

		openhmis.BillView = openhmis.GenericListView.extend({
			initialize: function(options) {
				options = _.extend(this.options, options);
				openhmis.GenericListView.prototype.initialize.call(this, options);
				this.bill = new openhmis.Bill();
				this.itemView = openhmis.BillLineItemView;
				this.totalsTemplate = this.getTemplate("bill.html", '#bill-totals');
				this.model.on("all", this.updateTotals);
			},
			
			options: {
				itemActions: ["remove", "inlineEdit"],
				showRetiredOption: false
			},
			
			schema: {
				item: { type: "Item" },
				quantity: { type: "CustomNumber", nonNegative: true },
				price: { type: "BasicNumber", readOnly: true }
			},
			
			addOne: function(model, schema) {
				var view = openhmis.GenericListView.prototype.addOne.call(this, model, schema);
				view.$('td.field-quantity').add('td.field-price').add('td.field-total').addClass("numeric");
				if (this.newItem && view.model.cid === this.newItem.cid)
					this.selectedItem = view;
				return view;
			},
			
			itemSelected: function(itemView) {
				openhmis.GenericListView.prototype.itemSelected.call(this, itemView);
				this.updateTotals();
			},

			itemRemoved: function(item) {
				openhmis.GenericListView.prototype.itemRemoved.call(this, item);
				if (item === this.newItem) {
					this.setupNewItem();
				}
			},
			
			patientSelected: function(patient) {
				this.bill.set("patient", patient);
				this.focus();
			},
			
			setupNewItem: function(lineItem) {
				var dept_uuid;
				if (lineItem !== undefined) {
					lineItem.off("change", this.setupNewItem);
					this.deselectAll();
					dept_uuid = lineItem.get("item").get("department").id;
				}
				this.newItem = new openhmis.LineItem();
				this.newItem.on("change", this.setupNewItem);
				this.model.add(this.newItem, { silent: true });
				if (this.$('p.empty').length > 0)
					this.render();
				else {
					var view = this.addOne(this.newItem);
					view.focus();
				}
			},
			
			getTotal: function() {
				var total = 0;
				this.model.each(function(item) {
					if (item.isClean()) total += item.getTotal();
				});
				return total;
			},
			
			getTotalPaid: function() {
				var total = 0;
				var payments = this.bill.get("payments");
				if (payments && payments.length > 0) {
					payments.each(function(payment) {
						if (payment.get("voided") !== true)
							total += payment.get("amount");
					});
				}
				return total;
			},
			
			updateTotals: function() {
				this.$totals.html(this.totalsTemplate({ bill: this, __: i18n }))
			},
			
			processPayment: function(payment, options) {
				options = options ? options : {};
				var success = options.success;
				var self = this;
				options.success = function(model, resp) {
					self.updateTotals();
					if (success) success(model, resp);
				}
				this.bill.addPayment(payment);
				if (this.bill.isNew())
					this.saveBill(options);
				else
					payment.save([], options);
			},
			
			validate: function(final) {
				var errors = this.bill.validate(true);
				var elMap = {
					'lineItems': [ $('#bill'), this ],
					'patient': [ $('#patient-view'),  $('#inputNode') ]
				}
				if (errors) {
					for (var e in errors)
						openhmis.validationMessage(elMap[e][0], errors[e], elMap[e][1]);
					return false;
				}
				return true;
			},
			
			saveBill: function(options) {
				options = options ? options : {};
				this.bill.set("lineItems", this.model.filter(function(item) { return item.isClean(); }));
				if (!this.validate()) return;
				var success = options.success;
				var error = options.error;
				var self = this;
				options.success = function(model, resp) {
					self.trigger("save", model);
					if (success) success(model, resp);
				}
				options.error = function(model, resp) {
					openhmis.error(resp);
					if (error) error(model, resp);
				}
				this.bill.save([], options);
			},
			
			render: function() {
				openhmis.GenericListView.prototype.render.call(this, {
					listTitle: ""
				});
				this.$('table').addClass("bill");
				this.$totals = $('<table class="totals"></table>');
				this.$('div.box').append(this.$totals);
				this.updateTotals();
				return this;
			}
		});
		
		return openhmis;
	}	
);