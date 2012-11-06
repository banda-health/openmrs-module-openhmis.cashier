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
				}
			},
			
			onModelChange: function(model) {
				if (model.hasChanged() && model.isValid())
					this.trigger("change", this);
			},
			
			displayErrors: function(errorMap, event) {
				// If there is already another item in the collection and
				// this is not triggered by enter key, skip the error message
				if (event && event.type !== "keypress" && this.model.collection && this.model.collection.length > 0)
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
				if (this.model.collection)
					this.model.collection.remove(this.model, { silent: true });
			},
			
			render: function() {
				openhmis.GenericListItemView.prototype.render.call(this);
				this.$(".field-price input, .field-total input").attr("readonly", "readonly");
				return this;
			},
		});

		/**
		 * BillView
		 * 
		 */
		openhmis.BillView = openhmis.GenericListView.extend({
			initialize: function(options) {
				_.bindAll(this);
				var bill = (options && options.bill) ? options.bill : new openhmis.Bill();
				this.setBill(bill);
				
				var billOptions = { showRetiredOption: false, showPaging: false }
				if (bill.get("status") === bill.BillStatus.PENDING)
					billOptions["itemActions"] = ["remove", "inlineEdit"];
				options = _.extend(billOptions, options);
				
				openhmis.GenericListView.prototype.initialize.call(this, options);
				this.itemView = openhmis.BillLineItemView;
				this.totalsTemplate = this.getTemplate("bill.html", '#bill-totals');
			},
			
			schema: {
				item: { type: "Item" },
				quantity: { type: "CustomNumber" },
				price: { type: "BasicNumber", readOnly: true, format: openhmis.ItemPrice.prototype.format }
			},
			
			setBill: function(bill) {
				this.bill = bill;
				this.model = bill.get("lineItems");
				this.model.on("all", this.updateTotals);
			},
			
			addOne: function(model, schema) {
				var view = openhmis.GenericListView.prototype.addOne.call(this, model, schema);
				view.$('td.field-quantity').add('td.field-price').add('td.field-total').addClass("numeric");
				if (this.newItem && view.model.cid === this.newItem.cid) {
					this.selectedItem = view;
					view.on("change", this.setupNewItem);
				}
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
				// Handle adding an item from the input line
				if (lineItemView !== undefined) {
					// Prevent multiple change events causing duplicate views
					if (this.model.getByCid(lineItemView.model.cid)) return;
					lineItemView.off("change", this.setupNewItem);
					this.model.add(lineItemView.model, { silent: true });
					this.bill.setUnsaved();
					lineItemView.on("change remove", this.bill.setUnsaved);
					this.deselectAll();
					dept_uuid = lineItemView.model.get("item").get("department").id;
				}
				this.newItem = new openhmis.LineItem();
				// Don't add the item to the collection, but give it a reference
				this.newItem.collection = this.model;
				if (this.$('p.empty').length > 0)
					this.render();
				else {
					var view = this.addOne(this.newItem);
					view.focus();
				}
			},
			
			updateTotals: function() {
				this.$totals.html(this.totalsTemplate({
					bill: this.bill,
					formatPrice: openhmis.ItemPrice.prototype.format,
					__: i18n }))
			},
			
			/**
			 *
			 *
			 * @should save bill if it is new
			 * @should add payment if the bill has already been saved
			 */
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
				if (!this.validate()) return;
				// Filter out any invalid lineItems (especially the bottom)
				// entry cursor
				this.bill.get("lineItems").reset(
					this.model.filter(function(item) { return item.isClean(); }),
					{ silent: true }
				);
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
			
			adjustBill: function() {
				var __ = i18n;
				if (confirm(__("Are you sure you want to adjust this bill?"))) {
					var adjustedUuid = this.bill.id;
					this.bill.unset("uuid");
					delete this.bill.id;
					var view = this;
					// Unset status to avoid the adjusted bill from being
					// immediately set to PAID
					this.bill.unset("status");
					this.bill.set("billAdjusted", new openhmis.Bill({ uuid: adjustedUuid }));
					this.bill.save([], { success: function(model, resp) {
						view.trigger("adjusted", model);
					}});
				}
			},
			
			printReceipt: function() {
				if (this.bill.get("receiptNumber")) {
					var self = this;
					var url = openhmis.config.pageUrlRoot + "receipt.form?receiptNumber=" + encodeURIComponent(self.bill.get("receiptNumber"));
					window.open(url, "pdfDownload");
				}
			},
			
			render: function() {
				if (this.newItem) this.model.add(this.newItem, { silent: true });
				openhmis.GenericListView.prototype.render.call(this, {
					listTitle: ""
				});
				if (this.newItem) this.model.remove(this.newItem, { silent: true });
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