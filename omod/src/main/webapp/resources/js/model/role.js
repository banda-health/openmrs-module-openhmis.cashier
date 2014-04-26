define(
    [
        openhmis.url.backboneBase + 'js/lib/backbone',
        openhmis.url.backboneBase + 'js/model/generic'
    ],
    function(Backbone, openhmis) {
        openhmis.Role = openhmis.GenericModel.extend({
            meta: {
                restUrl: 'v1/role'
            },
            schema: {
                display: 'Text'
            },

            parse: function(resp) {
                return resp;
            },

            toString: function() {
                    return  this.get("display");
            }
        });

        openhmis.RoleCollection = openhmis.GenericCollection.extend({
			model: openhmis.Role
        })

        return openhmis;
    }
)
