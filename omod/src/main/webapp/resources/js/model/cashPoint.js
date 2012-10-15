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
            
            validate: function(attrs, options) {
    			if (!attrs.name) return { name: __("A name is required") }
                return null;
            },

            toString: function() {
                return this.get('name');
            }
        });

        return openhmis;
    }
);
