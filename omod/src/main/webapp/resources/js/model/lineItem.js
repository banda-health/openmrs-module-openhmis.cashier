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
		openhmis.url.backboneBase + 'js/model/generic',
		openhmis.url.backboneBase + 'js/lib/i18n',
		openhmis.url.inventoryBase + 'js/model/item'
	],
	function(Backbone, openhmis, __) {
		openhmis.LineItem = openhmis.GenericModel.extend({
			meta: {
				name: openhmis.getMessage('openhmis.cashier.bill.lineItems'),
				namePlural: openhmis.getMessage('openhmis.cashier.bill.lineItemsPlural')
			},
			schema: {
				item: { type: 'NestedModel', model: openhmis.Item, objRef: true },
				quantity: { type: 'BasicNumber' },
				price: { type: 'BasicNumber', format: openhmis.ItemPrice.prototype.format },
				priceName: { type: 'Text'},
				priceUuid: { type: 'Text'},
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
				if (!attrs.item || !attrs.item.id) {
					return { item: __(openhmis.getMessage('openhmis.cashier.bill.lineItems.error.itemChoose')) };
				}
				if (!attrs.item.get('department')) {
					return { item: __(openhmis.getMessage('openhmis.cashier.bill.lineItems.error.itemDepartment')) };
				}
				if (!attrs.quantity || isNaN(attrs.quantity) || attrs.quantity === 0) {
					return { quantity: __(openhmis.getMessage('openhmis.cashier.bill.lineItems.error.quantityRequired')) }
				}
				return null;
			},
			
			_validate: function(attrs, options) {
				var valid = openhmis.GenericModel.prototype._validate.call(this, attrs, options);
				if (valid) {
					this.clean = true;
				}
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
			
			set: function(key, value, options) {
				if (key.quantity === 0) {
					return this.setQuantity(key, value, options)
				} else {
					return openhmis.GenericModel.prototype.set.call(this, key, value, options);
				}
			},
			
			setQuantity: function (key, value, options) {
				var attrs, attr, val;

				// Handle both `"key", value` and `{key: value}` -style arguments.
				attrs = key;
				options = value;

				// Extract attributes and options.
				options || (options = {});
				
				if (attrs instanceof Backbone.Model) {
					attrs = attrs.attributes;
				}
				
				// Run validation.
				var validationResult = this._validate(attrs, options);

				var changes = options.changes = {};
				var now = this.attributes;
				var escaped = this._escapedAttributes;
				var prev = this._previousAttributes || {};

				// For each `set` attribute...
				for (attr in attrs) {
					if (attr === "quantity") {
						val = attrs[attr];
						// If the new and current value differ, record the change.
						if (!_.isEqual(now[attr], val) || (options.unset && _.has(now, attr))) {
							delete escaped[attr];
							(options.silent ? this._silent : changes)[attr] = true;
						}
						
	
						// Update or delete the current value.
						options.unset ? delete now[attr] : now[attr] = val;
	
						// If the new and previous value differ, record the change.  If not,
						// then remove changes for this attribute.
						if (!_.isEqual(prev[attr], val) || (_.has(now, attr) != _.has(prev, attr))) {
							this.changed[attr] = val;
							if (!options.silent) this._pending[attr] = true;
						} else {
							delete this.changed[attr];
							delete this._pending[attr];
						}
					}
				}
				
				if(!validationResult) {
					return false
				}

				// Fire the `"change"` events.
				if (!options.silent) this.change(options);
					return this;
			},
			
			getTotal: function() {
				if (this.get('quantity') === undefined || this.get('price') === undefined) {
					return undefined;
				}
				return this.get('price') * this.get('quantity');
			},
			
			parse: function(resp) {
				if (resp.item) {
					resp.item = new openhmis.Item(resp.item, { parse: true });
				}
				if (resp.quantity && resp.price) {
					resp.total = resp.price * resp.quantity;
				}
				return resp;
			},
			
			toJSON: function() {
				var attrs = openhmis.GenericModel.prototype.toJSON.call(this);
				if (attrs.price) {
					if (_.isObject(attrs.price) && (attrs.priceUuid == null || attrs.priceUuid == "")) {
						attrs.priceUuid = attrs.price.get('uuid');
					}
					attrs.price = parseFloat(attrs.price);
				}
				return attrs;
			}
		});
		
		return openhmis;
	}
);
