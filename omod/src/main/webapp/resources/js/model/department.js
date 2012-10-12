define(
	[
		'openhmis',
		'lib/i18n',
		'model/generic',
	],
	function(openhmis, __) {
		openhmis.Department = openhmis.GenericModel.extend({
			meta: {
				name: __("Department"),
				namePlural: __("Departments"),
				openmrsType: 'metadata',
				restUrl: 'department'
			},
			
			schema: {
				name: 'Text',
				description: 'Text'
			},
			
			validate: function(attrs, options) {
				if (!attrs.name) return { name: __("A name is required.")}
				return null;
			},
			
			toString: function() {
				return this.get('name');
			}
		});
		
		return openhmis;
	}
);
