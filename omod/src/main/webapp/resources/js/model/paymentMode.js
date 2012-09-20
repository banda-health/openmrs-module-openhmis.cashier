openhmis.PaymentMode = openhmis.GenericModel.extend({
    meta: {
        name: __("Payment Mode"),
        namePlural: __("Payment Modes"),
        openmrsType: 'metadata'
    },

    schema: {
        uuid: { type: 'Text', readOnly: true },
        name: 'Text',
        description: 'Text',
        retired: 'Text',
        retireReason: { type: 'Text', readOnly: true }
    },

    toString: function() {
        return this.get('name');
    }
});