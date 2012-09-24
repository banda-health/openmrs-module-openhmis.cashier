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
			
			toString: function() {
				return this.get('name');
			}
		});
		
		return openhmis;
	}
);
