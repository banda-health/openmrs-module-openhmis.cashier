define(
	[
		'lib/jquery',
		'lib/backbone',
		'model/payment',
		'lib/i18n',
		'lib/backbone-forms',
		'view/generic'
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
						var order = parseInt(order.substring(order.lastIndexOf('-') + 1));
						var value = getValue.call(this);
						value.attributeOrder = order;
						return value;
					}
					items[id].getValue = newGetValue;
				}
				openhmis.GenericAddEditView.prototype.save.call(this);
			}
		});
		
		openhmis.PaymentView = Backbone.View.extend({
			tmplFile: 'payment.html',
			tmplSelector: '#payment-view',
			initialize: function(options) {
				if (options) {
					this.processCallback = options.processCallback;
				}
				this.template = this.getTemplate();
				if (this.model === undefined) this.model = new openhmis.Payment();
				this.modeChoice = new Backbone.Form({
					schema: {
						paymentMode: {
							type: 'Select',
							options: new openhmis.GenericCollection([], { model: openhmis.PaymentMode })
						},
						paymentAmount: {
							type: 'BasicNumber'
						}
					}
				});
			},
			
            events: {
                'change #paymentMode': 'paymentModeChange',
				'click #processPayment': 'processPayment'
            },
			
			paymentModeChange: function(event) {
				var paymentModeUuid = $(event.target).val();
				var self = this;
				this.$attributes.load(openhmis.config.pageUrlRoot + "paymentModeFragment.form?uuid=" + paymentModeUuid,
					function(content) {
						if ($(self.$attributes).find('#openmrs_dwr_error').length > 0 && content.indexOf("ContextAuthenticationException") !== -1) {
							$(self.$attributes).html("");
							openhmis.error({ responseText: '{"error":{"detail":"ContextAuthenticationException"}}' });
						}
					}
				)
			},
			
			processPayment: function(event) {
				this.paymentCallback(this.model);
			},
			
			render: function() {
				this.$el.html(this.template({ __: i18n }));
				this.$el.prepend(this.modeChoice.render().el);
				this.$attributes = this.$('#paymentAttributes');
				return this;
			}
		});
		
		return openhmis;
	}
);