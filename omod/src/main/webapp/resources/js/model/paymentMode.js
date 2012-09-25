openhmis.PaymentModeAttributeType = openhmis.GenericModel.extend({
    meta: {
        name: "Attribute Type",
        namePlural: "Attribute Types",
        openmrsType: 'metadata'
    },

    schema: {
        name: 'Text',
        format: 'Text',
        regexp: 'Text',
        required: 'Checkbox'
    },
    toString: function() { return this.get('name'); }
});

openhmis.PaymentMode = openhmis.GenericModel.extend({
    meta: {
        name: __("Payment Mode"),
        namePlural: __("Payment Modes"),
        openmrsType: 'metadata'
    },

    schema: {
        name: 'Text',
        description: 'Text',
        attributeTypes: { type: 'List', itemType: 'NestedModel', model: openhmis.PaymentModeAttributeType }
    },

    toString: function() {
        return this.get('name');
    },

    toJSON: function() {
        if (this.attributes.attributeTypes !== undefined) {
            // Can't set these, so need to remove them from JSON
            for (var attributeType in this.attributes.attributeTypes)
                delete this.attributes.attributeTypes[attributeType].resourceVersion;
        }

        return openhmis.GenericModel.prototype.toJSON.call(this);
    }
});