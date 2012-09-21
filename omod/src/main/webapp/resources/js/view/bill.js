define(
	[
		'lib/jquery',
		'lib/underscore',
		'view/generic',
		'view/editors'
	],
	function($, _, openhmis) {
		openhmis.BillLineItemView = openhmis.GenericListItemView.extend({
			initialize: function(options) {
				openhmis.GenericListItemView.prototype.initialize.call(this, options);
				this.form.on('price:change', this.update);
				this.form.on('quantity:change', this.update);
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
			
			removeModel: function() {
				this.model.collection.remove(this.model);
			},
			
			render: function() {
				openhmis.GenericListItemView.prototype.render.call(this);
				this.$(".field-total input").attr("readonly", "readonly");
				return this;
			}
		});

		openhmis.BillView = openhmis.GenericListView.extend({
			initialize: function(options) {
				openhmis.GenericListView.prototype.initialize.call(this, options);
				this.itemView = openhmis.BillLineItemView;
			},
			itemActions: ['remove', 'inlineEdit'],
			schema: {
				item: { type: 'Item' },
				quantity: { type: 'Number'}
			},
			
			itemRemoved: function(item) {
				openhmis.GenericListView.prototype.itemRemoved.call(this, item);
				if (item.isNew()) {
					this.model.add(new openhmis.LineItem());
					this.render();
				}
			},
			
			render: function() {
				openhmis.GenericListView.prototype.render.call(this, {
					listTitle: ""
				});
				this.$("table").addClass("bill");
				return this;
			}
		});
		
		return openhmis;
	}	
);