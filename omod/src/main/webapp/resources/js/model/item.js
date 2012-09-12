openhmis.ItemCode = openhmis.GenericModel.extend({
	meta: {
		name: "Item Code",
		namePlural: "Item Codes",
		openmrsType: 'metadata',
		resourcePath: 'code'
	},
	schema: {
		code: 'Text'
	}
});

openhmis.Item = openhmis.GenericModel.extend({
	meta: {
		name: "Item",
		namePlural: "Items",
		openmrsType: 'metadata'
	},
	schema: {
        name: 'Text',
		department: {
			type: 'Select',
			options: new openhmis.GenericCollection(null, {
				model: openhmis.Department,
				url: '/department'
			})
		},
		codes: { type: 'List', itemType: 'NestedModel', model: openhmis.ItemCode }
    },
	
	toJSON: function() {
		if (this.attributes.codes !== undefined) {
			for (var code in this.attributes.codes) {
				delete this.attributes.codes[code].resourceVersion;
			}
		}
		return openhmis.GenericModel.prototype.toJSON.call(this);
	}
});