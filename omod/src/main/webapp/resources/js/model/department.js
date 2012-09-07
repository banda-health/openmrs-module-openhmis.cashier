openhmis.Department = Backbone.Model.extend({
	meta: {
		name: __("Department"),
		namePlural: __("Departments"),
		openmrsType: 'metadata'
	},
	
	schema: {
		name: 'Text',
		description: 'Text',
		retired: 'Text',
		retireReason: { type: 'Text', readOnly: true }
	}
});