define(
	[
		'lib/jquery',
		'lib/underscore',
		'lib/backbone',
		'view/generic',
		'lib/i18n',
		'lib/backbone-forms',
		'view/editors',
		'model/bill',
		'model/cashPoint'
	],
	function($, _, Backbone, openhmis, i18n) {
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
				this.updatePriceOptions(item, form);
				if (form.fields.quantity.getValue() === 0)
					form.fields.quantity.setValue(1);
				this.update();
				this.$('.field-quantity input').focus();
			},
			
			updatePriceOptions: function(item, form) {
				item = item ? item : this.model.get("item");
				form = form ? form : this.form;
				var defaultPrice = item ? item.get("defaultPrice") : undefined;
				var price = this.model.get("price") || defaultPrice;
				if (price !== undefined)
					price = price.id;
				var options = new openhmis.GenericCollection([], { model: openhmis.ItemPrice });
				if (item)
					options.reset(item.get("prices"));
				else
					options.add(0);
				if (form) {
					form.fields.price.editor.options.options = options;
					form.fields.price.editor.render();
					if (price)
						form.fields.price.setValue(price);
				}
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
			
			// Maybe this should just be moved into generic.js			
			_removeModel: function() {
				if (this.model.collection)
					this.model.collection.remove(this.model, { silent: true });
			},
			
			render: function() {
				openhmis.GenericListItemView.prototype.render.call(this);
				this.updatePriceOptions();
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
				options = _.extend(billOptions, options);
				
				openhmis.GenericListView.prototype.initialize.call(this, options);
				this.options.roundToNearest = options.roundToNearest || 0;
				this.options.roundingMode = options.roundingMode || "MID";
				this.itemView = openhmis.BillLineItemView;
				this.totalsTemplate = this.getTemplate("bill.html", '#bill-totals');
			},
			
			schema: {
				item: { type: "Item" },
				quantity: { type: "CustomNumber" },
				price: {
					type: "ItemPriceSelect",
					options: new openhmis.GenericCollection([0], {model: openhmis.ItemPrice}),
					format: openhmis.ItemPrice.prototype.format
				}
			},
			
			setBill: function(bill) {
				this.bill = bill;
				this.model = bill.get("lineItems");
				this.model.on("all", this.updateTotals);
				if (bill.get("status") === bill.BillStatus.PENDING)
					this.options.itemActions = ["remove", "inlineEdit"];
				else
					this.options.itemActions = [];
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
			
			onItemSelected: function(itemView) {
				openhmis.GenericListView.prototype.onItemSelected.call(this, itemView);
				this.updateTotals();
			},

			onItemRemoved: function(item) {
				openhmis.GenericListView.prototype.onItemRemoved.call(this, item);
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
					this._deselectAll();
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
			
			setupCashPointForm: function(el) {
				var cashPoint = this.bill.get("cashPoint");
				var cashPointId = cashPoint ? cashPoint.id : undefined;
				this.cashPointForm = new Backbone.Form({
					schema: {
						cashPoint: {
							type: 'Select',
							title: i18n("Cash Point") + ":",
							options: new openhmis.GenericCollection([], { model: openhmis.CashPoint })
						}
					},
					data: {
						cashPoint: cashPointId
					},
					template: "trForm",
					fieldsetTemplate: "blankFieldset"
				});
				this.cashPointForm.render();
				$(el).empty().append(this.cashPointForm.el);
				return this.cashPointForm;
			},
			
			updateTotals: function() {
				var total = openhmis.round(this.bill.getAdjustedTotal(), this.options.roundToNearest, this.options.roundingMode);
				var totalPaid = this.bill.getTotalPayments();
				this.$totals.html(this.totalsTemplate({
					bill: this.bill,
					total: total,
					totalPaid: totalPaid,
					formatPrice: openhmis.ItemPrice.prototype.format,
					__: i18n }))
			},
			
			/**
			 * @should post bill if it is pending
			 * @should add payment if the bill has already been posted
			 */
			processPayment: function(payment, options) {
				options = options ? options : {};
				var success = options.success;
				var self = this;
				options.success = function(model, resp) {
					if (self.bill.getTotalPayments() >= self.bill.getTotal())
						self.trigger("paid", self.bill);
					self.updateTotals();
					if (success) success(model, resp);
				}
				payment.set("amountTendered", payment.get("amount"));
				var paymentChange = (this.bill.getTotalPayments() + payment.get("amount")) - this.bill.getAdjustedTotal();
				if (paymentChange > 0)
					payment.set("amount", payment.get("amountTendered") - paymentChange);
				this.bill.addPayment(payment);
				if (this.bill.get("status") === this.bill.BillStatus.PENDING) {
					if (!this.postBill(options));
						this.bill.get("payments").remove(payment);
				}
				else
					payment.save([], options);
			},
			
			validate: function(allowEmptyBill) {
				var errors = this.bill.validate(true);
				var elMap = {
					'lineItems': [ $('#bill'), this ],
					'patient': [ $('#patient-view'),  $('#inputNode') ]
				}
				if (allowEmptyBill === true
					&& errors
					&& errors.lineItems !== undefined)
						delete errors.lineItems;
				if (errors && _.size(errors) > 0) {
					for (var e in errors)
						openhmis.validationMessage(elMap[e][0], errors[e], elMap[e][1]);
					return false;
				}
				return true;
			},
			
			saveBill: function(options, post) {
				// Set up options, ignoring events
				options = options !== undefined && options.srcElement === undefined ? options : {};
				// If the bill is an adjustment, we will allow posting with zero
				// line items
				var billAdjusted = this.bill.get("billAdjusted");
				var allowEmptyBill = (billAdjusted !== undefined && billAdjusted.id !== undefined);
				if (!this.validate(allowEmptyBill)) return false;
				if (this.cashPointForm !== undefined)
					this.bill.set("cashPoint", this.cashPointForm.getValue("cashPoint"));
				// Filter out any invalid lineItems (especially the bottom)
				// entry cursor
				this.bill.get("lineItems").reset(
					this.model.filter(function(item) { return item.isClean(); }),
					{ silent: true }
				);

				if (post === true
					&& this.bill.get("billAdjusted")
					&& this.bill.get("status") === this.bill.BillStatus.PENDING)
						this._postAdjustingBill(this.bill);
				else if (post === true)
					this.bill.set("status", this.bill.BillStatus.POSTED);
				var print = options.print;
				var success = options.success;
				var error = options.error;
				var self = this;
				options.success = function(model, resp) {
					self.trigger(print ? "saveAndPrint" : "save", model);
					if (success) success(model, resp);
				}
				options.error = function(model, resp) {
					openhmis.error(resp);
					if (error) error(model, resp);
				}
				this.bill.save([], options);
				return true;
			},
			
			postBill: function(options) {
				return this.saveBill(options, true);
			},
			
			_postAdjustingBill: function(bill) {
				bill.get("billAdjusted").get("payments").each(function(payment) {
					payment.set("amountTendered", payment.get("amount"));
				});
				bill.get("payments").add(bill.get("billAdjusted").get("payments").models);
				var adjustingItems = bill.get("lineItems");
				bill.set("lineItems", bill.get("billAdjusted").get("lineItems"));
				bill.get("lineItems").add(adjustingItems.models);
				bill.set("status", bill.BillStatus.POSTED);
			},
			
			adjustBill: function() {
				var __ = i18n;
				if (confirm(__("Are you sure you want to adjust this bill?"))) {
					var adjustingBill = new openhmis.Bill({
						billAdjusted: this.bill.id,
						patient: this.bill.get("patient").id
					});
					// Unset status to avoid the adjusted bill from being
					// immediately set to PAID
					adjustingBill.unset("status");
					var view = this;
					adjustingBill.save([], {
						success: function(model, resp) {
							view.trigger("adjusted", model);
						}, error: openhmis.error
					});
				}
			},
			
			printReceipt: function(event) {
				var url = openhmis.config.pageUrlRoot
					+ "receipt.form?receiptNumber=" + encodeURIComponent(this.bill.get("receiptNumber"));
				// Triggered by an event
				//if (event) {}
				// Print on page load (Post & Print)
				//else {
				// Remove if print has been clicked before?
				$("#receiptDownload").remove();
				$iframe = $('<iframe id="receiptDownload" src="'+url+'" width="1" height="1"></iframe>');
				$iframe.load(function() { $(this).get(0).contentWindow.print(); });
				$("body").append($iframe);
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
		
		openhmis.BillAndPaymentsView = Backbone.View.extend({
			className: "combineBoxes",
			initialize: function(options) {
				var __ = i18n;
				this.itemsView = new openhmis.GenericListView({
					model: this.model.get("lineItems"),
					listTitle: __("Previous Bill (%s)", this.model),
					showPaging: false,
					showRetiredOption: false
				});
				this.paymentsView = new openhmis.GenericListView({
					model: this.model.get("payments"),
					className: "paymentList",
					listTitle: "Previous Payments",
					listFields: ["dateCreatedFmt", "paymentMode", "amountFmt"],
					showPaging: false,
					showRetiredOption: false
				});
			},
			
			render: function() {
				this.$el.append(this.itemsView.render().el);
				this.itemsView.$("table").addClass("bill");
				this.$el.append(this.paymentsView.render().el);
				return this;
			}
		});
		
		return openhmis;
	}	
);