define(
	[
		openhmis.url.backboneBase + 'js/lib/jquery',
		openhmis.url.backboneBase + 'js/lib/backbone',
		openhmis.url.cashierBase + 'js/model/payment',
		openhmis.url.backboneBase + 'js/lib/i18n',
		openhmis.url.cashierBase + 'js/model/bill',
		openhmis.url.backboneBase + 'js/lib/backbone-forms',
		openhmis.url.backboneBase + 'js/view/generic'
	],
	function($, Backbone, openhmis, i18n) {
		openhmis.PaymentModeAddEditView = openhmis.GenericAddEditView.extend({
			prepareModelForm: function(model, options) {
				var form = openhmis.GenericAddEditView.prototype.prepareModelForm.call(this, model, options);
				form.on('attributeTypes:change', this.makeTypesSortable);
				this.makeTypesSortable(form);
				return form;
			},
			
			makeTypesSortable: function(form) {
				form = form ? form : this.modelForm;
				form.$('.bbf-list ul').sortable();
			},
			
			save: function() {
				var attributes = this.$('.bbf-list ul').sortable("widget").children();
				$(attributes).each(function() {
					$(this).attr("id", "attr-" + $(attributes).index(this));
				});
				var items = this.modelForm.fields['attributeTypes'].editor.items;
				for (var id in items) {
					var getValue = items[id].getValue;
					var newGetValue = function() {
						var order = $(this.el).attr("id");
						order = parseInt(order.substring(order.lastIndexOf('-') + 1));
						var value = getValue.call(this);
						value.attributeOrder = order;
						return value;
					}
					items[id].getValue = newGetValue;
				}
				openhmis.GenericAddEditView.prototype.save.call(this);
			}
		});
		
		openhmis.PaymentListItemView = openhmis.GenericListItemView.extend({
			render: function() {
				openhmis.GenericListItemView.prototype.render.call(this);
				var detailsTemplate = this.getTemplate(
					openhmis.url.cashierBase + 'template/payment.html',
					"#payment-attributes"
				);
				this.$("td.field-Details").html(detailsTemplate({ attributes: this.model.get("attributes") }));
				return this;
			}
		});
		
		openhmis.PaymentView = Backbone.View.extend({
			tmplFile: openhmis.url.cashierBase + 'template/payment.html',
			tmplSelector: '#payment-view',
			initialize: function(options) {
				_.bindAll(this, "focus");
				if (options) {
					this.processCallback = options.processCallback;
					if (options.paymentCollection)
						this.paymentCollection = options.paymentCollection;
					this.readOnly = options.readOnly;
				}
				this.paymentCollection = this.paymentCollection ? this.paymentCollection
					: new openhmis.GenericCollection([], { model: openhmis.Payment });
				this.paymentCollection.comparator = function(payment) {
					return payment.get("dateCreated");
				}
				this.paymentCollection.sort();
				this.paymentListView = new openhmis.GenericListView({
					model: this.paymentCollection,
					itemView: openhmis.PaymentListItemView,
					id: "paymentList",
					className: "paymentList",
					listFields: ['dateCreatedFmt', 'Details', 'amountTenderedFmt', 'paymentMode'],
					//itemActions: ["details"],
					showRetiredOption: false,
					showPaging: false,
					hideIfEmpty: true
				});
				this.template = this.getTemplate();
				if (!this.readOnly) {
					if (this.model === undefined) this.model = new openhmis.Payment();
					this.form = new Backbone.Form({
						schema: {
							paymentMode: {
								type: 'Select',
								options: new openhmis.GenericCollection([], { model: openhmis.PaymentMode })
							},
							amount: {
								type: 'BasicNumber'
							}
						}
					});
				}
			},
			
            events: {
                'change #paymentMode': 'paymentModeChange',
				'click #processPayment': 'processPayment'
            },
			
			focus: function() {
				this.form.focus();
			},
			
			paymentModeChange: function(event) {
				var paymentModeUuid = $(event.target).val();
				var self = this;
				// Load payment mode form HTML fragment from server
				this.$attributes.load(openhmis.url.getPage("cashierBase") + "paymentModeFragment.form?uuid=" + paymentModeUuid,
					function(content) {
						if ($(self.$attributes).find('#openmrs_dwr_error').length > 0 && content.indexOf("ContextAuthenticationException") !== -1) {
							$(self.$attributes).html("");
							openhmis.error({ responseText: '{"error":{"detail":"ContextAuthenticationException"}}' });
						}
					}
				)
			},
			
			commitForm: function() {
				var attributeForm = this.$attributes.serializeArray();
				var attributes = [];
				var errors = {};
				for (var i in attributeForm) {
					try {
						var meta = $.parseJSON(this.$attributes.find('#'+attributeForm[i].name+'-meta').text());
					} catch (e) {}
					if (meta && meta.required === true && !attributeForm[i].value) {
						errors[attributeForm[i].name] = "This is a required field.";
						break;
					}
					attributes[i] = new openhmis.PaymentAttribute({
						paymentModeAttributeType: attributeForm[i].name,
						value: attributeForm[i].value
					});
				}
				for (var e in errors) {
					this.displayErrors(errors);
					return false;
				}
				this.model.set("attributes", attributes);
				this.model.set("amount", this.form.getValue("amount"));
				this.model.set("paymentMode", new openhmis.PaymentMode({
					uuid: this.form.getValue("paymentMode"),
					name: this.form.fields["paymentMode"].editor.$('option:selected').text()
				}));
				this.model.set("dateCreated", new Date().getTime());
				errors = this.model.validate(true);
				if (errors) {
					this.displayErrors(errors);
					return false;
				}
				return true;
			},
			
			displayErrors: function(errorMap) {
				for(var item in errorMap) {
					var $errorControl = this.$('#'+item);
					var $errorEl = $errorControl.parent();
					if ($errorEl.length > 0) {
						openhmis.validationMessage($errorEl, errorMap[item]);
						$errorControl.focus();
					}
				}
			},
			
			processPayment: function(event) {
				if (!this.commitForm()) return;
				if (confirm(i18n("Are you sure you want to process a %s payment of %s?",
							   this.model.get("paymentMode"), this.model.get("amountFmt")))) {
					var self = this;
					this.processCallback(this.model, { success: function(model, resp) {
						if (model instanceof openhmis.Bill) {
							// Entire bill was saved, so we expect the page to be
							// refreshed soon
						} else {
							// Payment has been saved, so it will be automatically
							// added to the payment collection, triggering rendering
						}
						// Set up new empty Payment
						self.model = new openhmis.Payment();
						self.form.fields["amount"].setValue("");
						self.$("#paymentAttributes input").val("");
					}});
				}
			},
			
			render: function() {
				this.$el.html(this.template({ __: i18n, readOnly: this.readOnly }));
				if (!this.readOnly)
					this.$el.prepend(this.form.render().el);
				this.$el.prepend(this.paymentListView.el);
				if (this.paymentCollection.filter(function(payment) { return !payment.get("voided"); }).length === 0
						&& this.paymentListView.options.hideIfEmpty) {
					// Skip payment list
				} else
					this.paymentListView.render();
				if (!this.readOnly) {
					this.$attributes = this.$('#paymentAttributes');
					var self = this;
					this.form.$el.add(this.$attributes).submit(function(event) {
						event.preventDefault();
						self.processPayment();
					});
				}
				return this;
			}
		});
		
		return openhmis;
	}
);