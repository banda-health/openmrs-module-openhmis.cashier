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
		
		return openhmis;
	}
);
