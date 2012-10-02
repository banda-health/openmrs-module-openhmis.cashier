define(
	[
		'lib/jquery',
		'view/generic'
	],
	function($, openhmis) {
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
		
		return openhmis;
	}
);