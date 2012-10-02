define(
	[
		'lib/jquery',
		'lib/backbone',
		'model/paymentMode',
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
				this.template = this.getTemplate();
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
                'change #paymentMode': 'paymentModeChange'
            },
			
			paymentModeChange: function(event) {
				var paymentModeUuid = $(event.target).val();
				this.$attributes.load(openhmis.config.pageUrlRoot + "paymentModeFragment.form?uuid=" + paymentModeUuid);
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