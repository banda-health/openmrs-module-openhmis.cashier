define(
    [
        'openhmis',
        'lib/i18n',
        'model/generic'
    ],
    function(openhmis, __) {
        openhmis.CashPoint = openhmis.GenericModel.extend({
            meta: {
                name: __("Cash Point"),
                namePlural: __("Cash Points"),
                openmrsType: 'metadata',
                restUrl: 'cashPoint'
            },

            schema: {
                name: 'Text',
                description: 'Text'
            },

            toString: function() {
                return this.get('name');
            }
        });

        return openhmis;
    }
);
