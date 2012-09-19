define(
    [
        'lib/backbone',
        'openhmis'
    ],
    function(Backbone, openhmis) {
        openhmis.Patient = Backbone.Model.extend({
            schema: {
                name: 'Text'
            }
        });
        
        return openhmis;
    }
)
