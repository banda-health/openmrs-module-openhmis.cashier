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
        openhmis.url.backboneBase + 'js/lib/underscore',
        openhmis.url.backboneBase + 'js/lib/backbone',
        openhmis.url.backboneBase + 'js/model/generic',
        openhmis.url.backboneBase + 'js/lib/i18n',
        openhmis.url.inventoryBase + 'js/model/item',
        openhmis.url.backboneBase + 'js/model/fieldGenHandler'
    ],
    function(_, Backbone, openhmis, __) {
        openhmis.PaymentAttribute = openhmis.GenericModel.extend({
            schema: {
                attributeType: { type: "Object", objRef: true },
                value: { type: "Text" }
            },
            
            parse: function(resp) {
                if (resp.attributeType) {
                    resp.attributeType =
                        new openhmis.PaymentModeAttributeType(resp.attributeType, { parse: true });
                }
                return resp;
            }
        });
        
        openhmis.Payment = openhmis.GenericModel.extend({
            meta: {
                name: "Payment",
                namePlural: "Payments",
                restUrl: "v2/cashier/payment"
            },
            
            schema: {
                dateCreated: { type: 'Text', readOnly: true },
                dateCreatedFmt: { type: 'Text', title: __("Date"), readOnly: true },
                amount: { type: 'BasicNumber' },
                amountFmt: { type: 'BasicNumber', title: __("Amount"), readOnly: true },
                amountTendered: { type: 'BasicNumber' },
                amountTenderedFmt: { type: 'BasicNumber', title: __("Amount"), readOnly: true },
                instanceType: { type: 'Object', objRef: true },
                attributes: { type: 'List', itemType: 'NestedModel', model: openhmis.PaymentAttribute }
            },
            
            url: function() {
                if (this.meta.parentRestUrl) {
                    this.urlRoot = this.meta.parentRestUrl +
                    	this.meta.restUrl.substring(this.meta.restUrl.lastIndexOf('/') + 1);
                }
                return openhmis.GenericModel.prototype.url.call(this);
            },
            
   			get: function(attr) {
				switch (attr) {
					case 'dateCreatedFmt':
                        var date = new Date(this.get("dateCreated"));
						return openhmis.dateFormat(date);
					case 'amountFmt':
                        return openhmis.ItemPrice.prototype.format.call(this, this.get("amount"));
					case 'amountTenderedFmt':
                        return openhmis.ItemPrice.prototype.format.call(this, this.get("amountTendered"));
					default:
						return openhmis.GenericModel.prototype.get.call(this, attr);
				}
			},
            
            validate: function(goAhead) {
   				// By default, backbone validates every time we try try to alter
				// the model.  We don't want to be bothered with this until we
				// care.
                if (goAhead !== true) {
                	return null;
                }
                
                if (this.get("amount") === null || this.get("amount") === undefined) {
                	return { amount: __("Amount is required.") }
                }
                if (isNaN(this.get("amount"))) {
                    return { amount: __("Amount needs to be a number") }
                }
                if (!this.get("instanceType") || !this.get("instanceType").id) {
                    return { instanceType: __("Payment mode is required.") }
                }
                return null;
            },
            
            parse: function(resp) {
                if (resp.instanceType) {
                    resp.instanceType = new openhmis.PaymentMode(resp.instanceType);
                }
                if (resp.attributes) {
                    var attributes = resp.attributes;
                    resp.attributes = [];
                    for (attr in attributes) {
                        var paymentAttribute = new openhmis.PaymentAttribute(attributes[attr], { parse: true });
                        if (attributes[attr].order !== undefined) {
                            resp.attributes[attributes[attr].order] = paymentAttribute;
                        } else {
                            resp.attributes.push(paymentAttribute);
                        }
                    }
                }
                return resp;
            },
            
            toJSON: function() {
                var attrs = openhmis.GenericModel.prototype.toJSON.call(this);
                return attrs;
            }
        });
        
        openhmis.PaymentModeAttributeType = openhmis.GenericModel.extend({
            meta: {
                name: "Attribute Type",
                namePlural: "Attribute Types",
                openmrsType: 'metadata',
                restUrl: 'v2/cashier/paymentMode'
            },

            schema: {
                name: { type: 'Text' },
                format: {
                    type: 'Select',
                    options: new openhmis.FieldFormatCollection()
                },
                foreignKey: { type: 'BasicNumber' },
                regExp: { type: 'Text' },
                required: { type: 'Checkbox' }
            },
            
            validate: function(attrs, options) {
   				if (!attrs.name) {
   					return { name: __("A name is required") }
   				}
                return null;
            },
            
            toString: function() { return this.get('name'); }
        });

        openhmis.PaymentMode = openhmis.GenericModel.extend({
            meta: {
                name: "Payment Mode",
                namePlural: "Payment Modes",
                openmrsType: 'metadata',
                restUrl: 'v2/cashier/paymentMode'
            },
            
            schema: {
                name: { type: 'Text' },
                description: { type: 'Text' },
                attributeTypes: { type: 'List', itemType: 'NestedModel', model: openhmis.PaymentModeAttributeType }
            },
            
            validate: function(attrs, options) {
   				if (!attrs.name) {
   					return { name: __("A name is required") }
   				}
                return null;
            },

            toJSON: function() {
                if (this.attributes.attributeTypes !== undefined) {
                    // Can't set these, so need to remove them from JSON
                    for (var attributeType in this.attributes.attributeTypes) {
                        delete this.attributes.attributeTypes[attributeType].resourceVersion;
                    }
                }
                return openhmis.GenericModel.prototype.toJSON.call(this);
            },
            
            toString: function() {
                return this.get('name');
            }
        });

        return openhmis;
    }
);