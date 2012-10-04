define(
    [
        'lib/backbone',
        'openhmis'
    ],
    function(Backbone, openhmis) {
        openhmis.Patient = Backbone.Model.extend({
            schema: {
                name: 'Text'
            },
            
            initialize: function() {
   				// Bit of a hack to get patient identifier from the REF representation
    			var ids = this.get("identifiers");
				if (ids) { for (var i in ids) { if (ids[i].display) {
					var pos = ids[i].display.lastIndexOf('=');
					if (pos !== -1)
						ids[i].display = ids[i].display.substring(pos + 2);
				}}}

            }
        });
        
        return openhmis;
    }
)
