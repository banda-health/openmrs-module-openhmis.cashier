define(
    [
        'lib/underscore',
        'lib/backbone',
        'model/generic',
        'lib/i18n',
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
                dateCreated: { type: 'Text', title: __("Date") },
                amount: { type: 'BasicNumber' },
            },
            
            url: function() {
                if (this.meta.parentRestUrl)
                    this.urlRoot = this.meta.parentRestUrl + this.meta.restUrl;
                return openhmis.GenericModel.prototype.url.call(this);
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