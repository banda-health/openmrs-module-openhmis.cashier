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
				if (this.form) {
					this.form.on('price:change', this.update);
					this.form.on('quantity:change', this.update);
					this.form.on('item:change', this.updateItem);
				}
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
				}
				this.updateTimeout = setTimeout(update, 200);
			},
			
			onKeyPress: function(event) {
				if (event.keyCode === 13) {
					var errors = this.commitForm(event);
					if (!errors) this.trigger("change", this);
				}
			},
			
			displayErrors: function(errorMap, event) {
				// If there is already another item in the collection and
				// this is not triggered by enter key, skip the error message
				if (event && event.type !== "keypress" && this.model.collection.length > 1)
					return;
				
				for(var item in errorMap) {
					var $errorEl = this.$('.field-' + item + ' .editor');
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
				_.bindAll(this);
				var bill = (options && options.bill) ? options.bill : new openhmis.Bill();
				this.setBill(bill);
				
				var billOptions = { showRetiredOption: false }
				if (bill.get("status") === bill.BillStatus.PENDING)
					billOptions["itemActions"] = ["remove", "inlineEdit"];
				options = _.extend(billOptions, options);
				
				openhmis.GenericListView.prototype.initialize.call(this, options);
				this.itemView = openhmis.BillLineItemView;
				this.totalsTemplate = this.getTemplate("bill.html", '#bill-totals');
			},
			
			schema: {
				item: { type: "Item" },
				quantity: { type: "CustomNumber", minimum: 1 },
				price: { type: "BasicNumber", readOnly: true }
			},
			
			setBill: function(bill) {
				this.bill = bill;
				this.model = bill.get("lineItems");
				this.model.on("all", this.updateTotals);
			},
			
			addOne: function(model, schema) {
				var view = openhmis.GenericListView.prototype.addOne.call(this, model, schema);
				view.$('td.field-quantity').add('td.field-price').add('td.field-total').addClass("numeric");
				if (this.newItem && view.model.cid === this.newItem.cid)
					this.selectedItem = view;
				else
					view.on("change remove", this.bill.setUnsaved);
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
			
			setupNewItem: function(lineItemView) {
				var dept_uuid;
				if (lineItemView !== undefined) {
					lineItemView.off("change", this.setupNewItem);
					lineItemView.on("change remove", this.bill.setUnsaved);
					this.deselectAll();
					dept_uuid = lineItemView.model.get("item").get("department").id;
				}
				this.newItem = new openhmis.LineItem();
				this.model.add(this.newItem, { silent: true });
				if (this.$('p.empty').length > 0)
					this.render();
				else {
					var view = this.addOne(this.newItem);
					view.on("change", this.setupNewItem);
					view.focus();
				}
			},
			
			updateTotals: function() {
				this.$totals.html(this.totalsTemplate({ bill: this.bill, __: i18n }))
			},
			
			processPayment: function(payment, options) {
				options = options ? options : {};
				var success = options.success;
				var self = this;
				options.success = function(model, resp) {
					if (self.bill.getTotalPaid() >= self.bill.getTotal())
						self.trigger("paid", self.bill);
					self.updateTotals();
					if (success) success(model, resp);
				}
				this.bill.addPayment(payment);
				if (this.bill.isNew() || this.bill.isUnsaved())
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
				// Filter out any invalid lineItems (especially the bottom)
				// entry cursor
				this.bill.get("lineItems").reset(
					this.model.filter(function(item) { return item.isClean(); })
				);
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