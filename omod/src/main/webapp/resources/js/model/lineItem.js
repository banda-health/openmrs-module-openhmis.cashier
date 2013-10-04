/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
define(
	[
		openhmis.url.backboneBase + 'js/lib/backbone',
		openhmis.url.backboneBase + 'js/model/generic',
		openhmis.url.backboneBase + 'js/lib/i18n',
		openhmis.url.inventoryBase + 'js/model/item'
	],
	function(Backbone, openhmis, __) {
		openhmis.LineItem = openhmis.GenericModel.extend({
			meta: {
				name: 'Line Item',
				namePlural: 'Line Items'
			},
			schema: {
				item: { type: 'NestedModel', model: openhmis.Item, objRef: true },
				quantity: { type: 'BasicNumber' },
				price: { type: 'BasicNumber', format: openhmis.ItemPrice.prototype.format },
				total: { type: 'BasicNumber', readOnly: true, format: openhmis.ItemPrice.prototype.format }
			},
			
			initialize: function(attributes, options) {
				if (attributes && attributes.item && !(attributes.item instanceof Backbone.Model)) {
					this.set("item", new openhmis.Item(attributes.item));
				}
				this.schema.total.value = this.getTotal;
				this.set("total", this.getTotal(), { silent: true });
				this.clean = false;
			},
			
			validate: function(attrs, options) {
				if (!attrs.item || !attrs.item.id) return { item: __("Please choose an item") };
				if (!attrs.item.get('department')) return { item: __("Item must belong to a department") };
				if (!attrs.quantity || isNaN(attrs.quantity)) return { quantity: __("Please enter a quantity") }
				return null;
			},
			
			_validate: function(attrs, options) {
				var valid = openhmis.GenericModel.prototype._validate.call(this, attrs, options);
				if (valid) this.clean = true;
				return valid;
			},
			
			isClean: function() {
				return this.clean;
			},
			
			dirty: function() {
				this.clean = false;
			},
			
			get: function(attr) {
				switch (attr) {
					case 'total':
						return this.getTotal();
					default:
						return openhmis.GenericModel.prototype.get.call(this, attr);
				}
			},
			
			getTotal: function() {
				if (this.get('quantity') === undefined
					|| this.get('price') === undefined)
					return undefined;
				return this.get('price') * this.get('quantity');
			},
			
			parse: function(resp) {
				if (resp.item)
					resp.item = new openhmis.Item(resp.item, { parse: true });
				if (resp.quantity && resp.price) resp.total = resp.price * resp.quantity;
				return resp;
			},
			
			toJSON: function() {
				var attrs = openhmis.GenericModel.prototype.toJSON.call(this);
				if (attrs.price)
					attrs.price = parseFloat(attrs.price);
				return attrs;
			}
		});
		
		return openhmis;
	}
);