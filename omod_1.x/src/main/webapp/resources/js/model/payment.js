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
        openhmis.url.backboneBase + 'js/model/generic',
        openhmis.url.backboneBase + 'js/lib/i18n',
        openhmis.url.inventoryBase + 'js/model/item',
        openhmis.url.backboneBase + 'js/model/fieldGenHandler',
        openhmis.url.backboneBase + 'js/model/openhmis'
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
                name: openhmis.getMessage('openhmis.cashier.payment'),
                namePlural: openhmis.getMessage('openhmis.cashier.paymentPlural'),
                restUrl: "v2/cashier/payment"
            },

            schema: {
                dateCreated: { type: 'Text', readOnly: true },
                dateCreatedFmt: { type: 'Text', title: __(openhmis.getMessage('openhmis.cashier.payment.detailsTitle.date')), readOnly: true },
                amount: { type: 'BasicNumber' },
                amountFmt: { type: 'BasicNumber', title: __(openhmis.getMessage('openhmis.cashier.payment.detailsTitle.amount')), readOnly: true },
                amountTendered: { type: 'BasicNumber' },
                amountTenderedFmt: { type: 'BasicNumber', title: __(openhmis.getMessage('openhmis.cashier.payment.detailsTitle.tendered')), readOnly: true },
                instanceType: { type: 'Object', objRef: true, title: __(openhmis.getMessage('openhmis.cashier.paymentMode.name'))},
                attributes: { type: 'List', itemType: 'NestedModel', model: openhmis.PaymentAttribute , title: __(openhmis.getMessage('openhmis.cashier.payment.detailsTitle.details')) }
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
                	return { amount: __(openhmis.getMessage('openhmis.cashier.payment.error.amountRequired')) }
                }
                if (isNaN(this.get("amount"))) {
                    return { amount: __(openhmis.getMessage('openhmis.cashier.payment.error.amountType')) }
                }
                if (!this.get("instanceType") || !this.get("instanceType").id) {
                    return { instanceType: __(openhmis.getMessage('openhmis.cashier.payment.error.paymentMode.required')) }
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

        openhmis.PaymentModeAttributeType = openhmis.InstanceAttributeTypeBase.extend({
            meta: {
                restUrl: 'v2/cashier/paymentMode',
                confirmDelete: openhmis.getMessage('openhmis.cashier.payment.confirm.paymentAttribute.delete')
            }
        });

        openhmis.PaymentMode = openhmis.CustomizableInstanceTypeBase.extend({
            attributeTypeClass: openhmis.PaymentModeAttributeType,

            meta: {
                name: openhmis.getMessage('openhmis.cashier.paymentMode.name'),
                namePlural: openhmis.getMessage('openhmis.cashier.paymentModes.namePlural'),
                restUrl: 'v2/cashier/paymentMode'
            },

            schema: {
                name: { type: 'Text' },
                description: { type: 'Text' },
                sortOrder: { type: 'BasicNumber' }
            },

            toString: function() {
                return this.get('name');
            }
        });

        return openhmis;
    }
);
