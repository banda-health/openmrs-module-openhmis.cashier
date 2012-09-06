openhmis.Department = Backbone.Model.extend({
	meta: {
		name: __("Department"),
		namePlural: __("Departments")
	},
	
	schema: {
		name: 'Text',
		description: 'Text'
	}
});