define(
    [
        'lib/underscore',
        'lib/backbone',
        'model/generic',
        'lib/i18n',
        'model/item',
        'model/fieldGenHandler'
    ],
    function(_, Backbone, openhmis, __) {
        openhmis.Payment = openhmis.GenericModel.extend({
            meta: {
                name: "Payment",
                namePlural: "Payments",
                restUrl: "payment"
            },
            
            schema: {
                dateCreated: { type: 'Text', readOnly: true },
                dateCreatedFmt: { type: 'Text', title: __("Date"), readOnly: true },
                amount: { type: 'BasicNumber' },
                amountFmt: { type: 'BasicNumber', title: __("Amount"), readOnly: true },
                paymentMode: { type: 'Text' }
            },
            
            url: function() {
                if (this.meta.parentRestUrl)
                    this.urlRoot = this.meta.parentRestUrl + this.meta.restUrl;
                return openhmis.GenericModel.prototype.url.call(this);
            },
            
   			get: function(attr) {
				switch (attr) {
					case 'dateCreatedFmt':
                        var date = new Date(this.get("dateCreated"));
						return openhmis.dateFormat(date);
					case 'amountFmt':
                        return openhmis.ItemPrice.prototype.format.call(this, this.get("amount"));
					default:
						return openhmis.GenericModel.prototype.get.call(this, attr);
				}
			},
            
            parse: function(resp) {
                if (resp.paymentMode)
                    resp.paymentMode = new openhmis.PaymentMode(resp.paymentMode);
                return resp;
            },
            
            toJSON: function() {
                var attrs = openhmis.GenericModel.prototype.toJSON.call(this);
                if (attrs.paymentMode)
                    attrs.paymentMode = attrs.paymentMode.id;
                return attrs;
            }
        });
        
        openhmis.PaymentModeAttributeType = openhmis.GenericModel.extend({
            meta: {
                name: "Attribute Type",
                namePlural: "Attribute Types",
                openmrsType: 'metadata',
                restUrl: 'paymentMode'
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
            toString: function() { return this.get('name'); }
        });

        openhmis.PaymentMode = openhmis.GenericModel.extend({
            meta: {
                name: "Payment Mode",
                namePlural: "Payment Modes",
                openmrsType: 'metadata',
                restUrl: 'paymentMode'
            },
            
            schema: {
                name: { type: 'Text' },
                description: { type: 'Text' },
                attributeTypes: { type: 'List', itemType: 'NestedModel', model: openhmis.PaymentModeAttributeType }
            },

            toJSON: function() {
                if (this.attributes.attributeTypes !== undefined) {
                    // Can't set these, so need to remove them from JSON
                    for (var attributeType in this.attributes.attributeTypes)
                        delete this.attributes.attributeTypes[attributeType].resourceVersion;
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