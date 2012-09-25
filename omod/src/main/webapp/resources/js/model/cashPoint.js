openhmis.CashPoint = openhmis.GenericModel.extend({
    meta: {
        name: __("Cash Point"),
        namePlural: __("Cash Points"),
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