define(
    [
        'lib/backbone',
        'model/generic'
    ],
    function(Backbone, openhmis) {
        openhmis.Patient = openhmis.GenericModel.extend({
            meta: {
                restUrl: 'v1/patient'
            },
            schema: {
                name: 'Text'
            },
            
            initialize: function() {
    			this.simplifyIds(this.attributes);
            },
            
    		// Bit of a hack to get patient identifier from the REF representation
            simplifyIds: function(attrs) {
                var ids = attrs.identifiers;
                if (ids) { for (var i in ids) { if (ids[i].display) {
					var pos = ids[i].display.lastIndexOf('=');
					if (pos !== -1)
						ids[i].display = ids[i].display.substring(pos + 2);
				}}}
            },
            
            parse: function(resp) {
                this.simplifyIds(resp);
                return resp;
            }
        });
        
        return openhmis;
    }
)
