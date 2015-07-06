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
		openhmis.url.backboneBase + 'js/lib/backbone',
		openhmis.url.cashierBase + 'js/model/payment',
		openhmis.url.backboneBase + 'js/lib/i18n',
		openhmis.url.cashierBase + 'js/model/bill',
		openhmis.url.backboneBase + 'js/lib/backbone-forms',
		openhmis.url.backboneBase + 'js/view/generic'
	],
	function(Backbone, openhmis, i18n) {
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
					if (options.paymentCollection) {
						this.paymentCollection = options.paymentCollection;
					}
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
					listFields: ['dateCreatedFmt', 'attributes', 'amountTenderedFmt', 'amountFmt', 'instanceType'],
					showRetiredOption: false,
					showPaging: false,
					hideIfEmpty: true
				});
				this.template = this.getTemplate();
				if (!this.readOnly) {
					if (this.model === undefined) {
						this.model = new openhmis.Payment();
					}
					this.form = new Backbone.Form({
						schema: {
							paymentMode: {
								type: 'Select',
								options: new openhmis.GenericCollection([], { model: openhmis.PaymentMode }),
							},
							amount: {
								type: 'BasicNumber'
							}
						}
					});
					$('#payment').addClass("box");
				} else {
					$('#payment').removeClass("box")
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
						errors[attributeForm[i].name] = openhmis.getMessage('openhmis.cashier.error.fieldRequired');
						break;
					}
					attributes[i] = new openhmis.PaymentAttribute({
						attributeType: attributeForm[i].name,
						value: attributeForm[i].value
					});
				}
				for (var e in errors) {
					this.displayErrors(errors);
					return false;
				}
				this.model.set("attributes", attributes);
				this.model.set("amount", this.form.getValue("amount"));
				this.model.set("instanceType", new openhmis.PaymentMode({
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
				if (!this.commitForm()) {
					return;
				}
				if (confirm(i18n(openhmis.getMessage('openhmis.cashier.payment.confirm.paymentProcess'), this.model.get("instanceType"), this.model.get("amountFmt")))) {
					var self = this;
					this.processCallback(this.model, { success: function(model, resp) {
						// Set up new empty Payment
						self.model = new openhmis.Payment();
						self.$("#paymentAttributes input").val("");
					}});
				}
			},
			
			render: function() {
				this.$el.html(this.template({ __: i18n, readOnly: this.readOnly }));
				if (!this.readOnly) {
					this.$el.prepend(this.form.render().el);
					this.form.$el.attr("class", "bbf-form payment-container");
				}
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
