/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
 * the License for the specific language governing rights and
 * limitations under the License.
 *
 * Copyright (C) OpenHMIS.  All Rights Reserved.
 */
define(
    [
        openhmis.url.backboneBase + 'js/lib/underscore',
        openhmis.url.backboneBase + 'js/lib/backbone',
        openhmis.url.backboneBase + 'js/view/generic',
        openhmis.url.backboneBase + 'js/lib/i18n',
        openhmis.url.backboneBase + 'js/lib/backbone-forms',
        openhmis.url.backboneBase + 'js/view/editors',
        openhmis.url.cashierBase + 'js/view/editors',
        openhmis.url.inventoryBase + 'js/view/editors',
        openhmis.url.cashierBase + 'js/model/bill',
        openhmis.url.cashierBase + 'js/model/cashPoint',
        'link!' + openhmis.url.cashierBase + 'css/style.css'
    ],
    function(_, Backbone, openhmis, i18n) {
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
                if (form.fields.quantity.getValue() === 0) {
                    form.fields.quantity.setValue(1);
                }
                this.update();
                form.fields.quantity.editor.focus(true);
            },

            updatePriceOptions: function(item, form) {
                item = item ? item : this.model.get("item");
                form = form ? form : this.form;
                var defaultPrice = item ? item.get("defaultPrice") : undefined;
                var price = this.model.get("price") || defaultPrice;
                if (price !== undefined) {
                    price = price.id;
                }
                var options = new openhmis.GenericCollection([], { model: openhmis.ItemPrice });
                if (item) {
                    options.reset(item.get("prices"));
                } else {
                    options.add(0);
                }
                if (form) {
                    form.fields.price.editor.options.options = options;
                    form.fields.price.editor.render();
                    if (price) {
                        form.fields.price.setValue(price);
                    }
                    if (this.model.attributes.price) {
                        var self = this;
                        options.each(function(model) {
                            if (model.get('price') === self.model.attributes.price) {
                                self.$('select[name=price]').val(model.get('uuid'));
                            }
                        });
                    }
                }
            },

            update: function() {
                if (this.updateTimeout !== undefined) {
                    clearTimeout(this.updateTimeout);
                    this.form.model.set(this.form.getValue());
                }
                var view = this;
                var update = function() {
                    var price = view.form.getValue("price");
                    var quantity = view.form.getValue("quantity");
                    view.form.setValue({ total: price * quantity });
                }
                this.updateTimeout = setTimeout(update, 200);
                this.form.model.set(this.form.getValue());
                this.trigger("select", this);
                this.$el.addClass("row_selected");
            },

            onKeyPress: function(event) {
                //this is for firefox as arrows are detected as keypress events
                if ($('input[name=quantity]').is(':focus')) {
                    var view = this;
                    if (event.keyCode === 38 /*arrow up*/) {
                        var quantity = view.form.getValue("quantity");
                        view.form.setValue({ quantity: quantity + 1 });
                        this.update;
                    }
                    if (event.keyCode === 40 /*arrow down*/) {
                        var quantity = view.form.getValue("quantity");
                        view.form.setValue({ quantity: quantity - 1 });
                        this.update;
                    }
                }

                if (event.keyCode === 13 /* Enter */) {
                    this.update();
                    this.commitForm(event);
                    // Prevent enter press from interfering with HTML form controls
                    event.preventDefault();
                }
            },

            commitForm: function(event) {
                var errors = openhmis.GenericListItemView.prototype.commitForm.call(this, event);
                if (errors === undefined && event && event.keyCode === 13) {
                    this.trigger("focusNext", this);
                }
            },

            onModelChange: function(model) {
                if (model.hasChanged() && model.isValid()) {
                    this.trigger("change", this);
                }
            },

            displayErrors: function(errorMap, event) {
                // If there is already another item in the collection and
                // this is not triggered by enter key, skip the error message
                if (event && event.type !== "keypress" && this.model.collection && this.model.collection.length > 0) {
                    return;
                }
                // If there is already an item in the collection and the event
                // was triggered by the enter key, request that focus be moved
                // to the next form item.
                else if (event && event.type === "keypress" && event.keyCode === 13
                    && this.model.collection && this.model.collection.length > 1) {
                    this.trigger("focusNext", this);
                    return;
                }
                openhmis.GenericListItemView.prototype.displayErrors.call(this, errorMap, event);
            },

            focus: function(form) {
                openhmis.GenericListItemView.prototype.focus.call(this, form);
                if (!form) {
                    this.$('.item-name').focus();
                }
            },

            // Maybe this should just be moved into generic.js
            _removeModel: function() {
                if (this.model.collection) {
                    this.model.collection.remove(this.model, { silent: true });
                }
            },

            render: function() {
                openhmis.GenericListItemView.prototype.render.call(this);
                this.updatePriceOptions();
                this.$(".field-price input, .field-total input").attr("readonly", "readonly");
//				this.$('td.field-quantity')
//					.add(this.$('td.field-price'))
//					.add(this.$('td.field-total'))
//					.addClass("numeric");
//				this.$('input[type=number]').stepper({
//					allowArrows: false,
//					onStep: this.stepCallback
//				});
                this.$('td.field-priceName').hide();
                this.$('td.field-priceUuid').hide();
                return this;
            },

            stepCallback: function(val, up) {
                this.update();
            },

        });

        /**
         * BillView
         *
         */
        openhmis.BillView = openhmis.GenericListEntryView.extend({
            initialize: function(options) {
                _.bindAll(this);
                this.events = _.extend({}, this.events, {
                    'keypress': 'onKeyPress'
                });
                var bill = (options && options.bill) ? options.bill : new openhmis.Bill();
                this.setBill(bill);
                openhmis.GenericListEntryView.prototype.initialize.call(this, options);
                options = options ? options : {};
                this.options.roundToNearest = options.roundToNearest || 0;
                this.options.roundingMode = options.roundingMode || "MID";
                this.itemView = openhmis.BillLineItemView;
                this.totalsTemplate = this.getTemplate(openhmis.url.cashierBase + "template/bill.html", '#bill-totals');
            },

            schema: {
                item: { type: "ItemAutocomplete" },
                quantity: { type: "CustomNumber" },
                price: {
                    type: "ItemPriceSelect",
                    options: new openhmis.GenericCollection([0], { model: openhmis.ItemPrice }),
                    format: openhmis.ItemPrice.prototype.format
                }
            },

            setBill: function(bill) {
                this.bill = bill;
                this.model = bill.get("lineItems");
                this.model.on("all", this.updateTotals);
                if (bill.get("status") === bill.BillStatus.PENDING) {
                    this.options.itemActions = ["remove", "inlineEdit"];
                } else {
                    this.options.itemActions = [];
                }
            },

            onItemRemoved: function(itemView) {
                openhmis.GenericListEntryView.prototype.onItemRemoved.call(this, itemView);
                this.updateTotals();
            },

            onItemSelected: function(itemView) {
                openhmis.GenericListEntryView.prototype.onItemSelected.call(this, itemView);
                this.updateTotals();
            },

            // TODO: This shouldn't be here... should be in screen setup
            patientSelected: function(patient) {
                this.bill.set("patient", patient);
                this.focus();
            },

            setupNewItem: function(lineItemView) {
                // Handle adding an item from the input line
                // TODO: Is this the best place to handle changes/setUnsaved()?
                if (lineItemView !== undefined) {
                    this.bill.setUnsaved();
                    lineItemView.on("change remove", this.bill.setUnsaved);
                }
                openhmis.GenericListEntryView.prototype.setupNewItem.call(this, lineItemView);
            },

            onKeyPress: function(event) {
                if (event.keyCode === 13 /* Enter */) {
                    var lineItems = this.bill.get("lineItems");
                    if (lineItems && lineItems.length > 0) {
                        var errors = null;
                        lineItems.each(function(item) {
                            errors = item.validate(item.attributes, '');
                            if (errors != null) {
                                return errors;
                            }
                        });
                    }
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
                var total = openhmis.round(this.bill.getAdjustedTotal(), this.options.roundToNearest, this.options.roundingMode) - this.bill.getAdjustedAmountPaid();
                var totalPaid = this.bill.getTotalPayments();
                this.$totals.html(this.totalsTemplate({
                    bill: this.bill,
                    total: total,
                    totalPaid: totalPaid,
                    formatPrice: openhmis.ItemPrice.prototype.format,
                    __: i18n
                }));
                this.showAmountPayable(total, totalPaid);
            },

            /**
             * @should display balance on the payment input box
             * @param total
             * @param totalPaid
             */
            showAmountPayable: function(total, totalPaid) {
                var $autofill = $('#autofillPaymentAmount');
                if ($autofill.val() == 'true') {
                    var balance = total - totalPaid;
                    $('#amount').val(balance);
                }
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
                    if (self.bill.getTotalPayments() >= self.bill.getTotal()) {
                        self.trigger("paid", self.bill);
                    }
                    /*The line below is to add the the payment for a posted or not fully paid bill*/
                    self.bill.addPayment(payment);

                    self.updateTotals();
                    if (success) {
                        success(model, resp);
                    }
                }
                payment.set("amountTendered", payment.get("amount"));
                var paymentChange = (this.bill.getAdjustedAmountPaid() + this.bill.getTotalPaymentsAmount() + payment.get("amountTendered"))
                    - openhmis.round(this.bill.getAdjustedTotal(), this.options.roundToNearest, this.options.roundingMode);
                if (paymentChange > 0) {
                    payment.set("amount", payment.get("amountTendered") - paymentChange);
                }

                if (this.bill.get("status") === this.bill.BillStatus.PENDING) {
                    /*The line below is to add the payment for a pending bill*/
                    this.bill.addPayment(payment);

                    if (!this.postBill(options));
                    this.bill.get("payments").remove(payment);
                } else {
                    payment.meta.parentRestUrl = this.bill.url() + '/';
                    payment.save([], options);
                }
            },

            validate: function(allowEmptyBill) {
                var errors = this.bill.validate(true);
                var elMap = {
                    'lineItems': [$('#bill'), this],
                    'patient': [$('#patient-view'), $('#inputNode')]
                }
                if (allowEmptyBill === true
                    && errors
                    && errors.lineItems !== undefined) {
                    delete errors.lineItems;
                }
                if (errors && _.size(errors) > 0) {
                    for (var e in errors) {
                        openhmis.validationMessage(elMap[e][0], errors[e], elMap[e][1]);
                    }
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
                if (!this.validate(allowEmptyBill)) {
                    return false;
                }
                if (this.cashPointForm !== undefined) {
                    this.bill.set("cashPoint", this.cashPointForm.getValue("cashPoint"));
                }
                // Filter out any invalid lineItems (especially the bottom)
                // entry cursor
                this.bill.get("lineItems").reset(
                    this.model.filter(function(item) {
                        return item.isClean();
                    }),
                    { silent: true }
                );

                if (post === true
                    && this.bill.get("billAdjusted")
                    && this.bill.get("status") === this.bill.BillStatus.PENDING) {
                    this._postAdjustingBill(this.bill);
                } else if (post === true) {
                    this.bill.set("status", this.bill.BillStatus.POSTED);
                }
                var print = options.print;
                var success = options.success;
                var error = options.error;
                var self = this;
                options.success = function(model, resp) {
                    self.trigger(print ? "saveAndPrint" : "save", model);
                    if (success) {
                        success(model, resp);
                    }
                }
                options.error = function(model, resp) {
                    openhmis.error(resp);
                    if (error) {
                        error(model, resp);
                    }
                }
                this.bill.save([], options);
                return true;
            },

            postBill: function(options) {
                return this.saveBill(options, true);
            },

            _postAdjustingBill: function(bill) {
                bill.get("payments").add(bill.get("billAdjusted").get("payments").models);
                var self = this;
                bill.get("billAdjusted").get("payments").each(function(payment) {
                    payment.set("amountTendered", payment.get("amount"));
                    if (payment.get("uuid") != null || payment.get("uuid") != undefined) {
                        payment.set("uuid", "");
                        self.handlePreviousPayments(payment);
                    }
                });
                var adjustingItems = bill.get("lineItems");
                bill.set("lineItems", bill.get("billAdjusted").get("lineItems"));
                bill.get("lineItems").add(adjustingItems.models);
                bill.get("lineItems").each(function(lineItem) {
                    if (lineItem.get("uuid") != null || lineItem.get("uuid") != undefined) {
                        lineItem.set("uuid", "");
                    }
                });
                bill.set("status", bill.BillStatus.POSTED);
            },

            handleAdjustBill: function() {
                var __ = i18n;
                var $adjustmentReason;
                if ($('#showAdjustmentReasonField').val() === 'false') {
                    if (confirm(__(openhmis.getMessage('openhmis.cashier.adjustedReasonPrompt')))) {
                        this.adjustBill("");
                    }
                } else {
                    $adjustmentReason = prompt(__(openhmis.getMessage('openhmis.cashier.adjustedReasonMessage')));
                    if ($adjustmentReason != null && $adjustmentReason != "") {
                        this.adjustBill($adjustmentReason);
                    } else if ($adjustmentReason == "") {
                        alert(openhmis.getMessage('openhmis.cashier.adjustedReasonRequiredPrompt'));
                    }
                }
            },

            adjustBill: function(adjustmentReason) {
                var __ = i18n;
                var adjustingBill = new openhmis.Bill({
                    adjustmentReason: adjustmentReason,
                    billAdjusted: this.bill.id,
                    patient: this.bill.get("patient").id
                });
                /**/
                adjustingBill.unset("status");
                var view = this;
                adjustingBill.save([], {
                    success: function(model, resp) {
                        view.trigger("adjusted", model);
                    },
                    error: openhmis.error
                });
            },

            handlePreviousPayments: function(payment) {
                var paymentAttributes = payment.get("attributes");
                for (attribute in paymentAttributes) {
                    var attr = paymentAttributes[attribute];
                    attr.set("uuid", "");
                    attr.set("value", attr.get("valueName"));
                }
            },

            printReceipt: function(event) {
                var url = openhmis.url.getPage("cashierBase")
                    + "receipt.form?billId=" + encodeURIComponent(this.bill.get("id"));
                // Remove if print has been clicked before?
                $("#receiptDownload").remove();
                $iframe = $('<iframe id="receiptDownload" src="' + url + '" width="1" height="1"></iframe>');
                $iframe.load(function() {
                    $(this).get(0).contentWindow.print();
                });
                $("body").append($iframe);
            },

            render: function() {
                openhmis.GenericListEntryView.prototype.render.call(this, { options: { listTitle: "" } });
                this.$('table').addClass("bill");
                this.$totals = $('<table class="totals"></table>');
                this.$('div.box').append(this.$totals);
                this.updateTotals();
                this.$('th.field-priceName').hide();
                this.$('td.field-priceName').hide();
                this.$('th.field-priceUuid').hide();
                this.$('td.field-priceUuid').hide();

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
                    listFields: ["dateCreatedFmt", "instanceType", "amountFmt"],
                    showPaging: false,
                    showRetiredOption: false
                });
            },

            render: function() {
                this.$el.append(this.itemsView.render().el);
                this.itemsView.$("table").addClass("bill");
                this.$el.append(this.paymentsView.render().el);
                this.$('th.field-priceName').hide();
                this.$('td.field-priceName').hide();
                this.$('th.field-priceUuid').hide();
                this.$('td.field-priceUuid').hide();
                return this;
            }
        });

        return openhmis;
    }
);
